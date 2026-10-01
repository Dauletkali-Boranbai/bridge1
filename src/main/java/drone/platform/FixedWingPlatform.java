package drone.platform;

import drone.model.Waypoint;
import java.util.function.Consumer;

public final class FixedWingPlatform implements FlightPlatform {
    private static final String NAME = "FixedWing";
    private final FlightLog log;

    public FixedWingPlatform(Consumer<String> output) {
        this.log = new FlightLog(NAME, output);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void takeOff(double altitudeMeters) {
        log.record("Runway takeoff run, then climbing to " + altitudeMeters + " m");
    }

    @Override
    public void flyTo(Waypoint target) {
        log.record("Cruising to " + target + " at high speed");
    }

    @Override
    public void holdPosition(int seconds) {
        log.record("Cannot hover: loitering in a circle for " + seconds + " s");
    }

    @Override
    public void releasePayload() {
        log.record("Dropping parcel through the belly hatch");
    }

    @Override
    public void captureImage() {
        log.record("Wing camera captures a wide-area frame");
    }

    @Override
    public void land() {
        log.record("Gliding approach and runway landing");
    }
}
