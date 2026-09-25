package malecluk.garminparser.processing;

import java.time.LocalDateTime;

/**
 * Configuration of a meditation-days analyzer.
 *
 * <p>The analyzer counts distinct calendar days on which a meditation
 * activity starts within the configured date range.</p>
 */
public class MeditationDaysProperties {

	/**
	 * Name of the analyzer used in its result and console output.
	 */
	private String name;
	
	/**
	 * Start of the date and time range in which meditation activities are counted.
	 */
    private LocalDateTime startDate;
    
    /**
     * End of the date and time range in which meditation activities are counted.
     */
    private LocalDateTime endDate;
    
    /**
     * Number of distinct meditation days required to complete the analyzer.
     */
    private Integer requiredDays;
    
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
	
	public Integer getRequiredDays() {
		return requiredDays;
	}
	
	public void setRequiredDays(Integer requiredDays) {
		this.requiredDays = requiredDays;
	}
}
