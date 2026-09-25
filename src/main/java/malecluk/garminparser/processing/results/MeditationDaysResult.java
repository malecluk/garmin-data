package malecluk.garminparser.processing.results;

/**
 * Result produced by a meditation-days analyzer.
 *
 * @param name analyzer name from configuration
 * @param meditatedDays number of distinct calendar days containing a qualifying meditation activity
 * @param requiredDays number of distinct meditation days required to complete the analyzer
 * @param isCompleted whether the required number of meditation days has been reached
 */
public record MeditationDaysResult(
		String name,
        Integer meditatedDays,
        Integer requiredDays,
        boolean isCompleted) implements ActivityListenerResult {

}
