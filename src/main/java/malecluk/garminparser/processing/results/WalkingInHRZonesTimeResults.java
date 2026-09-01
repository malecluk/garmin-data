package malecluk.garminparser.processing.results;

import java.time.Duration;
import java.time.Instant;

/**
 * Result of the walking in heart-rate zones analyzer for a configured period.
 *
 * @param name name of the analyzed challenge
 * @param startDate start of the analyzed period
 * @param endDate end of the analyzed period
 * @param countedDuration walking duration counted in the required heart-rate zones
 * @param requiredDuration duration required to complete the challenge
 * @param isCompleted whether the challenge has been completed
 */
public record WalkingInHRZonesTimeResults(
        String name,
        Instant startDate,
        Instant endDate,
        Duration countedDuration,
        Duration requiredDuration,
        boolean isCompleted) implements ActivityListenerResult {
}
