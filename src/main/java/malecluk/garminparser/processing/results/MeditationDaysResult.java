package malecluk.garminparser.processing.results;

import java.time.Instant;

/**
 * Result of the meditation days analyzer for a configured period.
 *
 * @param name name of the analyzed challenge
 * @param startDate start of the analyzed period
 * @param endDate end of the analyzed period
 * @param meditatedDays number of days with a qualifying meditation
 * @param requiredDays number of days required to complete the challenge
 * @param isCompleted whether the challenge has been completed
 */
public record MeditationDaysResult(
        String name,
        Instant startDate,
        Instant endDate,
        Integer meditatedDays,
        Integer requiredDays,
        boolean isCompleted) implements ActivityListenerResult {
}
