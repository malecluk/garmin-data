package malecluk.garminparser.processing.results;

import java.time.Clock;
import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

import org.springframework.stereotype.Component;

import malecluk.garminparser.utils.DateTimeConverterHelper;

/**
 * Prints activity analyzer results to the application console.
 *
 * <p>
 * Each supported result type has its own human-readable console representation.
 * </p>
 */
@Component
public class ActivityResultConsolePrinter {
	
	private final Clock clock;
	
	//-----------------------
	
	/**
	 * Creates a printer using the system default clock.
	 */
	public ActivityResultConsolePrinter() {
	    this(Clock.systemDefaultZone());
	}

	/**
	 * Creates a printer using the supplied clock.
	 * @param clock clock used to determine whether an analyzer period has ended
	 */
	ActivityResultConsolePrinter(Clock clock) {
	    this.clock = Objects.requireNonNull(clock, "clock must not be null");
	}
	
	//-----------------------
	
	/**
	 * Prints analyzer results ordered by the start of their configured periods.
	 *
	 * <p>
	 * Results belonging to the same period are kept in their original analyzer
	 * registration order.
	 * </p>
	 *
	 * @param processingResult results to print
	 */
	public void print(ActivityProcessingResult processingResult) {
		processingResult.results().stream().sorted(Comparator.comparing(ActivityListenerResult::startDate))
				.forEach(this::printResult);
	}

	private void printResult(ActivityListenerResult result) {

		switch (result) { // TODO null check

		case WalkingAvgPaceTimeResult r -> printWalking(r);

		case MeditationDaysResult r -> printMeditation(r);

		case WalkingInHRZonesTimeResults r -> printWalkingInZones(r);

		default -> throw new IllegalArgumentException("Unknown result: " + result.getClass()); // TODO handle exception
																								// in upper class, do
																								// not fail application
		}
	}

	private void printWalking(WalkingAvgPaceTimeResult r) {

		System.out.println();
		System.out.println(walkingTCS(r));
	}

	String walkingTCS(WalkingAvgPaceTimeResult r) {
		StringBuilder sb = new StringBuilder();

		sb.append(r.name()).append(":").append("\n");
		sb.append(periodTCS(r)).append("\n");
		sb.append("Already walked: ").append(DateTimeConverterHelper.formatSeconds(r.countedDuration())).append("\n");

		if (r.isCompleted()) {
			sb.append("Fast walking completed!").append("\n");
		} else if (isPeriodOver(r)) {
		    sb.append("Criteria not met.\n");
		} else {
			Duration remaining = r.requiredDuration().minus(r.countedDuration());

			if (remaining.isNegative()) {
				remaining = Duration.ZERO;
			}
			sb.append("Remaining: ").append(DateTimeConverterHelper.formatSeconds(remaining)).append("\n");
			sb.append("Keep walking!!!").append("\n");
		}
		return sb.toString();
	}

	private void printMeditation(MeditationDaysResult r) {

		System.out.println();
		System.out.println(meditationTCS(r));
	}

	String meditationTCS(MeditationDaysResult r) {
		StringBuilder sb = new StringBuilder();

		sb.append(r.name()).append(":").append("\n");
		sb.append(periodTCS(r)).append("\n");
		sb.append("Days already meditated: ").append(r.meditatedDays()).append("\n");

		if (r.isCompleted()) {
			sb.append("Meditation badge completed.").append("\n");
		} else if (isPeriodOver(r)) {
		    sb.append("Criteria not met.\n");
		} else {
			int remaining = Math.max(0, r.requiredDays() - r.meditatedDays());

			sb.append("Remaining: " + remaining + " days").append("\n");
			sb.append("Keep meditating!!!").append("\n");
		}

		return sb.toString();
	}

	private void printWalkingInZones(WalkingInHRZonesTimeResults res) {
		System.out.println();
		System.out.println(walkingInZonesTCS(res));
	}

	String walkingInZonesTCS(WalkingInHRZonesTimeResults res) {
		StringBuilder sb = new StringBuilder();

		sb.append(res.name()).append(":").append("\n");
		sb.append(periodTCS(res)).append("\n");
		sb.append("Already walked: ").append(DateTimeConverterHelper.formatSeconds(res.countedDuration())).append("\n");

		if (res.isCompleted()) {
			sb.append("Walking in zones completed!").append("\n");
		} else if (isPeriodOver(res)) {
		    sb.append("Criteria not met.\n");
		} else {
			Duration remaining = res.requiredDuration().minus(res.countedDuration());

			if (remaining.isNegative()) {
				remaining = Duration.ZERO;
			}
			sb.append("Remaining: ").append(DateTimeConverterHelper.formatSeconds(remaining)).append("\n");
			sb.append("Keep walking in zones!!!").append("\n");
		}

		return sb.toString();
	}

	/**
	 * Formats the configured analyzer period for console output.
	 * @param result analyzer result containing the period
	 * @return formatted period
	 */
	private String periodTCS(ActivityListenerResult result) {
		return "Period: " + DateTimeConverterHelper.formatDate(result.startDate()) + " - "
				+ DateTimeConverterHelper.formatDate(result.endDate());
	}
	
	private boolean isPeriodOver(ActivityListenerResult result) {
	    return clock.instant().isAfter(result.endDate());
	}
}
