package drone.platform;

import drone.model.Waypoint;
import java.util.function.Consumer;


public final class QuadcopterPlatform implements FlightPlatform {
    private static final String NAME = "Quadcopter";
    private final FlightLog log;

    public QuadcopterPlatform(Consumer<String> output) {
        this.log = new FlightLog(NAME, output);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void takeOff(double altitudeMeters) {
        log.record("Vertical takeoff, climbing straight up to " + altitudeMeters + " m");
    }

    @Override
    public void flyTo(Waypoint target) {
        log.record("Flying to " + target + " with rotors tilted forward");
    }

    @Override
    public void holdPosition(int seconds) {
        log.record("Hovering in place for " + seconds + " s");
    }

    @Override
    public void releasePayload() {
        log.record("Lowering parcel on the winch while hovering");
    }

    @Override
    public void captureImage() {
        log.record("Gimbal camera takes a still image");
    }

    @Override
    public void land() {
        log.record("Vertical landing on the pad");
    }
}
