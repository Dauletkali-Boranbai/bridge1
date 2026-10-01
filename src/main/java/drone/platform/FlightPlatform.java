package drone.platform;

import drone.model.Waypoint;


public interface FlightPlatform {

    String name();

    void takeOff(double altitudeMeters);

    void flyTo(Waypoint target);

    void holdPosition(int seconds);

    void releasePayload();

    void captureImage();

    void land();
}
