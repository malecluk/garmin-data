package malecluk.garminparser.processing.results;

import java.time.Duration;

public record WalkingInHRZonesTimeResults(
		String name,
        Duration countedDuration,
        Duration requiredDuration,
        boolean isCompleted)  implements ActivityListenerResult {
}
