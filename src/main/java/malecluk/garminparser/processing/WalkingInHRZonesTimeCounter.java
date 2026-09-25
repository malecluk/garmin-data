package malecluk.garminparser.processing;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.component.HeartRateZone;
import malecluk.garminparser.model.component.HeartRateZones;
import malecluk.garminparser.processing.results.ActivityListenerResult;
import malecluk.garminparser.processing.results.WalkingInHRZonesTimeResults;
import malecluk.garminparser.utils.DateTimeConverterHelper;

/**
 * Counts time spent in configured heart-rate zones during walking activities.
 *
 * <p>Only walking activities whose start time falls within the configured date
 * range are considered. For each required zone number that is present in the
 * activity, its recorded duration is added to the accumulated duration.</p>
 *
 * <p>Zone numbers refer to the actual zone numbers stored in
 * {@link HeartRateZone#number()}, not to positions in the zone list.</p>
 */
public class WalkingInHRZonesTimeCounter  implements ActivityAnalyzer {

	private static final Logger log = LogManager.getLogger(WalkingInHRZonesTimeCounter.class);
	
	private final String name;
	
	/**
	 * Amount of qualifying time spent in the configured heart-rate zones required to complete the analyzer.
	 */
    private final Duration requiredDuration;
    private final Instant startDate;
    private final Instant endDate;
    
    /**
     * Actual heart-rate zone numbers whose recorded durations are included in the count.
     *
     * <p>Zone numbers are matched against {@link HeartRateZone#number()} and are
     * not interpreted as positions in the zone list.</p>
     */
    private final List<Integer> requiredZones;
	
    private Duration countedDuration = Duration.ZERO;
    
    /**
     * Creates a walking heart-rate-zone analyzer.
     *
     * @param name analyzer name used in the result and console output
     * @param requiredDuration amount of qualifying time spent in the configured heart-rate zones required to complete the analyzer
     * @param startDate start of the date and time range in which walking activities are counted
     * @param endDate end of the date and time range in which walking activities are counted
     * @param requiredZones actual heart-rate zone numbers whose recorded durations are included in the count
     */
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
					
					if (zones.hasZone(zoneNum)) {
						HeartRateZone zone = zones.get(zoneNum);

						log.debug("Adding " + DateTimeConverterHelper.formatSeconds(zone.time()) + " from zone " + zoneNum);
						this.countedDuration = this.countedDuration.plus(zone.time());
					}
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
