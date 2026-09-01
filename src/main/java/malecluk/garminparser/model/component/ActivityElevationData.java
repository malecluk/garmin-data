package malecluk.garminparser.model.component;

import malecluk.garminparser.model.value.Distance;

public class ActivityElevationData {

	private Distance totalAscent;
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
