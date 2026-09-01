package malecluk.garminparser.processing.results;

import java.util.List;
import java.util.Objects;

/**
 * Contains the results produced by all configured activity analyzers.
 *
 * <p>
 * The order of results represents the order in which analyzers produced their
 * results. Presentation-specific ordering is handled separately.
 * </p>
 *
 * @param results analyzer results
 */
public record ActivityProcessingResult(List<ActivityListenerResult> results) {
	
	public ActivityProcessingResult {
		results = List.copyOf(Objects.requireNonNull(results, "results must not be null"));
	}
}
