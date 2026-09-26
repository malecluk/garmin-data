package malecluk.garminparser.fileparser.dto;

import java.util.ArrayList;
import java.util.List;

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
	private final List<ActivityMesg> activityMesgList = new ArrayList<>();
	private final List<DeviceInfoMesg> deviceInfoMesgList = new ArrayList<>();
	private final List<DeviceSettingsMesg> deviceSettingsMesgList = new ArrayList<>();
	private final List<FileIdMesg> fileIdMesgList = new ArrayList<>();
	private final List<HrZoneMesg> hrZoneMesgList = new ArrayList<>();
	private final List<LapMesg> lapMesgList = new ArrayList<>();
	private final List<SessionMesg> sessionMesgList = new ArrayList<>();
	private final List<SportMesg> sportMesgList = new ArrayList<>();
	private final List<TimeInZoneMesg> timeInZoneMesgList = new ArrayList<>();
	private final List<TimestampCorrelationMesg> timestampCorrelationMesgList = new ArrayList<>();
	private final List<UserProfileMesg> userProfileMesgList = new ArrayList<>();
	private final List<ZonesTargetMesg> zonesTargetMesgList = new ArrayList<>();
	
	// -------------------------
	
	/**
	 * Returns list of stored ActivityMesg messages
	 * @return list of ActivityMesg
	 */
	public List<ActivityMesg> getActivityMesgList() {
		return activityMesgList;
	}
	
	/**
	 * Returns list of stored DeviceInfoMesg messages
	 * @return list of DeviceInfoMesg
	 */
	public List<DeviceInfoMesg> getDeviceInfoMesgList() {
		return deviceInfoMesgList;
	}

	/**
	 * Returns list of stored DeviceSettingsMesg messages
	 * @return list of DeviceSettingsMesg
	 */
	public List<DeviceSettingsMesg> getDeviceSettingsMesgList() {
		return deviceSettingsMesgList;
	}
	
	/**
	 * Returns list of stored FileIdMesg messages
	 * @return list of FileIdMesg
	 */
	public List<FileIdMesg> getFileIdMesgList() {
		return fileIdMesgList;
	}
	
	/**
	 * Returns list of stored HrZoneMesg messages
	 * @return list of HrZoneMesg
	 */
	public List<HrZoneMesg> getHrZoneMesgList() {
		return hrZoneMesgList;
	}

	/**
	 * Returns list of stored LapMesg messages
	 * @return list of LapMesg
	 */
	public List<LapMesg> getLapMesgList() {
		return lapMesgList;
	}
	
	/**
	 * Returns list of stored SessionMesg messages
	 * @return list of SessionMesg
	 */
	public List<SessionMesg> getSessionMesgList() {
		return sessionMesgList;
	}
	
	/**
	 * Returns list of stored SportMesg messages
	 * @return list of SportMesg
	 */
	public List<SportMesg> getSportMesgList() {
		return sportMesgList;
	}

	/**
	 * Returns list of stored TimeInZoneMesg messages
	 * @return list of TimeInZoneMesg
	 */
	public List<TimeInZoneMesg> getTimeInZoneMesgList() {
		return timeInZoneMesgList;
	}

	/**
	 * Returns list of stored TimestampCorrelationMesg messages
	 * @return list of TimestampCorrelationMesg
	 */
	public List<TimestampCorrelationMesg> getTimestampCorrelationMesgList() {
		return timestampCorrelationMesgList;
	}

	/**
	 * Returns list of stored UserProfileMesg messages
	 * @return list of UserProfileMesg
	 */
	public List<UserProfileMesg> getUserProfileMesgList() {
		return userProfileMesgList;
	}
	
	/**
	 * Returns list of stored ZonesTargetMesg messages
	 * @return list of ZonesTargetMesg
	 */
	public List<ZonesTargetMesg> getZonesTargetMesgList() {
		return zonesTargetMesgList;
	}	
}
