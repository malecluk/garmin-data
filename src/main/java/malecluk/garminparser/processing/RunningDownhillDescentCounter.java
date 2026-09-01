package malecluk.garminparser.processing;

import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Running;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.processing.results.ActivityListenerResult;
import malecluk.garminparser.processing.results.RunningDownhillDescentResult;
import malecluk.garminparser.utils.DateTimeConverterHelper;

/**
 * Counts total descent distance from running activities within a configured
 * date range.
 *
 * <p>
 * An activity is counted when its start time is strictly after the analyzer
 * start date and strictly before the analyzer end date.
 * </p>
 */
public class RunningDownhillDescentCounter implements ActivityAnalyzer {

	private static final Logger log = LogManager.getLogger(RunningDownhillDescentCounter.class);

	/**
	 * Name of the analyzer.
	 */
	private final String name;

	/**
	 * Descent distance required to complete the analyzer.
	 */
	private final Distance requiredDescent;

	/**
	 * Start of the period in which activities are counted.
	 */
	private final Instant startDate;

	/**
	 * End of the period in which activities are counted.
	 */
	private final Instant endDate;

	/**
	 * Total descent distance counted so far.
	 */
	private Distance countedDescent = Distance.ZERO;

	/**
	 * Creates a running downhill descent analyzer.
	 *
	 * @param name            analyzer name
	 * @param requiredDescent descent distance required to complete the analyzer
	 * @param startDate       start of the period in which activities are counted
	 * @param endDate         end of the period in which activities are counted
	 */
	public RunningDownhillDescentCounter(String name, Distance requiredDescent, Instant startDate, Instant endDate) {

		this.name = name;
		this.requiredDescent = requiredDescent;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Processes an activity and adds its total descent when the activity is a
	 * qualifying running activity within the configured period.
	 *
	 * @param activity activity to process
	 */
	@Override
	public void onActivity(Activity activity) {

		log.debug("'" + this.name + "' analyzer:");

		// We are only interested in running activities.
		if (activity instanceof Running r) {

			log.debug("Comparing start date: " + DateTimeConverterHelper.formatDate(this.startDate)
					+ " and activity start: " + DateTimeConverterHelper.formatDate(r.getStartTime()));
			log.debug("Comparing end date: " + DateTimeConverterHelper.formatDate(this.endDate)
					+ " and activity start: " + DateTimeConverterHelper.formatDate(r.getStartTime()));
			if ((this.startDate.isBefore(r.getStartTime())) && (this.endDate.isAfter(r.getStartTime()))) {

				// activity date is after badge start date and before badge end date
				log.debug("Activity start date is between badge start and end dates");

				if (r.getElevationData() == null || r.getElevationData().getTotalDescent() == null) {
					log.debug("Running activity has no descent data.");
					return;
				}

				Distance activityDescent = r.getElevationData().getTotalDescent();
				
				if (activityDescent.isNegative()) {
				    log.debug("Ignoring negative descent: " + activityDescent.toMeters());
				    return;
				}
				
				log.debug("Adding descent of " + activityDescent.toMeters() + " of the month");
				this.countedDescent = this.countedDescent.add(activityDescent);
			}

		} else {
			log.debug("Activity is not running.");
		}

	}

	/**
	 * Returns the current result of the analyzer.
	 *
	 * @return current running downhill descent result
	 */
	@Override
	public ActivityListenerResult getResult() {

		return new RunningDownhillDescentResult(this.name, this.startDate, this.endDate, this.countedDescent,
				this.requiredDescent, this.countedDescent.compareTo(this.requiredDescent) >= 0);
	}

}
