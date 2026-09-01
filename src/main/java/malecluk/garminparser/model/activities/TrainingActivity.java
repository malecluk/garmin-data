package malecluk.garminparser.model.activities;

import malecluk.garminparser.model.ActivityIds;

/**
 * Represents a general training activity.
 * 
 * <p>A training activity is an {@link Activity} that does not currently have
 * a more specific activity type represented by a dedicated domain class.</p>
 */
public class TrainingActivity extends Activity {

	/**
	 * Creates a training activity with the specified identifiers.
	 * @param ids identifiers associated with the activity
	 */
	public TrainingActivity(ActivityIds ids) {
		super(ids);
	}
}
