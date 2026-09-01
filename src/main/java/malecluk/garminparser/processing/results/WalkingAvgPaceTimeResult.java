package malecluk.garminparser.processing.results;

import java.time.Duration;
import java.time.Instant;

/**
 * Result of the walking average pace time analyzer for a configured period.
 *
 * @param name name of the analyzed challenge
 * @param startDate start of the analyzed period
 * @param endDate end of the analyzed period
 * @param countedDuration walking duration counted towards the challenge
 * @param requiredDuration walking duration required to complete the challenge
 * @param maxAveragePace maximum allowed average pace
 * @param isCompleted whether the challenge has been completed
 */
public record WalkingAvgPaceTimeResult(
        String name,
        Instant startDate,
        Instant endDate,
        Duration countedDuration,
        Duration requiredDuration,
        Float maxAveragePace,
        boolean isCompleted) implements ActivityListenerResult {
}
