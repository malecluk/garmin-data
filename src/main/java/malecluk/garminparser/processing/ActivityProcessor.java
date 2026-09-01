package malecluk.garminparser.processing;

import java.util.List;

import org.springframework.stereotype.Component;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.processing.results.ActivityListenerResult;
import malecluk.garminparser.processing.results.ActivityProcessingResult;

@Component
public class ActivityProcessor {
	
	private final List<ActivityAnalyzer> analyzers;
	
	public ActivityProcessor(
			ActivityAnalyzerFactory factory,
            ActivityProcessingProperties properties) {

        this.analyzers = factory.createAnalyzers(properties);
    }
	
	public void process(Activity activity) {		
		analyzers.forEach(analyzer -> analyzer.onActivity(activity));
    }
	
	public ActivityProcessingResult getResults() {

		List<ActivityListenerResult> results = 
				analyzers.stream()
	            .map(ActivityAnalyzer::getResult)
	            .toList();
		
		return new ActivityProcessingResult(results);
	}
}
