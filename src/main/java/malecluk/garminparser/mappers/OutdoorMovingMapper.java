package malecluk.garminparser.mappers;

import org.springframework.stereotype.Component;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.activities.OutdoorMovingActivity;
import malecluk.garminparser.model.value.Distance;

@Component
public class OutdoorMovingMapper {

	/**
	 * Maps outdoor-moving activity data from a FIT {@link SessionMesg}
	 * to an {@link OutdoorMovingActivity}.
	 *
	 * <p>Maps enhanced average speed and total distance. Total distance is stored
	 * in the domain model as a {@link Distance} value expressed in meters.</p>
	 *
	 * @param a outdoor-moving activity to update
	 * @param m FIT session message containing the source data
	 */
	public void setOutdoorMovingSessionParams(OutdoorMovingActivity a, SessionMesg m) {
		a.setEnhancedAvgSpeed(m.getEnhancedAvgSpeed());
		
		if (m.getTotalDistance() != null) {
			a.setTotalDistance(new Distance(m.getTotalDistance()));
		} else {
			a.setTotalDistance(null);
		}
	}
	
}
