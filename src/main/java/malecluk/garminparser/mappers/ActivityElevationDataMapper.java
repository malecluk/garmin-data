package malecluk.garminparser.mappers;

import org.springframework.stereotype.Component;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivityElevationData;
import malecluk.garminparser.model.value.Distance;

@Component
public class ActivityElevationDataMapper {

	/**
	 * Maps total ascent and total descent from a FIT {@link SessionMesg}
	 * to an {@link Activity}.
	 *
	 * <p>If the activity does not yet contain elevation data, a new
	 * {@link ActivityElevationData} instance is created. Missing ascent or
	 * descent values in the FIT message are mapped to {@code null}.</p>
	 *
	 * @param a activity to update
	 * @param m FIT session message containing elevation data
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
