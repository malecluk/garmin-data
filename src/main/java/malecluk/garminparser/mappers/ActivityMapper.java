package malecluk.garminparser.mappers;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.garmin.fit.FileIdMesg;
import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.SubSport;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivityDeviceInfo;

@Component
public class ActivityMapper {

	/**
	 * Maps common activity data from a FIT {@link SessionMesg} to an {@link Activity}.
	 *
	 * <p>Maps sport, sport profile name, sub-sport, start time, timestamp,
	 * total elapsed time, and total timer time.</p>
	 *
	 * <p>The total elapsed time and total timer time are mapped separately because
	 * elapsed time can include paused time while timer time represents the time
	 * during which the activity timer was running.</p>
	 *
	 * @param a activity to update
	 * @param m FIT session message containing the source data
	 */
	public void setBaseSessionParams(Activity a, SessionMesg m) {
		a.setSport(convertSport(m.getSport()));
		a.setSportProfileName(m.getSportProfileName());
		a.setStartTime(m.getStartTime().getDate().toInstant());
		a.setSubSport(convertSubSport(m.getSubSport()));
		a.setTimestamp(m.getTimestamp().getDate().toInstant());
		a.setTotalElapsedTime(Duration.ofMillis(Math.round(m.getTotalElapsedTime() * 1000)));
		a.setTotalTimerTime(Duration.ofMillis(Math.round(m.getTotalTimerTime() * 1000)));
	}
	
	/**
	 * Maps file identification and recording-device data from a FIT
	 * {@link FileIdMesg} to an {@link Activity}.
	 *
	 * <p>Maps the file creation time and creates the activity's
	 * {@link ActivityDeviceInfo} from the manufacturer, product, and serial number
	 * stored in the FIT file ID message.</p>
	 *
	 * @param a activity to update
	 * @param m FIT file ID message containing the source data
	 */
	public void setBaseFileIdParams(Activity a, FileIdMesg m) {
		a.setFileCreationTime(m.getTimeCreated().getDate().toInstant());
		
		ActivityDeviceInfo di = new ActivityDeviceInfo();
		di.setManufacturer(m.getManufacturer());
		di.setProduct(m.getProduct());
		di.setSerialNumber(m.getSerialNumber());
		
		a.setDeviceInfo(di);
	}
	
	/**
	 * Converts a Garmin FIT sport value to the corresponding application-domain
	 * {@link Sport} value.
	 *
	 * <p>FIT sports that are not explicitly supported by the application are
	 * mapped to {@link Sport#UNKNOWN}. A {@code null} FIT sport is also mapped
	 * to {@link Sport#UNKNOWN}.</p>
	 *
	 * @param sport Garmin FIT sport value to convert
	 * @return corresponding application-domain sport
	 */
	private Sport convertSport(com.garmin.fit.Sport sport) {
	    if (sport == null) {
	        return Sport.UNKNOWN;
	    }

	    return switch (sport) {
	        case FLOOR_CLIMBING -> Sport.FLOOR_CLIMBING;
	        case HIKING -> Sport.HIKING;
	        case MEDITATION -> Sport.MEDITATION;
	        case RUNNING -> Sport.RUNNING;
	        case TRAINING -> Sport.TRAINING;
	        case WALKING -> Sport.WALKING;

	        default -> Sport.UNKNOWN;
	    };
	}
	
	/**
	 * Converts a Garmin FIT sub-sport value to the corresponding application-domain
	 * {@link SubSport} value.
	 *
	 * <p>FIT sub-sports that are not explicitly supported by the application are
	 * mapped to {@link SubSport#UNKNOWN}. A {@code null} FIT sub-sport is also
	 * mapped to {@link SubSport#UNKNOWN}.</p>
	 *
	 * @param subSport Garmin FIT sub-sport value to convert
	 * @return corresponding application-domain sub-sport
	 */
	private SubSport convertSubSport(com.garmin.fit.SubSport subSport) {
		if (subSport == null) {
	        return SubSport.UNKNOWN;
	    }
		
		return switch (subSport) {
		    case GENERIC -> SubSport.GENERIC;
		    case RUCKING -> SubSport.RUCKING;
		    case YOGA -> SubSport.YOGA;
		    
		    default -> SubSport.UNKNOWN;
		};
	}
}
