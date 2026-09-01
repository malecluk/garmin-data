package malecluk.garminparser.processing;

import java.time.Duration;
import java.time.LocalDateTime;

public class WalkingAvgPaceTimeProperties {
	
	private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Duration requiredDuration;
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
