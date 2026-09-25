package malecluk.garminparser.processing.results;

import java.util.List;

/**
 * Aggregated results produced by all configured activity analyzers.
 * @param results one result for each configured analyzer
 */
public record ActivityProcessingResult(
        List<ActivityListenerResult> results
) {

}
