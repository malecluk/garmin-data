package malecluk.garminparser.processing.results;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivityResultConsolePrinterTest {

	private static final Instant START_DATE = Instant.parse("2026-10-01T00:00:00Z");
	private static final Instant END_DATE = Instant.parse("2026-10-31T23:59:59Z");
	private static final Instant NOW = Instant.parse("2026-10-15T12:00:00Z");

	private ActivityResultConsolePrinter printer;

	private ByteArrayOutputStream output;
	private PrintStream originalOut;

	@BeforeEach
	void setUp() {
		printer = new ActivityResultConsolePrinter(Clock.fixed(NOW, ZoneOffset.UTC));

		originalOut = System.out;
		output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));
	}

	@AfterEach
	void tearDown() {
		System.setOut(originalOut);
	}

	@Test
	void walkingTCS_completed_returnsCompletedMessage() {
		WalkingAvgPaceTimeResult result = new WalkingAvgPaceTimeResult("Fast walking", START_DATE, END_DATE,
				Duration.ofMinutes(35), Duration.ofMinutes(30), 5.5f, true);

		String actual = printer.walkingTCS(result);

		assertEquals("""
				Fast walking:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Already walked: 00:35:00
				Fast walking completed!
				""", actual);
	}

	@Test
	void walkingTCS_notCompleted_returnsRemainingTime() {
		WalkingAvgPaceTimeResult result = new WalkingAvgPaceTimeResult("Fast walking", START_DATE, END_DATE,
				Duration.ofMinutes(20), Duration.ofMinutes(30), 5.5f, false);

		String actual = printer.walkingTCS(result);

		assertEquals("""
				Fast walking:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Already walked: 00:20:00
				Remaining: 00:10:00
				Keep walking!!!
				""", actual);
	}

	@Test
	void walkingTCS_countedDurationExceedsRequiredDuration_returnsZeroRemaining() {
		WalkingAvgPaceTimeResult result = new WalkingAvgPaceTimeResult("Fast walking", START_DATE, END_DATE,
				Duration.ofMinutes(40), Duration.ofMinutes(30), 5.5f, false);

		String actual = printer.walkingTCS(result);

		assertEquals("""
				Fast walking:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Already walked: 00:40:00
				Remaining: 00:00:00
				Keep walking!!!
				""", actual);
	}

	@Test
	void walkingTCS_periodOverAndNotCompleted_returnsCriteriaNotMet() {
		WalkingAvgPaceTimeResult result = new WalkingAvgPaceTimeResult("Fast walking",
				Instant.parse("2026-09-01T00:00:00Z"), Instant.parse("2026-09-30T23:59:59Z"), Duration.ofMinutes(20),
				Duration.ofMinutes(30), 5.5f, false);

		String actual = printer.walkingTCS(result);

		assertTrue(actual.contains("Criteria not met."));
		assertFalse(actual.contains("Remaining:"));
		assertFalse(actual.contains("Keep walking!!!"));
	}

	@Test
	void walkingTCS_atPeriodEnd_returnsRemainingTime() {
		Instant endDate = Instant.parse("2026-10-31T23:59:59Z");

		printer = new ActivityResultConsolePrinter(Clock.fixed(endDate, ZoneOffset.UTC));

		WalkingAvgPaceTimeResult result = new WalkingAvgPaceTimeResult("Fast walking", START_DATE, endDate,
				Duration.ofMinutes(20), Duration.ofMinutes(30), 5.5f, false);

		String actual = printer.walkingTCS(result);

		assertTrue(actual.contains("Remaining: 00:10:00"));
		assertTrue(actual.contains("Keep walking!!!"));
	}

	@Test
	void meditationTCS_completed_returnsCompletedMessage() {
		MeditationDaysResult result = new MeditationDaysResult("Meditation", START_DATE, END_DATE, 30, 30, true);

		String actual = printer.meditationTCS(result);

		assertEquals("""
				Meditation:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Days already meditated: 30
				Meditation badge completed.
				""", actual);
	}

	@Test
	void meditationTCS_notCompleted_returnsRemainingDays() {
		MeditationDaysResult result = new MeditationDaysResult("Meditation", START_DATE, END_DATE, 12, 30, false);

		String actual = printer.meditationTCS(result);

		assertEquals("""
				Meditation:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Days already meditated: 12
				Remaining: 18 days
				Keep meditating!!!
				""", actual);
	}

	@Test
	void meditationTCS_meditatedDaysExceedRequiredDays_returnsZeroRemaining() {
		MeditationDaysResult result = new MeditationDaysResult("Meditation", START_DATE, END_DATE, 35, 30, false);

		String actual = printer.meditationTCS(result);

		assertEquals("""
				Meditation:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Days already meditated: 35
				Remaining: 0 days
				Keep meditating!!!
				""", actual);
	}

	@Test
	void meditationTCS_periodOverAndNotCompleted_returnsCriteriaNotMet() {
		MeditationDaysResult result = new MeditationDaysResult("Meditation", Instant.parse("2026-09-01T00:00:00Z"),
				Instant.parse("2026-09-30T23:59:59Z"), 12, 30, false);

		String actual = printer.meditationTCS(result);

		assertTrue(actual.contains("Criteria not met."));
		assertFalse(actual.contains("Remaining:"));
		assertFalse(actual.contains("Keep meditating!!!"));
	}

	@Test
	void walkingInZonesTCS_completed_returnsCompletedMessage() {
		WalkingInHRZonesTimeResults result = new WalkingInHRZonesTimeResults("Walking in HR zones", START_DATE,
				END_DATE, Duration.ofMinutes(45), Duration.ofMinutes(30), true);

		String actual = printer.walkingInZonesTCS(result);

		assertEquals("""
				Walking in HR zones:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Already walked: 00:45:00
				Walking in zones completed!
				""", actual);
	}

	@Test
	void walkingInZonesTCS_notCompleted_returnsRemainingTime() {
		WalkingInHRZonesTimeResults result = new WalkingInHRZonesTimeResults("Walking in HR zones", START_DATE,
				END_DATE, Duration.ofMinutes(20), Duration.ofMinutes(30), false);

		String actual = printer.walkingInZonesTCS(result);

		assertEquals("""
				Walking in HR zones:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Already walked: 00:20:00
				Remaining: 00:10:00
				Keep walking in zones!!!
				""", actual);
	}

	@Test
	void walkingInZonesTCS_countedDurationExceedsRequiredDuration_returnsZeroRemaining() {
		WalkingInHRZonesTimeResults result = new WalkingInHRZonesTimeResults("Walking in HR zones", START_DATE,
				END_DATE, Duration.ofMinutes(40), Duration.ofMinutes(30), false);

		String actual = printer.walkingInZonesTCS(result);

		assertEquals("""
				Walking in HR zones:
				Period: 01.10.2026 02:00:00 - 01.11.2026 00:59:59
				Already walked: 00:40:00
				Remaining: 00:00:00
				Keep walking in zones!!!
				""", actual);
	}

	@Test
	void walkingInZonesTCS_periodOverAndNotCompleted_returnsCriteriaNotMet() {
		WalkingInHRZonesTimeResults result = new WalkingInHRZonesTimeResults("Walking in HR zones",
				Instant.parse("2026-09-01T00:00:00Z"), Instant.parse("2026-09-30T23:59:59Z"), Duration.ofMinutes(20),
				Duration.ofMinutes(30), false);

		String actual = printer.walkingInZonesTCS(result);

		assertTrue(actual.contains("Criteria not met."));
		assertFalse(actual.contains("Remaining:"));
		assertFalse(actual.contains("Keep walking in zones!!!"));
	}

	@Test
	void print_withMultipleResults_printsAllResults() {
		WalkingAvgPaceTimeResult walking = new WalkingAvgPaceTimeResult("Fast walking", START_DATE, END_DATE,
				Duration.ofMinutes(20), Duration.ofMinutes(30), 5.5f, false);

		MeditationDaysResult meditation = new MeditationDaysResult("Meditation", START_DATE, END_DATE, 30, 30, true);

		WalkingInHRZonesTimeResults zones = new WalkingInHRZonesTimeResults("Walking in HR zones", START_DATE, END_DATE,
				Duration.ofMinutes(45), Duration.ofMinutes(30), true);

		ActivityProcessingResult processingResult = new ActivityProcessingResult(List.of(walking, meditation, zones));

		printer.print(processingResult);

		String actual = output.toString();

		assertTrue(actual.contains("Fast walking:"));
		assertTrue(actual.contains("Meditation:"));
		assertTrue(actual.contains("Walking in HR zones:"));

		assertTrue(actual.indexOf("Fast walking:") < actual.indexOf("Meditation:"));
		assertTrue(actual.indexOf("Meditation:") < actual.indexOf("Walking in HR zones:"));
	}

	@Test
	void print_withResultsFromDifferentPeriods_printsResultsOrderedByStartDate() {
		Instant septemberStart = Instant.parse("2026-09-01T00:00:00Z");
		Instant septemberEnd = Instant.parse("2026-09-30T23:59:59Z");
		Instant octoberStart = Instant.parse("2026-10-01T00:00:00Z");
		Instant octoberEnd = Instant.parse("2026-10-31T23:59:59Z");

		WalkingInHRZonesTimeResults octoberZones = new WalkingInHRZonesTimeResults("October zones", octoberStart,
				octoberEnd, Duration.ZERO, Duration.ofHours(3), false);

		MeditationDaysResult septemberMeditation = new MeditationDaysResult("September meditation", septemberStart,
				septemberEnd, 5, 10, false);

		WalkingAvgPaceTimeResult octoberWalking = new WalkingAvgPaceTimeResult("October walking", octoberStart,
				octoberEnd, Duration.ZERO, Duration.ofHours(3), 5.5f, false);

		ActivityProcessingResult processingResult = new ActivityProcessingResult(
				List.of(octoberZones, octoberWalking, septemberMeditation));

		printer.print(processingResult);

		String actual = output.toString();

		assertTrue(actual.indexOf("September meditation:") < actual.indexOf("October zones:"));

		assertTrue(actual.indexOf("October zones:") < actual.indexOf("October walking:"));
	}

	@Test
	void print_withUnknownResultType_throwsIllegalArgumentException() {
		ActivityListenerResult unknownResult = mock(ActivityListenerResult.class);

		ActivityProcessingResult processingResult = new ActivityProcessingResult(List.of(unknownResult));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> printer.print(processingResult));

		assertEquals("Unknown result: " + unknownResult.getClass(), exception.getMessage());
	}
}
