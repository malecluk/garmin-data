package malecluk.garminparser.processing;

import java.util.List;

import org.springframework.stereotype.Component;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.processing.results.ActivityListenerResult;
import malecluk.garminparser.processing.results.ActivityProcessingResult;

/**
 * Processes activities through all configured {@link ActivityAnalyzer analyzers}.
 *
 * <p>The analyzers are created from {@link ActivityProcessingProperties} when this processor is initialized.
 * Each processed activity is passed to every configured analyzer.</p>
 */
@Component
public class ActivityProcessor {
	
	private final List<ActivityAnalyzer> analyzers;
	
	public ActivityProcessor(
			ActivityAnalyzerFactory factory,
            ActivityProcessingProperties properties) {

        this.analyzers = factory.createAnalyzers(properties);
    }
	
	/**
	 * Passes the activity to all configured analyzers.
	 * @param activity activity to process
	 */
	public void process(Activity activity) {		
		analyzers.forEach(analyzer -> analyzer.onActivity(activity));
    }
	
	/**
	 * Collects the current results from all configured analyzers.
	 * @return processing result containing one result from each analyzer
	 */
	public ActivityProcessingResult getResults() {

		List<ActivityListenerResult> results = analyzers.stream().map(ActivityAnalyzer::getResult).toList();

		return new ActivityProcessingResult(results);
	}
}
