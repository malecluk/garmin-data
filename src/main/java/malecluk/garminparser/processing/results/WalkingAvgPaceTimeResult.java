package malecluk.garminparser.processing.results;

import java.time.Duration;

public record WalkingAvgPaceTimeResult(
		String name,
        Duration countedDuration,
        Duration requiredDuration,
        Float maxAveragePace,
        boolean isCompleted) implements ActivityListenerResult {

}
