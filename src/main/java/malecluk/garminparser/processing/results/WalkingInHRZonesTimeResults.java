package malecluk.garminparser.processing.results;

import java.time.Duration;

/**
 * Result produced by a walking heart-rate-zone analyzer.
 *
 * @param name analyzer name from configuration
 * @param countedDuration total duration spent in the configured heart-rate zones
 * @param requiredDuration duration required to complete the analyzer
 * @param isCompleted whether the required duration has been reached
 */
public record WalkingInHRZonesTimeResults(
		String name,
        Duration countedDuration,
        Duration requiredDuration,
        boolean isCompleted)  implements ActivityListenerResult {
}
