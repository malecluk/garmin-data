package malecluk.garminparser.processing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for activity analyzers.
 *
 * <p>Each configured entry creates one independent
 * {@link ActivityAnalyzer} instance.</p>
 */
@ConfigurationProperties(prefix = "activity-processing")
public class ActivityProcessingProperties {

	/**
	 * Configurations for walking average-pace analyzers.
	 */
	private List<WalkingAvgPaceTimeProperties> walkingAvgPaceTime = new ArrayList<>();

	/**
	 * Configurations for meditation days analyzers.
	 */
    private List<MeditationDaysProperties> meditationDays = new ArrayList<>();
    
    /**
     * Configurations for walking in HR zones analyzers.
     */
    private List<WalkingInHRZonesTimeProperties> walkingHRZones = new ArrayList<>();
    
    /**
     * Configurations for running descent analyzers.
     */
    private List<RunningDownhillDescentProperties> runningDescents = new ArrayList<>();

    //-----------------------
    
	public List<WalkingAvgPaceTimeProperties> getWalkingAvgPaceTime() {
		return walkingAvgPaceTime;
	}

	public void setWalkingAvgPaceTime(List<WalkingAvgPaceTimeProperties> walkingAvgPaceTime) {
		this.walkingAvgPaceTime = walkingAvgPaceTime;
	}

	public List<MeditationDaysProperties> getMeditationDays() {
		return meditationDays;
	}

	public void setMeditationDays(List<MeditationDaysProperties> meditationDays) {
		this.meditationDays = meditationDays;
	}

	public List<WalkingInHRZonesTimeProperties> getWalkingHRZones() {
		return walkingHRZones;
	}

	public void setWalkingHRZones(List<WalkingInHRZonesTimeProperties> walkingHRZones) {
		this.walkingHRZones = walkingHRZones;
	}

	/**
	 * Returns the configured running descent analyzers.
	 *
	 * @return running descent analyzer configurations
	 */
	public List<RunningDownhillDescentProperties> getRunningDescents() {
		return runningDescents;
	}

	/**
	 * Sets the running descent analyzer configurations.
	 *
	 * @param runningDescents running descent analyzer configurations
	 */
	public void setRunningDescents(List<RunningDownhillDescentProperties> runningDescents) {
		this.runningDescents = runningDescents;
	}
}
