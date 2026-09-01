package malecluk.garminparser.processing;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Creates activity analyzers from the configured activity-processing properties.
 *
 * <p>Each configured analyzer entry results in a separate analyzer instance.
 * This allows multiple analyzers of the same type to run simultaneously with
 * different dates, thresholds, or other parameters.</p>
 */
@Component
public class ActivityAnalyzerFactory {

	/**
	 * Creates analyzers according to the supplied configuration.
	 *
	 * @param properties activity-processing configuration
	 * @return configured analyzer instances
	 */
	public List<ActivityAnalyzer> createAnalyzers(ActivityProcessingProperties properties) {
		
		List<ActivityAnalyzer> analyzers = new ArrayList<>();
		
		for (WalkingAvgPaceTimeProperties config : properties.getWalkingAvgPaceTime()) {

            WalkingAvgPaceTimeCounter analyzer =
            		new WalkingAvgPaceTimeCounter(
            		        config.getName(),
            		        config.getRequiredDuration(),
            		        config.getMaxAveragePace(),
            		        config.getStartDate().atZone(ZoneId.systemDefault()).toInstant(),
            		        config.getEndDate().atZone(ZoneId.systemDefault()).toInstant()
            		);
            
            analyzers.add(analyzer);
        }
		
		for (MeditationDaysProperties  config : properties.getMeditationDays()) {
			MeditationDaysCounter analyzer = 
					new MeditationDaysCounter(
							config.getName(),
							config.getStartDate().atZone(ZoneId.systemDefault()).toInstant(),
            		        config.getEndDate().atZone(ZoneId.systemDefault()).toInstant(), 
            		        config.getRequiredDays()
            		);
			
			analyzers.add(analyzer);
		}
		
		for (WalkingInHRZonesTimeProperties config : properties.getWalkingHRZones()) {
			WalkingInHRZonesTimeCounter analyzer =
					new WalkingInHRZonesTimeCounter(
							config.getName(),
							config.getRequiredDuration(),
							config.getStartDate().atZone(ZoneId.systemDefault()).toInstant(),
							config.getEndDate().atZone(ZoneId.systemDefault()).toInstant(),
							config.getRequiredZones()
					);
			
			analyzers.add(analyzer);
		}
		
		return analyzers;
	}
}
