package malecluk.garminparser.processing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "activity-processing")
public class ActivityProcessingProperties {

	private List<WalkingAvgPaceTimeProperties> walkingAvgPaceTime = new ArrayList<>();

    private List<MeditationDaysProperties> meditationDays = new ArrayList<>();
    
    private List<WalkingInHRZonesTimeProperties> walkingHRZones = new ArrayList<>();

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
}
