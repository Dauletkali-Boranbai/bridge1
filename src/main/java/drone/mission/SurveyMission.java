package drone.mission;

import drone.model.Waypoint;
import drone.platform.FlightPlatform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SurveyMission extends Mission {
    private static final double SURVEY_ALTITUDE_M = 120;

    private final List<Waypoint> route;

    public SurveyMission(FlightPlatform platform, String areaName, List<Waypoint> route) {
        super("Survey of " + areaName, platform);
        if (route == null || route.isEmpty()) {
            throw new IllegalArgumentException("A survey needs at least one waypoint");
        }
        this.route = Collections.unmodifiableList(new ArrayList<>(route));
    }

    @Override
    public void execute() {
        platform().takeOff(SURVEY_ALTITUDE_M);
        for (Waypoint point : route) {
            platform().flyTo(point);
            platform().captureImage();
        }
        platform().flyTo(HOME);
        platform().land();
    }
}
