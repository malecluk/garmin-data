package malecluk.garminparser.model.activities;

import malecluk.garminparser.model.ActivityIds;

/** 
 * Represents a floor climbing activity.¨
 * 
 * <p>Contains common activity data inherited from {@link Activity}.
 */
public class FloorClimbing extends Activity {

	/**
	 * Creates a floor climbing activity with the specified identifiers.
	 * @param ids identifiers associated with the activity
	 */
	public FloorClimbing(ActivityIds ids) {
		super(ids);
	}

}
