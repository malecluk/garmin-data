package malecluk.garminparser.processing.results;

import java.time.Duration;

import org.springframework.stereotype.Component;

import malecluk.garminparser.utils.DateTimeConverterHelper;

/**
 * Prints activity analyzer results to the application console.
 *
 * <p>Each supported result type has its own human-readable console representation.</p>
 */
@Component
public class ActivityResultConsolePrinter {
	
	/**
	 * Prints all activity analyzer results contained in the processing result.
	 *
	 * @param processingResult aggregated results produced by the configured activity analyzers
	 * @throws IllegalArgumentException if the result contains an unsupported result type
	 */
	public void print(ActivityProcessingResult processingResult) {

        for (ActivityListenerResult result : processingResult.results()) {
            printResult(result);
        }
    }
	
	private void printResult(ActivityListenerResult result) {

        switch (result) { // TODO null check

            case WalkingAvgPaceTimeResult r -> printWalking(r);

            case MeditationDaysResult r -> printMeditation(r);
            
            case WalkingInHRZonesTimeResults r -> printWalkingInZones(r);

            default -> throw new IllegalArgumentException( "Unknown result: " + result.getClass()); // TODO handle exception in upper class, do not fail application
        }
    }
	
	private void printWalking(WalkingAvgPaceTimeResult r) {

	    System.out.println();
	    System.out.println(r.name() + ":");
	    
	    System.out.println("Already walked: " + DateTimeConverterHelper.formatSeconds(r.countedDuration()));

	    if (r.isCompleted()) {
	        System.out.println("Fast walking completed!");
	    } 
	    else {
	    	Duration remaining = r.requiredDuration().minus(r.countedDuration());

		    if (remaining.isNegative()) {
		        remaining = Duration.ZERO;
		    }
	        System.out.println("Remaining: " + DateTimeConverterHelper.formatSeconds(remaining));
	        System.out.println("Keep walking!!!");
	    }
	}
	
	private void printMeditation(MeditationDaysResult r) {

	    System.out.println();
	    System.out.println(r.name() + ":");

	    System.out.println("Days already meditated: " + r.meditatedDays());

	    if (r.isCompleted()) {
	        System.out.println("Meditation badge completed.");
	    } 
	    else {
	    	
	    	int remaining = Math.max(0, r.requiredDays() - r.meditatedDays());
	    	
	        System.out.println("Remaining: " + remaining + " days");
	        System.out.println("Keep meditating!!!");
	    }
	}
	
	private void printWalkingInZones(WalkingInHRZonesTimeResults res) {
		 System.out.println();
		 System.out.println(res.name() + ":");
		 
		 System.out.println("Already walked: " + DateTimeConverterHelper.formatSeconds(res.countedDuration()));
		 
		 if (res.isCompleted()) {
			 System.out.println("Walking in zones completed!");
		 }
		 else {
			 Duration remaining = res.requiredDuration().minus(res.countedDuration());
			 
			 if (remaining.isNegative()) {
				 remaining = Duration.ZERO;
			 }
			 System.out.println("Remaining: " + DateTimeConverterHelper.formatSeconds(remaining));
		     System.out.println("Keep walking in zones!!!");
			 
		 }
	}
}
