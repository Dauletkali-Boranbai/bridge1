package drone.platform;

import java.util.Objects;
import java.util.function.Consumer;

public final class FlightLog {
    private final String source;
    private final Consumer<String> sink;

    public FlightLog(String source, Consumer<String> sink) {
        this.source = Objects.requireNonNull(source, "source");
        this.sink = Objects.requireNonNull(sink, "sink");
    }

    public void record(String message) {
        sink.accept("[" + source + "] " + message);
    }
}
