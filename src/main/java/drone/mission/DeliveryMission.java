package drone.mission;

import drone.model.Waypoint;
import drone.platform.FlightPlatform;
import java.util.Objects;

public final class DeliveryMission extends Mission {
    private static final double CRUISE_ALTITUDE_M = 60;
    private static final int DROP_HOLD_SECONDS = 3;

    private final Waypoint dropPoint;

    public DeliveryMission(FlightPlatform platform, String parcelLabel, Waypoint dropPoint) {
        super("Delivery of \"" + parcelLabel + "\"", platform);
        this.dropPoint = Objects.requireNonNull(dropPoint, "dropPoint");
    }

    @Override
    public void execute() {
        platform().takeOff(CRUISE_ALTITUDE_M);
        platform().flyTo(dropPoint);
        platform().holdPosition(DROP_HOLD_SECONDS);
        platform().releasePayload();
        platform().flyTo(HOME);
        platform().land();
    }
}
