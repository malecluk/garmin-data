package malecluk.garminparser.mappers;

import org.springframework.stereotype.Component;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.activities.OutdoorMovingActivity;
import malecluk.garminparser.model.value.Distance;

@Component
public class OutdoorMovingMapper {

	/**
	 * Method will set parameters for OutdoorMovingActivity "a" based on SessionMesg "m"
	 * @param a OutdoorMovingActivity where params will be set
	 * @param m SessionMesg from which params will be read
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
