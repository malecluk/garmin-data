package malecluk.garminparser.model.activities;

import malecluk.garminparser.model.ActivityIds;

/**
 * Represents a running activity.
 * 
 * <p>A running activity is an {@link OutdoorMovingActivity} performed primarily
 * by running. It inherits common activity information and outdoor movement
 * metrics such as duration, distance, and average pace from its parent class.</p>
 */
public class Running extends OutdoorMovingActivity {

	/**
	 * Creates a running activity with the specified identifiers.
	 * @param ids identifiers associated with the activity
	 */
	public Running(ActivityIds ids) {
		super(ids);
	}
	
}
