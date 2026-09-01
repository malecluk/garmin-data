package malecluk.garminparser.processing;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.component.HeartRateZones;
import malecluk.garminparser.processing.results.ActivityListenerResult;
import malecluk.garminparser.processing.results.WalkingInHRZonesTimeResults;
import malecluk.garminparser.utils.DateTimeConverterHelper;

public class WalkingInHRZonesTimeCounter  implements ActivityAnalyzer {

	private static final Logger log = LogManager.getLogger(WalkingInHRZonesTimeCounter.class);
	
	private final String name;
	
	/**
	 * Number of seconds required to fulfill this task
	 */
    private final Duration requiredDuration;
    private final Instant startDate;
    private final Instant endDate;
    List<Integer> requiredZones;
	
    private Duration countedDuration = Duration.ZERO;
    
	public WalkingInHRZonesTimeCounter(String name, Duration requiredDuration, Instant startDate, Instant endDate,
			List<Integer> requiredZones) {
		super();
		this.name = name;
		this.requiredDuration = requiredDuration;
		this.startDate = startDate;
		this.endDate = endDate;
		this.requiredZones = requiredZones;
	}

	@Override
	public void onActivity(Activity activity) {

		log.debug("'" + this.name + "' analyzer:");
		
		// we are only interested in walking
		if (activity.getSport() == Sport.WALKING) {
			Walking w = (Walking) activity;
			
			log.debug("Comparing start date: " + DateTimeConverterHelper.formatDate(this.startDate) + " and activity start: " + DateTimeConverterHelper.formatDate(w.getStartTime()));
			log.debug("Comparing end date: " + DateTimeConverterHelper.formatDate(this.endDate) + " and activity start: " + DateTimeConverterHelper.formatDate(w.getStartTime()));
			if (
					(this.startDate.isBefore(w.getStartTime())) && 
					(this.endDate.isAfter(w.getStartTime()))
					){
				
				// activity date is after badge start date and before badge end date
				log.debug("Activity start date is between badge start and end dates");
				
				HeartRateZones zones = w.getHeartRateZones();
				
				for (Integer zoneNum : requiredZones) {
					//log.debug("Zone num: " + zoneNum + " time: " + DateTimeConverterHelper.formatSeconds(zones.get(zoneNum).time()));
					log.debug("Adding " + DateTimeConverterHelper.formatSeconds(zones.get(zoneNum).time()) + " from zone " + zoneNum);
					this.countedDuration = this.countedDuration.plus(zones.get(zoneNum).time());
				}
			}
		}
		else {
			log.debug("Activity is not walking.");
		}
	}

	@Override
	public ActivityListenerResult getResult() {
		
		return new WalkingInHRZonesTimeResults(
				this.name, 
				this.countedDuration, 
				this.requiredDuration, 
				this.countedDuration.compareTo(this.requiredDuration) >= 0
		);
	}
	
	

}
