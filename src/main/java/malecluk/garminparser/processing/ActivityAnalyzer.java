package malecluk.garminparser.processing;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.processing.results.ActivityListenerResult;

/**
 * 
 */
public interface ActivityAnalyzer {
	
	void onActivity(Activity activity);
	
	ActivityListenerResult getResult();
}
