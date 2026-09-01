package malecluk.garminparser.model.activities;

import malecluk.garminparser.model.ActivityIds;

/**
 * Represents a meditation activity.
 * 
 * <p>Contains common activity data inherited from {@link Activity}.
 */
public class Meditation extends Activity {

	/**
	 * Creates a meditation activity with the specified identifiers.
	 * @param ids identifiers associated with the activity
	 */
	public Meditation(ActivityIds ids) {
		super(ids);
	}
	
}
