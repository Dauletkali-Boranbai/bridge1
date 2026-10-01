package drone;

import drone.mission.DeliveryMission;
import drone.mission.Mission;
import drone.mission.SurveyMission;
import drone.model.Waypoint;
import drone.platform.FixedWingPlatform;
import drone.platform.FlightPlatform;
import drone.platform.QuadcopterPlatform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public final class BridgeTests {


    private static final class RecordingPlatform implements FlightPlatform {
        final List<String> calls = new ArrayList<>();
        public String name() { return "Recording"; }
        public void takeOff(double altitudeMeters) { calls.add("takeOff"); }
        public void flyTo(Waypoint target) { calls.add("flyTo" + target); }
        public void holdPosition(int seconds) { calls.add("hold"); }
        public void releasePayload() { calls.add("release"); }
        public void captureImage() { calls.add("capture"); }
        public void land() { calls.add("land"); }
    }

    private static int failures = 0;

    public static void main(String[] args) {
        deliveryDelegatesStepsInOrder();
        surveyCapturesOneImagePerWaypoint();
        switchingPlatformKeepsTheSameSteps();
        sameMissionBehavesDifferentlyOnDifferentPlatforms();
        invalidArgumentsAreRejected();
        System.out.println(failures == 0 ? "ALL TESTS PASSED" : failures + " TEST(S) FAILED");
        System.exit(failures == 0 ? 0 : 1);
    }

    private static void deliveryDelegatesStepsInOrder() {
        RecordingPlatform platform = new RecordingPlatform();
        new DeliveryMission(platform, "Box", new Waypoint(5, 5)).execute();
        check("delivery order", platform.calls,
                Arrays.asList("takeOff", "flyTo(5.0, 5.0)", "hold", "release", "flyTo(0.0, 0.0)", "land"));
    }

    private static void surveyCapturesOneImagePerWaypoint() {
        RecordingPlatform platform = new RecordingPlatform();
        new SurveyMission(platform, "Field", Arrays.asList(new Waypoint(1, 1), new Waypoint(2, 2))).execute();
        check("survey order", platform.calls, Arrays.asList(
                "takeOff", "flyTo(1.0, 1.0)", "capture", "flyTo(2.0, 2.0)", "capture", "flyTo(0.0, 0.0)", "land"));
    }

    private static void switchingPlatformKeepsTheSameSteps() {
        RecordingPlatform first = new RecordingPlatform();
        RecordingPlatform second = new RecordingPlatform();
        Mission mission = new DeliveryMission(first, "Box", new Waypoint(5, 5));
        mission.execute();
        mission.switchPlatform(second);
        mission.execute();
        check("switch: first platform used once", first.calls.size(), 6);
        check("switch: second platform got identical steps", second.calls, first.calls);
    }

    private static void sameMissionBehavesDifferentlyOnDifferentPlatforms() {
        List<String> quadLog = new ArrayList<>();
        List<String> wingLog = new ArrayList<>();
        new DeliveryMission(new QuadcopterPlatform(quadLog::add), "Box", new Waypoint(5, 5)).execute();
        new DeliveryMission(new FixedWingPlatform(wingLog::add), "Box", new Waypoint(5, 5)).execute();
        check("same number of steps", quadLog.size(), wingLog.size());
        check("quadcopter hovers", quadLog.get(2).contains("Hovering"), true);
        check("fixed wing loiters", wingLog.get(2).contains("loitering"), true);
    }

    private static void invalidArgumentsAreRejected() {
        RecordingPlatform platform = new RecordingPlatform();
        expectFailure("null platform", () -> new DeliveryMission(null, "Box", new Waypoint(1, 1)));
        expectFailure("null drop point", () -> new DeliveryMission(platform, "Box", null));
        expectFailure("empty route", () -> new SurveyMission(platform, "Field", Collections.<Waypoint>emptyList()));
        Mission mission = new DeliveryMission(platform, "Box", new Waypoint(1, 1));
        expectFailure("switch to null", () -> mission.switchPlatform(null));
    }

    private static void expectFailure(String name, Runnable action) {
        try {
            action.run();
            fail(name + ": expected an exception");
        } catch (NullPointerException | IllegalArgumentException expected) {
            System.out.println("PASS " + name);
        }
    }

    private static void check(String name, Object actual, Object expected) {
        if (actual.equals(expected)) {
            System.out.println("PASS " + name);
        } else {
            fail(name + ": expected " + expected + " but was " + actual);
        }
    }

    private static void fail(String message) {
        failures++;
        System.out.println("FAIL " + message);
    }
}
