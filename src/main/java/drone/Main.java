package drone;

import drone.mission.DeliveryMission;
import drone.mission.Mission;
import drone.mission.SurveyMission;
import drone.model.Waypoint;
import drone.platform.FixedWingPlatform;
import drone.platform.FlightPlatform;
import drone.platform.QuadcopterPlatform;
import java.util.Arrays;


public final class Main {

    public static void main(String[] args) {
        FlightPlatform quadcopter = new QuadcopterPlatform(System.out::println);
        FlightPlatform fixedWing = new FixedWingPlatform(System.out::println);

        Mission delivery = new DeliveryMission(quadcopter, "Medical kit", new Waypoint(400, 250));
        Mission survey = new SurveyMission(quadcopter, "North field",
                Arrays.asList(new Waypoint(100, 0), new Waypoint(100, 100), new Waypoint(0, 100)));

        run(delivery);
        delivery.switchPlatform(fixedWing);
        run(delivery);

        run(survey);
        survey.switchPlatform(fixedWing);
        run(survey);
    }

    private static void run(Mission mission) {
        System.out.println();
        System.out.println("=== " + mission.describe() + " ===");
        mission.execute();
    }
}
