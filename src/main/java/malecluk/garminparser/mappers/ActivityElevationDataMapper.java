package malecluk.garminparser.mappers;

import org.springframework.stereotype.Component;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivityElevationData;
import malecluk.garminparser.model.value.Distance;

@Component
public class ActivityElevationDataMapper {

	/**
	 * Method will set parameters for elevation (ascent, descent, ...) for Activity "a" based on SessionMesg "m"
	 * @param a Activity where params will be set
	 * @param m SessionMesg from which params will be read
	 */
	public void setActivityElevationData(Activity a, SessionMesg m) {
		if (a.getElevationData() == null) {
			a.setElevationData(new ActivityElevationData());
		}
		
		if (m.getTotalAscent() != null) {
			a.getElevationData().setTotalAscent(new Distance(m.getTotalAscent()));
		} else {
			a.getElevationData().setTotalAscent(null);
		}
		
		if (m.getTotalDescent() != null) {
			a.getElevationData().setTotalDescent(new Distance(m.getTotalDescent()));
		} else {
			a.getElevationData().setTotalDescent(null);
		}
	}
}
