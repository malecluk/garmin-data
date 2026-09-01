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
	 * Method will set parameters for Activity "a" based on SessionMesg "m"
	 * @param a Activity where params will be set
	 * @param m SessionMesg from which params will be read
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
	 * Method will set parameters for Activity "a" based on SessionMesg "m"
	 * @param a Activity where params will be set
	 * @param m FileIdMesg from which params will be read
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
	 * Helper method for converting Sport from com.garmin.fit.Sport to malecluk.garminparser.model.Sport
	 * @param sport com.garmin.fit.Sport from which sport will be converted
	 * @return malecluk.garminparser.model.Sport representation of param sport
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
	 * Helper method for converting SubSport from com.garmin.fit.SubSport to malecluk.garminparser.model.SubSport
	 * @param subSport com.garmin.fit.SubSport from which sport will be converted
	 * @return malecluk.garminparser.model.SubSport representation of param sport
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
