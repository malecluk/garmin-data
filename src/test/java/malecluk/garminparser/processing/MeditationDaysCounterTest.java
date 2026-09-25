package malecluk.garminparser.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.activities.Meditation;
import malecluk.garminparser.processing.results.MeditationDaysResult;

class MeditationDaysCounterTest {

	private static final String NAME = "September meditation";

	private static final Instant START_DATE = Instant.parse("2026-09-01T00:00:00Z");

	private static final Instant END_DATE = Instant.parse("2026-10-01T00:00:00Z");

	private static final Integer REQUIRED_DAYS = 10;

	private MeditationDaysCounter counter;

	@BeforeEach
	void setUp() {
		counter = new MeditationDaysCounter(NAME, START_DATE, END_DATE, REQUIRED_DAYS);
	}

	@Test
	void shouldCountMeditationWithinDateRange() {
		Meditation activity = createMeditation("2026-09-15T12:00:00Z");

		counter.onActivity(activity);

		assertEquals(Integer.valueOf(1), getResult().meditatedDays());
	}

	@Test
	void shouldNotCountNonMeditationActivity() {
		Meditation activity = createMeditation("2026-09-15T12:00:00Z");

		activity.setSport(Sport.WALKING);

		counter.onActivity(activity);

		assertEquals(Integer.valueOf(0), getResult().meditatedDays());
	}

	@Test
	void shouldNotCountActivityBeforeStartDate() {
		Meditation activity = createMeditation("2026-08-31T23:59:59Z");

		counter.onActivity(activity);

		assertEquals(Integer.valueOf(0), getResult().meditatedDays());
	}

	@Test
	void shouldNotCountActivityAfterEndDate() {
		Meditation activity = createMeditation("2026-10-01T00:00:01Z");

		counter.onActivity(activity);

		assertEquals(Integer.valueOf(0), getResult().meditatedDays());
	}

	@Test
	void shouldNotCountActivityAtStartDate() {
		Meditation activity = createMeditation(START_DATE.toString());

		counter.onActivity(activity);

		assertEquals(Integer.valueOf(0), getResult().meditatedDays());
	}

	@Test
	void shouldNotCountActivityAtEndDate() {
		Meditation activity = createMeditation(END_DATE.toString());

		counter.onActivity(activity);

		assertEquals(Integer.valueOf(0), getResult().meditatedDays());
	}

	@Test
	void shouldCountMultipleMeditationsOnSameDayAsOneDay() {
		counter.onActivity(createMeditation("2026-09-15T08:00:00Z"));

		counter.onActivity(createMeditation("2026-09-15T12:00:00Z"));

		counter.onActivity(createMeditation("2026-09-15T18:00:00Z"));

		assertEquals(Integer.valueOf(1), getResult().meditatedDays());
	}

	@Test
	void shouldCountMeditationsOnDifferentDaysAsDifferentDays() {
		counter.onActivity(createMeditation("2026-09-15T08:00:00Z"));

		counter.onActivity(createMeditation("2026-09-16T08:00:00Z"));

		counter.onActivity(createMeditation("2026-09-17T08:00:00Z"));

		assertEquals(Integer.valueOf(3), getResult().meditatedDays());
	}

	@Test
	public void shouldReportNotCompletedWhenRequiredDaysAreNotReached() {
		counter.onActivity(createMeditation("2026-09-15T08:00:00Z"));

		counter.onActivity(createMeditation("2026-09-16T08:00:00Z"));

		assertFalse(getResult().isCompleted());
	}

	@Test
	void shouldReportCompletedWhenRequiredDaysAreReached() {
		addMeditationsOnDifferentDays(REQUIRED_DAYS);

		assertTrue(getResult().isCompleted());
	}

	@Test
	void shouldReportCompletedWhenCountedDaysExceedRequiredDays() {
		addMeditationsOnDifferentDays(REQUIRED_DAYS + 2);

		MeditationDaysResult result = getResult();

		assertEquals(Integer.valueOf(REQUIRED_DAYS + 2), result.meditatedDays());
		assertTrue(result.isCompleted());
	}

	@Test
	void shouldReturnConfiguredValuesInResult() {
		MeditationDaysResult result = getResult();

		assertEquals(NAME, result.name());
		assertEquals(Integer.valueOf(0), result.meditatedDays());
		assertEquals(REQUIRED_DAYS, result.requiredDays());
		assertFalse(result.isCompleted());
	}

	private MeditationDaysResult getResult() {
		return (MeditationDaysResult) counter.getResult();
	}

	private Meditation createMeditation(String startTime) {
		Meditation meditation = new Meditation();

		meditation.setSport(Sport.MEDITATION);
		meditation.setStartTime(Instant.parse(startTime));

		return meditation;
	}

	private void addMeditationsOnDifferentDays(int numberOfDays) {
		for (int i = 1; i <= numberOfDays; i++) {
			counter.onActivity(createMeditation("2026-09-" + String.format("%02d", i) + "T12:00:00Z"));
		}
	}

}