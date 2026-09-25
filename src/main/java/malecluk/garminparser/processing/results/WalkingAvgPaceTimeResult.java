package malecluk.garminparser.processing.results;

import java.time.Duration;

/**
 * Result of a walking average-pace analyzer.
 *
 * @param name analyzer name from configuration
 * @param countedDuration total timer duration of qualifying walking activities
 * @param requiredDuration duration required to complete the analyzer
 * @param maxAveragePace maximum allowed average pace in minutes per kilometer
 * @param isCompleted whether the required duration has been reached
 */
public record WalkingAvgPaceTimeResult(
		String name,
        Duration countedDuration,
        Duration requiredDuration,
        Float maxAveragePace,
        boolean isCompleted) implements ActivityListenerResult {

}
