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
    
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LocalDateTime getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
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
