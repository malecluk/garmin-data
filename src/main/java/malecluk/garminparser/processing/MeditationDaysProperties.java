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
	
	public Integer getRequiredDays() {
		return requiredDays;
	}
	
	public void setRequiredDays(Integer requiredDays) {
		this.requiredDays = requiredDays;
	}
}
