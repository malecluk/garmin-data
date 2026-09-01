package malecluk.garminparser.processing.results;

import java.time.Instant;

import malecluk.garminparser.model.value.Distance;

/**
 * Result produced by the running downhill descent analyzer.
 *
 * @param name analyzer name
 * @param startDate start of the analyzer period
 * @param endDate end of the analyzer period
 * @param countedDescent total descent counted so far
 * @param requiredDescent descent distance required to complete the analyzer
 * @param isCompleted whether the required descent distance has been reached
 */
public record RunningDownhillDescentResult(
        String name,
        Instant startDate,
        Instant endDate,
        Distance countedDescent,
        Distance requiredDescent,
        boolean isCompleted) implements ActivityListenerResult {
}
