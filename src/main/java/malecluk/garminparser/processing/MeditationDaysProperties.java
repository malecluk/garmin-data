package malecluk.garminparser.processing;

import java.time.LocalDateTime;

public class MeditationDaysProperties {

	private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int requiredDays;
    
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
	
	public int getRequiredDays() {
		return requiredDays;
	}
	
	public void setRequiredDays(int requiredDays) {
		this.requiredDays = requiredDays;
	}
}
