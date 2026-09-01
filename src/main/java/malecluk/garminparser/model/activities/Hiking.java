package malecluk.garminparser.model.activities;

import malecluk.garminparser.model.ActivityIds;

/**
 * Represents a hiking activity.
 * 
 * <p>Contains common activity data inherited from {@link OutdoorMovingActivity}.
 */
public class Hiking extends OutdoorMovingActivity {

	/**
	 * Creates a hiking activity with the specified identifiers.
	 * @param ids identifiers associated with the activity
	 */
	public Hiking(ActivityIds ids) {
		super(ids);
	}
	
}
