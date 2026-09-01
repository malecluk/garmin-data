package malecluk.garminparser.model.component;

import malecluk.garminparser.model.value.Distance;

/**
 * Elevation-related summary data for an activity.
 */
public class ActivityElevationData {

	/**
	 * Total ascent during the activity, expressed as a {@link Distance} in meters.
	 */
	private Distance totalAscent;
	
	/**
	 * Total descent during the activity, expressed as a {@link Distance} in meters.
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
