package malecluk.garminparser.processing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Configuration of a walking heart-rate-zone analyzer.
 *
 * <p>The analyzer counts time spent in the configured heart-rate zones during
 * walking activities whose start time falls within the configured date range.</p>
 */
public class WalkingInHRZonesTimeProperties {
	
	/**
	 * Name of the analyzer used in its result and console output.
	 */
	private String name;
	
	/**
	 * Start of the date and time range in which walking activities are counted.
	 */
    private LocalDateTime startDate;
    
    /**
     * End of the date and time range in which walking activities are counted.
     */
    private LocalDateTime endDate;
    
    /**
     * Amount of qualifying time spent in the configured heart-rate zones required
     * to complete the analyzer.
     */
    private Duration requiredDuration;
    
    /**
     * Actual heart-rate zone numbers whose recorded durations are included in the count.
     *
     * <p>The numbers identify zones by their {@link malecluk.garminparser.model.component.HeartRateZone#number()}
     * value and are not interpreted as indexes into the zone list.</p>
     */
    private List<Integer> requiredZones;
    
    //-----------------------
    
    /**
     * Returns the analyzer name.
     *
     * @return analyzer name
     */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the analyzer name.
	 *
	 * @param name analyzer name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the start of the analyzer period.
	 *
	 * @return start of the analyzer period
	 */
	public LocalDateTime getStartDate() {
		return startDate;
	}
	
	/**
	 * Sets the start of the analyzer period.
	 *
	 * @param startDate start of the analyzer period
	 */
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Returns the end of the analyzer period.
	 *
	 * @return end of the analyzer period
	 */
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	/**
	 * Sets the end of the analyzer period.
	 *
	 * @param endDate end of the analyzer period
	 */
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	
	public Duration getRequiredDuration() {
		return requiredDuration;
	}
	
	public void setRequiredDuration(Duration requiredDuration) {
		this.requiredDuration = requiredDuration;
	}
	
	public List<Integer> getRequiredZones() {
		return requiredZones;
	}
	
	public void setRequiredZones(List<Integer> requiredZones) {
		this.requiredZones = requiredZones;
	}
}
