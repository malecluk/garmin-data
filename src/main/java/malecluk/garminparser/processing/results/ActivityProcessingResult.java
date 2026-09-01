package malecluk.garminparser.processing.results;

import java.util.List;

public record ActivityProcessingResult(
        List<ActivityListenerResult> results
) {

}
