package malecluk.garminparser.processing;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.processing.results.ActivityListenerResult;

/**
 * Analyzes activities incrementally and produces a result for a configured
 * activity-processing task.
 *
 * <p>An analyzer receives activities one by one through {@link #onActivity(Activity)}
 * and keeps its internal state until {@link #getResult()} is called.</p>
 */
public interface ActivityAnalyzer {
	
	/**
     * Processes one activity.
     *
     * <p>The analyzer decides whether the activity is relevant to its 
     * configuration and updates its internal state accordingly.</p>
     *
     * @param activity activity to process
     */
	void onActivity(Activity activity);
	
	/**
     * Returns the current result of the analysis.
     * @return current analysis result
     */
	ActivityListenerResult getResult();
}
