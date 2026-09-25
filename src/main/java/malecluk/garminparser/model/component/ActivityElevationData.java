package malecluk.garminparser.model.component;

import malecluk.garminparser.model.value.Distance;

/**
 * Elevation-related summary data for an activity.
 */
public class ActivityElevationData {

	/**
	 * Total elevation gained during the activity.
	 */	
	private Distance totalAscent;
	
	/**
	 * Total elevation lost during the activity.
	 */
    private Distance totalDescent;
    
    //-----------------------
    
	public Distance getTotalAscent() {
		return totalAscent;
	}
	
	public void setTotalAscent(Distance totalAscent) {
		this.totalAscent = totalAscent;
	}
	
	public Distance getTotalDescent() {
		return totalDescent;
	}
	
	public void setTotalDescent(Distance totalDescent) {
		this.totalDescent = totalDescent;
	}
}
