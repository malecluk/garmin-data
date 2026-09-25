package malecluk.garminparser.processing;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Configuration of a walking average-pace analyzer.
 *
 * <p>The analyzer counts the timer duration of walking activities whose
 * start time falls within the configured date range and whose average pace
 * is below the configured maximum pace.</p>
 */
public class WalkingAvgPaceTimeProperties {
	
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
     * Amount of qualifying walking timer duration required to complete the analyzer.
     */
    private Duration requiredDuration;
    
    /**
     * Maximum average walking pace accepted by the analyzer, expressed in minutes per kilometer.
     *
     * <p>An activity qualifies only when its average pace is strictly below this value.</p>
     */
    private Float maxAveragePace;
    
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
	
	public Float getMaxAveragePace() {
		return maxAveragePace;
	}
	
	public void setMaxAveragePace(Float maxAveragePace) {
		this.maxAveragePace = maxAveragePace;
	}
}
