package malecluk.garminparser.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.processing.results.WalkingAvgPaceTimeResult;

public class WalkingAvgPaceTimeCounterTest {

	private static final String NAME = "September walking";

	private static final Instant START_DATE = Instant.parse("2026-09-01T00:00:00Z");

	private static final Instant END_DATE = Instant.parse("2026-10-01T00:00:00Z");

	private static final Duration REQUIRED_DURATION = Duration.ofHours(3);

	private static final Float MAX_AVERAGE_PACE = 10.5f;

	private WalkingAvgPaceTimeCounter counter;

	@BeforeEach
	void setUp() {
		counter = new WalkingAvgPaceTimeCounter(NAME, REQUIRED_DURATION, MAX_AVERAGE_PACE, START_DATE, END_DATE);
	}

	@Test
	void shouldCountWalkingActivityWithinDateRangeWithAcceptablePace() {
		Walking activity = createWalking("2026-09-15T12:00:00Z", 10.0f, Duration.ofMinutes(45));

		counter.onActivity(activity);

		WalkingAvgPaceTimeResult result = getResult();

		assertEquals(Duration.ofMinutes(45), result.countedDuration());
		assertFalse(result.isCompleted());
	}

	@Test
	void shouldNotCountNonWalkingActivity() {
		Walking activity = createWalking("2026-09-15T12:00:00Z", 10.0f, Duration.ofMinutes(45));

		activity.setSport(Sport.RUNNING);

		counter.onActivity(activity);

		assertEquals(Duration.ZERO, getResult().countedDuration());
	}

	@Test
	void shouldNotCountWalkingActivityWithPaceAboveMaximum() {
		Walking activity = createWalking("2026-09-15T12:00:00Z", 11.0f, Duration.ofMinutes(45));

		counter.onActivity(activity);

		assertEquals(Duration.ZERO, getResult().countedDuration());
	}

	@Test
	void shouldNotCountWalkingActivityWithPaceEqualToMaximum() {
		Walking activity = createWalking("2026-09-15T12:00:00Z", MAX_AVERAGE_PACE, Duration.ofMinutes(45));

		counter.onActivity(activity);

		assertEquals(Duration.ZERO, getResult().countedDuration());
	}

	@Test
	void shouldNotCountActivityBeforeStartDate() {
		Walking activity = createWalking("2026-08-31T23:59:59Z", 10.0f, Duration.ofMinutes(45));

		counter.onActivity(activity);

		assertEquals(Duration.ZERO, getResult().countedDuration());
	}

	@Test
	void shouldNotCountActivityAfterEndDate() {
		Walking activity = createWalking("2026-10-01T00:00:01Z", 10.0f, Duration.ofMinutes(45));

		counter.onActivity(activity);

		assertEquals(Duration.ZERO, getResult().countedDuration());
	}

	@Test
	void shouldNotCountActivityAtStartDate() {
		Walking activity = createWalking(START_DATE.toString(), 10.0f, Duration.ofMinutes(45));

		counter.onActivity(activity);

		assertEquals(Duration.ZERO, getResult().countedDuration());
	}

	@Test
	void shouldNotCountActivityAtEndDate() {
		Walking activity = createWalking(END_DATE.toString(), 10.0f, Duration.ofMinutes(45));

		counter.onActivity(activity);

		assertEquals(Duration.ZERO, getResult().countedDuration());
	}

	@Test
	void shouldAccumulateDurationFromMultipleActivities() {
		counter.onActivity(createWalking("2026-09-05T10:00:00Z", 10.0f, Duration.ofMinutes(45)));

		counter.onActivity(createWalking("2026-09-10T10:00:00Z", 9.5f, Duration.ofMinutes(30)));

		counter.onActivity(createWalking("2026-09-15T10:00:00Z", 12.0f, Duration.ofMinutes(60)));

		assertEquals(Duration.ofMinutes(75), getResult().countedDuration());
	}

	@Test
	void shouldReportNotCompletedWhenRequiredDurationIsNotReached() {
		counter.onActivity(createWalking("2026-09-15T10:00:00Z", 10.0f, Duration.ofHours(2)));

		assertFalse(getResult().isCompleted());
	}

	@Test
	void shouldReportCompletedWhenRequiredDurationIsReached() {
		counter.onActivity(createWalking("2026-09-15T10:00:00Z", 10.0f, Duration.ofHours(3)));

		assertTrue(getResult().isCompleted());
	}

	@Test
	void shouldReportCompletedWhenCountedDurationExceedsRequiredDuration() {
		counter.onActivity(createWalking("2026-09-15T10:00:00Z", 10.0f, Duration.ofHours(4)));

		assertTrue(getResult().isCompleted());
	}

	@Test
	void shouldReturnConfiguredValuesInResult() {
		WalkingAvgPaceTimeResult result = getResult();

		assertEquals(NAME, result.name());
		assertEquals(Duration.ZERO, result.countedDuration());
		assertEquals(REQUIRED_DURATION, result.requiredDuration());
		assertEquals(MAX_AVERAGE_PACE, result.maxAveragePace());
		assertFalse(result.isCompleted());
	}

	private WalkingAvgPaceTimeResult getResult() {
		return (WalkingAvgPaceTimeResult) counter.getResult();
	}

	private Walking createWalking(String startTime, float averagePace, Duration totalTimerTime) {

		Walking walking = new Walking();

		walking.setSport(Sport.WALKING);
		walking.setStartTime(Instant.parse(startTime));
		walking.setEnhancedAvgSpeed(speedForPace(averagePace));
		walking.setTotalTimerTime(totalTimerTime);

		return walking;
	}

	/**
	 * Converts average pace in minutes per kilometer to speed in meters per second.
	 *
	 * @param pace average pace in minutes per kilometer
	 * @return speed in meters per second
	 */
	private float speedForPace(float pace) {
		return 1000.0f / (pace * 60.0f);
	}

}
