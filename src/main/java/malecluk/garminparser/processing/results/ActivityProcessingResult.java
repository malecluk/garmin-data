package malecluk.garminparser.processing.results;

import java.util.List;

/**
 * Aggregated results produced by all configured activity analyzers.
 *
 * <p>The order of results corresponds to the order in which the analyzers
 * are registered with the activity processor.</p>
 *
 * @param results result produced by each configured activity analyzer
 */
public record ActivityProcessingResult(
        List<ActivityListenerResult> results
) {

}
