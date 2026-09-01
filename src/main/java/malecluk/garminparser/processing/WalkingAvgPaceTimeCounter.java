package malecluk.garminparser.processing;

import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.processing.results.ActivityListenerResult;
import malecluk.garminparser.processing.results.WalkingAvgPaceTimeResult;
import malecluk.garminparser.utils.DateTimeConverterHelper;

public class WalkingAvgPaceTimeCounter implements ActivityAnalyzer {
	
	private static final Logger log = LogManager.getLogger(WalkingAvgPaceTimeCounter.class);
	
	private final String name;
	
	/**
	 * Number of seconds required to fulfill this task
	 */
    private final Duration requiredDuration;
    private final Float maxAveragePace;
    private final Instant startDate;
    private final Instant endDate;
    
    private Duration countedDuration = Duration.ZERO;
    
    public WalkingAvgPaceTimeCounter(
            String name,
            Duration requiredDuration,
            Float maxAveragePace,
            Instant startDate,
            Instant endDate) {

        this.name = name;
        this.requiredDuration = requiredDuration;
        this.maxAveragePace = maxAveragePace;
        this.startDate = startDate;
        this.endDate = endDate;
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
				
				log.debug("Avg. pace: " + w.getAvgPace());
				if (w.getAvgPace() < this.maxAveragePace) {
					log.debug("Adding " + w.getTotalTimerTime().toSeconds() + " seconds");
					this.countedDuration = this.countedDuration.plus(w.getTotalTimerTime());
				}
			}
		}
		else {
			log.debug("Activity is not walking.");
		}
	}
	
	@Override
	public ActivityListenerResult getResult() {

	    return new WalkingAvgPaceTimeResult(
	            this.name,
	            this.countedDuration,
	            this.requiredDuration,
	            this.maxAveragePace,
	            this.countedDuration.compareTo(this.requiredDuration) >= 0
	    );
	}
}
