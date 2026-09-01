package malecluk.garminparser.fileparser.dto;

import java.util.ArrayList;

import com.garmin.fit.ActivityMesg;
import com.garmin.fit.DeviceInfoMesg;
import com.garmin.fit.DeviceSettingsMesg;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.HrZoneMesg;
import com.garmin.fit.LapMesg;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.SportMesg;
import com.garmin.fit.TimeInZoneMesg;
import com.garmin.fit.TimestampCorrelationMesg;
import com.garmin.fit.UserProfileMesg;
import com.garmin.fit.ZonesTargetMesg;

/**
 * DTO object for returning parsed messages from fit files.
 * Contains lists of messages which were read from FIT file. 
 * There are no parsed values of single fields in this DTO, parsing of messages must be done in next processing step.
 */
public class ParsedFitFileMessagesDTO {
	private final ArrayList<ActivityMesg> activityMesgList = new ArrayList<>();
	private final ArrayList<DeviceInfoMesg> deviceInfoMesgList = new ArrayList<>();
	private final ArrayList<DeviceSettingsMesg> deviceSettingsMesgList = new ArrayList<>();
	private final ArrayList<FileIdMesg> fileIdMesgList = new ArrayList<>();
	private final ArrayList<HrZoneMesg> hrZoneMesgList = new ArrayList<>();
	private final ArrayList<LapMesg> lapMesgList = new ArrayList<>();
	private final ArrayList<SessionMesg> sessionMesgList = new ArrayList<>();
	private final ArrayList<SportMesg> sportMesgList = new ArrayList<>();
	private final ArrayList<TimeInZoneMesg> timeInZoneMesgList = new ArrayList<>();
	private final ArrayList<TimestampCorrelationMesg> timestampCorrelationMesgList = new ArrayList<>();
	private final ArrayList<UserProfileMesg> userProfileMesgList = new ArrayList<>();
	private final ArrayList<ZonesTargetMesg> zonesTargetMesgList = new ArrayList<>();
	
	// -------------------------
	
	/**
	 * Returns list of stored ActivityMesg messages
	 * @return list of ActivityMesg
	 */
	public ArrayList<ActivityMesg> getActivityMesgList() {
		return activityMesgList;
	}
	
	/**
	 * Returns list of stored DeviceInfoMesg messages
	 * @return list of DeviceInfoMesg
	 */
	public ArrayList<DeviceInfoMesg> getDeviceInfoMesgList() {
		return deviceInfoMesgList;
	}

	/**
	 * Returns list of stored DeviceSettingsMesg messages
	 * @return list of DeviceSettingsMesg
	 */
	public ArrayList<DeviceSettingsMesg> getDeviceSettingsMesgList() {
		return deviceSettingsMesgList;
	}
	
	/**
	 * Returns list of stored FileIdMesg messages
	 * @return list of FileIdMesg
	 */
	public ArrayList<FileIdMesg> getFileIdMesgList() {
		return fileIdMesgList;
	}
	
	/**
	 * Returns list of stored HrZoneMesg messages
	 * @return list of HrZoneMesg
	 */
	public ArrayList<HrZoneMesg> getHrZoneMesgList() {
		return hrZoneMesgList;
	}

	/**
	 * Returns list of stored LapMesg messages
	 * @return list of LapMesg
	 */
	public ArrayList<LapMesg> getLapMesgList() {
		return lapMesgList;
	}
	
	/**
	 * Returns list of stored SessionMesg messages
	 * @return list of SessionMesg
	 */
	public ArrayList<SessionMesg> getSessionMesgList() {
		return sessionMesgList;
	}
	
	/**
	 * Returns list of stored SportMesg messages
	 * @return list of SportMesg
	 */
	public ArrayList<SportMesg> getSportMesgList() {
		return sportMesgList;
	}

	/**
	 * Returns list of stored TimeInZoneMesg messages
	 * @return list of TimeInZoneMesg
	 */
	public ArrayList<TimeInZoneMesg> getTimeInZoneMesgList() {
		return timeInZoneMesgList;
	}

	/**
	 * Returns list of stored TimestampCorrelationMesg messages
	 * @return list of TimestampCorrelationMesg
	 */
	public ArrayList<TimestampCorrelationMesg> getTimestampCorrelationMesgList() {
		return timestampCorrelationMesgList;
	}

	/**
	 * Returns list of stored UserProfileMesg messages
	 * @return list of UserProfileMesg
	 */
	public ArrayList<UserProfileMesg> getUserProfileMesgList() {
		return userProfileMesgList;
	}
	
	/**
	 * Returns list of stored ZonesTargetMesg messages
	 * @return list of ZonesTargetMesg
	 */
	public ArrayList<ZonesTargetMesg> getZonesTargetMesgList() {
		return zonesTargetMesgList;
	}	
}
