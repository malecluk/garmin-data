package malecluk.garminparser.fileparser.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.garmin.fit.ActivityMesg;
import com.garmin.fit.DeviceInfoMesg;
import com.garmin.fit.DeviceSettingsMesg;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.HrZoneMesg;
import com.garmin.fit.LapMesg;
import com.garmin.fit.Mesg;
import com.garmin.fit.MesgListener;
import com.garmin.fit.MesgNum;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.SportMesg;
import com.garmin.fit.TimeInZoneMesg;
import com.garmin.fit.TimestampCorrelationMesg;
import com.garmin.fit.UserProfileMesg;
import com.garmin.fit.ZonesTargetMesg;

import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;

/**
 * Receives decoded FIT messages and stores supported message types
 * in a {@link ParsedFitFileMessagesDTO}.
 *
 * <p>Messages not explicitly handled by this listener are ignored.</p>
 */
public class MyMesgListener implements MesgListener {
	
	private static final Logger log = LogManager.getLogger(MyMesgListener.class);

	private final ParsedFitFileMessagesDTO messagesDTO = new ParsedFitFileMessagesDTO();
	
	/**
	 * Receives one decoded FIT message and converts supported message types
	 * to their corresponding FIT SDK message class before storing them.
	 *
	 * @param mesg decoded FIT message
	 */
	@Override
	public void onMesg(Mesg mesg) {
		log.debug("onMesg(): ({}) {}", mesg.getNum(), mesg.getName());
		
		switch (mesg.getNum()) {
		
		    case MesgNum.ACTIVITY -> {
		    	messagesDTO.getActivityMesgList().add(new ActivityMesg(mesg));
		    	log.debug("Added ActivityMesg");
		    }
			case MesgNum.DEVICE_INFO -> {
				messagesDTO.getDeviceInfoMesgList().add(new DeviceInfoMesg(mesg));
				log.debug("Added DeviceInfoMesg");
			}
			
			case MesgNum.DEVICE_SETTINGS -> {
				messagesDTO.getDeviceSettingsMesgList().add(new DeviceSettingsMesg(mesg));
				log.debug("Added DeviceSettingsMesg");
			}
			
			case MesgNum.FILE_ID -> {
				messagesDTO.getFileIdMesgList().add(new FileIdMesg(mesg));
				log.debug("Added FileIdMesg");
			}
			
			case MesgNum.HR_ZONE -> {
				messagesDTO.getHrZoneMesgList().add(new HrZoneMesg(mesg));
				log.debug("Added HrZoneMesg");
			}
			    
			case MesgNum.LAP -> {
				messagesDTO.getLapMesgList().add(new LapMesg(mesg));
				log.debug("Added LapMesg");
			}
			
			case MesgNum.SESSION -> {
				messagesDTO.getSessionMesgList().add(new SessionMesg(mesg));
				log.debug("Added SessionMesg");
			}
			
			case MesgNum.SPORT -> {
				messagesDTO.getSportMesgList().add(new SportMesg(mesg));
				log.debug("Added SportMesg");
			}
			
			case MesgNum.TIME_IN_ZONE -> {
				messagesDTO.getTimeInZoneMesgList().add(new TimeInZoneMesg(mesg));
				log.debug("Added TimeInZoneMesg");
			}
			
			case MesgNum.TIMESTAMP_CORRELATION -> {
				messagesDTO.getTimestampCorrelationMesgList().add(new TimestampCorrelationMesg(mesg));
				log.debug("Added TimestampCorrelationMesg");
			}
			
			case MesgNum.USER_PROFILE -> {
				messagesDTO.getUserProfileMesgList().add(new UserProfileMesg(mesg));
				log.debug("Added UserProfileMesg");
			}
			
			case MesgNum.ZONES_TARGET -> {
				messagesDTO.getZonesTargetMesgList().add(new ZonesTargetMesg(mesg));
				log.debug("Added ZonesTargetMesg");
			}
			
			default -> {}
		}
	}
	
	public ParsedFitFileMessagesDTO getMessages() {
		return messagesDTO;
	}

}
