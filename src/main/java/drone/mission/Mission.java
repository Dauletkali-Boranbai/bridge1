package drone.mission;

import drone.model.Waypoint;
import drone.platform.FlightPlatform;
import java.util.Objects;

public abstract class Mission {
    protected static final Waypoint HOME = new Waypoint(0, 0);

    private final String title;
    private FlightPlatform platform;

    protected Mission(String title, FlightPlatform platform) {
        this.title = Objects.requireNonNull(title, "title");
        this.platform = Objects.requireNonNull(platform, "platform");
    }

    /** Changes the implementation at runtime; the mission itself is untouched. */
    public final void switchPlatform(FlightPlatform newPlatform) {
        this.platform = Objects.requireNonNull(newPlatform, "newPlatform");
    }

    protected final FlightPlatform platform() {
        return platform;
    }

    public final String describe() {
        return title + " using " + platform.name();
    }

    public abstract void execute();
}
