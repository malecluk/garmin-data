package malecluk.garminparser.model.activities;

import malecluk.garminparser.model.ActivityIds;

/**
 * Represents a yoga activity.
 * 
 * <p>A yoga activity is a {@link TrainingActivity} performed primarily through yoga exercises and practices.</p>
 */
public class Yoga extends TrainingActivity {

	/**
	 * Creates an yoga activity with the specified identifiers.
	 * @param ids identifiers associated with the activity
	 */
	public Yoga(ActivityIds ids) {
		super(ids);
	}
	
}
