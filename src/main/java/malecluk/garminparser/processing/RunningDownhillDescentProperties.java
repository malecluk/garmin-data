package malecluk.garminparser.processing;

import java.time.LocalDateTime;

import malecluk.garminparser.model.value.Distance;

/**
 * Configuration of a running-descent analyzer.
 *
 * <p>The analyzer counts total descent distance during running within the configured date range.</p>
 */
public class RunningDownhillDescentProperties {

	/**
	 * Name of the analyzer used in its result and console output.
	 */
	private String name;
	
	/**
	 * Start of the date and time range in which running activities are counted.
	 */
    private LocalDateTime startDate;
    
    /**
     * End of the date and time range in which running activities are counted.
     */
    private LocalDateTime endDate;
    
    /**
     * Number of descended meters required to complete the analyzer.
     */
    private Distance requiredDescent;
    
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

	/**
	 * Returns the descent distance required to complete the analyzer.
	 *
	 * @return required descent distance
	 */
	public Distance getRequiredDescent() {
		return requiredDescent;
	}

	/**
	 * Sets the descent distance required to complete the analyzer.
	 *
	 * @param requiredDescent required descent distance
	 */
	public void setRequiredDescent(Distance requiredDescent) {
		this.requiredDescent = requiredDescent;
	}
}
