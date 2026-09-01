package malecluk.garminparser.processing;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Meditation;
import malecluk.garminparser.processing.results.ActivityListenerResult;
import malecluk.garminparser.processing.results.MeditationDaysResult;
import malecluk.garminparser.utils.DateTimeConverterHelper;

public class MeditationDaysCounter implements ActivityAnalyzer {
	
	private static final Logger log = LogManager.getLogger(MeditationDaysCounter.class);
	
	// Stores only the days that actually occurred
    private final Map<Integer, Integer> dayCounts = new HashMap<>();
	
	private final String name;
    private final Instant startDate;
    private final Instant endDate;
    
    /**
	 * How many different days are needed
	 */
	private final Integer requiredDays;
	
	public MeditationDaysCounter(
			String name, 
			Instant startDate, 
			Instant endDate, 
			Integer requiredDays) {

		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.requiredDays = requiredDays;
	}

	@Override
	public void onActivity(Activity activity) {
		
		log.debug("'" + this.name + "' analyzer:");
		
		// we are only interested in meditation
		if (activity.getSport() == Sport.MEDITATION) {
			Meditation m = (Meditation) activity;
			
			log.debug("Comparing start date: " + DateTimeConverterHelper.formatDate(this.startDate) + " and activity start: " + DateTimeConverterHelper.formatDate(m.getStartTime()));
			log.debug("Comparing end date: " + DateTimeConverterHelper.formatDate(this.endDate) + " and activity start: " + DateTimeConverterHelper.formatDate(m.getStartTime()));
			if (
					(this.startDate.isBefore(m.getStartTime())) && 
					(this.endDate.isAfter(m.getStartTime()))
					){
				
				// activity date is after badge start date and before badge end date
				log.debug("Activity start date is between badge start and end dates");
				int day = m.getStartTime().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
				
				// merge() is highly optimized: it sets to 1 if absent, or adds 1 if present
				log.debug("Adding yoga activity for day " + day + " of the month");
			    dayCounts.merge(day, 1, Integer::sum);
			}
			
		}
		else {
			log.debug("Activity is not meditation.");
		}
	}
	
	@Override
	public ActivityListenerResult getResult() {
	    return new MeditationDaysResult(
	    		this.name,
	            this.dayCounts.size(),
	            this.requiredDays,
	            this.dayCounts.size() >= this.requiredDays
	    );
	}

}
