package malecluk.garminparser.processing.results;

import java.time.Instant;

/**
 * Marker interface for results produced by {@link ActivityAnalyzer activity analyzers}.
 *
 * <p>Each analyzer returns its own result type, while this common interface
 * allows all analyzer results to be collected in a single
 * {@link ActivityProcessingResult}.</p>
 */
public interface ActivityListenerResult {
	
	/**
     * Returns the start of the period covered by this result.
     *
     * @return period start
     */
    Instant startDate();

    /**
     * Returns the end of the period covered by this result.
     *
     * @return period end
     */
    Instant endDate();

}
