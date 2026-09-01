package malecluk.garminparser.processing.results;

import java.util.List;

/**
 * Contains the results produced by all configured activity analyzers.
 *
 * <p>The order of results represents the order in which analyzers produced
 * their results. Presentation-specific ordering is handled separately.</p>
 *
 * @param results analyzer results
 */
public record ActivityProcessingResult(
        List<ActivityListenerResult> results
) {

}
