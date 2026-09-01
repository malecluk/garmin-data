package malecluk.garminparser.model.activities;

import malecluk.garminparser.model.ActivityIds;

/**
 * Represents a breathing activity.
 * 
 * <p>A breathing activity is a {@link TrainingActivity} performed primarily through breathing exercises and practices.</p>
 */
public class Breathing extends TrainingActivity {

	/**
	 * Creates a breathing activity with the specified identifiers.
	 * @param ids identifiers associated with the activity
	 */
	public Breathing(ActivityIds ids) {
		super(ids);
	}
	
}
