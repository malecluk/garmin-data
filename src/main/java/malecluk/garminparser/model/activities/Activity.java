package malecluk.garminparser.model.activities;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.SubSport;
import malecluk.garminparser.model.component.ActivityDeviceInfo;
import malecluk.garminparser.model.component.ActivityElevationData;
import malecluk.garminparser.model.component.HeartRateZones;

public class Activity {
	
	/** 
	 * Information about the device, such as a watch, that recorded the activity.
	 */
	private ActivityDeviceInfo deviceInfo;
	
	/**
	 * Information about elevation changes during the activity, such as ascent and descent.
	 */
	private ActivityElevationData elevationData;
	
	/**
	 * Information about the time spent in individual heart rate zones during the activity.
	 */
	private HeartRateZones heartRateZones;
	
	/**
	 * Date and time when the original activity file was created.
	 */
	private Instant fileCreationTime;
	
	/**
	 * Path from which the original activity file was loaded.
	 */
	private Path filePath;
	
	/**
	 * Activity type, for example RUNNING, WALKING, or CYCLING. 
	 * Uses the application's internal {@link Sport} enum rather than {@code com.garmin.fit.Sport}.
	 */
	private Sport sport;
	
	/**
	 * Localized name of the sport profile used for the activity.
	 */
	private String sportProfileName;
	
	/**
	 * Date and time when the activity started.
	 */
	private Instant startTime;
	
	/**
	 * Activity sub-category, for example TREADMILL for running. 
	 * Uses the application's internal {@link SubSport} enum rather than {@code com.garmin.fit.SubSport}.
	 */
	private SubSport subSport;
	
	/**
	 * Timestamp reported by the FIT SessionMesg message.
	 */
	private Instant timestamp;
	
	/**
	 * Total elapsed duration of the activity, including pauses.
	 */
	private Duration totalElapsedTime;
	
	/**
	 * Total time for which the activity timer was running, excluding pauses.
	 */
	private Duration totalTimerTime;
	
	//-----------------------
	
	/**
	 * Returns information about the device that recorded the activity.
	 * @return activity device information
	 */
	public ActivityDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * Sets information about the device that recorded the activity.
	 * @param deviceInfo activity device information
	 */
	public void setDeviceInfo(ActivityDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	
	/**
	 * Returns information about elevation changes during the activity.
	 * @return activity elevation data
	 */
	public ActivityElevationData getElevationData() {
		return elevationData;
	}

	/**
	 * Sets information about elevation changes during the activity.
	 * @param elevationData activity elevation data
	 */
	public void setElevationData(ActivityElevationData elevationData) {
		this.elevationData = elevationData;
	}

	/**
	 * Returns the date and time when the original activity file was created.
	 * @return activity file creation time
	 */
	public Instant getFileCreationTime() {
		return fileCreationTime;
	}

	/**
	 * Sets the date and time when the original activity file was created.
	 * @param fileCreationTime activity file creation time
	 */
	public void setFileCreationTime(Instant fileCreationTime) {
		this.fileCreationTime = fileCreationTime;
	}
	
	/**
	 * Returns the path from which the original activity file was loaded.
	 * @return original activity file path
	 */
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * Sets the path from which the original activity file was loaded.
	 * @param filePath original activity file path
	 */
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	/**
	 * Returns information about the time spent in individual heart rate zones.
	 * @return heart rate zone information
	 */
	public HeartRateZones getHeartRateZones() {
		return heartRateZones;
	}

	/**
	 * Sets information about the time spent in individual heart rate zones.
	 * @param heartRateZones heart rate zone information
	 */
	public void setHeartRateZones(HeartRateZones heartRateZones) {
		this.heartRateZones = heartRateZones;
	}

	/**
	 * Returns the type of the activity.
	 * @return activity sport type
	 */
	public Sport getSport() {
		return sport;
	}

	/**
	 * Sets the type of the activity.
	 * @param sport activity sport type
	 */
	public void setSport(Sport sport) {
		this.sport = sport;
	}

	/**
	 * Returns the localized name of the sport profile used for the activity.
	 * @return localized sport profile name
	 */
	public String getSportProfileName() {
		return sportProfileName;
	}

	/**
	 * Sets the localized name of the sport profile used for the activity.
	 * @param sportProfileName localized sport profile name
	 */
	public void setSportProfileName(String sportProfileName) {
		this.sportProfileName = sportProfileName;
	}
	
	/**
	 * Returns the date and time when the activity started.
	 * @return activity start time
	 */
	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	/**
	 * Returns the sub-category of the activity.
	 * @return activity sub-sport
	 */
	public SubSport getSubSport() {
		return subSport;
	}

	/**
	 * Sets the sub-category of the activity.
	 * @param subSport activity sub-sport
	 */
	public void setSubSport(SubSport subSport) {
		this.subSport = subSport;
	}

	/**
	 * Returns the timestamp reported by the FIT SessionMesg message.
	 * @return session timestamp
	 */
	public Instant getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp reported by the FIT SessionMesg message.
	 * @param timestamp session timestamp
	 */
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the total elapsed duration of the activity, including pauses.
	 * @return total elapsed activity duration
	 */
	public Duration getTotalElapsedTime() {
		return totalElapsedTime;
	}

	/**
	 * Sets the total elapsed duration of the activity, including pauses.
	 * @param totalElapsedTime total elapsed activity duration
	 */
	public void setTotalElapsedTime(Duration totalElapsedTime) {
		this.totalElapsedTime = totalElapsedTime;
	}

	/**
	 * Returns the total time for which the activity timer was running, * excluding pauses.
	 * @return total timer duration
	 */
	public Duration getTotalTimerTime() {
		return totalTimerTime;
	}

	/**
	 * Sets the total time for which the activity timer was running, * excluding pauses.
	 * @param totalTimerTime total timer duration
	 */
	public void setTotalTimerTime(Duration totalTimerTime) {
		this.totalTimerTime = totalTimerTime;
	}
}
