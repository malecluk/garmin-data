package malecluk.garminparser.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;
import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.activities.Running;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.component.ActivityElevationData;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.processing.results.RunningDownhillDescentResult;

class RunningDownhillDescentCounterTest {

	private static final String NAME = "October running downhill";

	private static final Instant START_DATE = Instant.parse("2026-10-01T00:00:00Z");

	private static final Instant END_DATE = Instant.parse("2026-11-01T00:00:00Z");

	private static final Distance REQUIRED_DESCENT = new Distance(500.0);

	private RunningDownhillDescentCounter counter;

	@BeforeEach
	void setUp() {
		counter = new RunningDownhillDescentCounter(NAME, REQUIRED_DESCENT, START_DATE, END_DATE);
	}

	@Test
	void shouldCountRunningActivityWithinDateRange() {
		Running activity = createRunning("2026-10-15T12:00:00Z", 150.0);

		counter.onActivity(activity);

		RunningDownhillDescentResult result = getResult();

		assertEquals(new Distance(150.0), result.countedDescent());
		assertFalse(result.isCompleted());
	}

	@Test
	void shouldNotCountNonRunningActivity() {
		Walking activity = createWalking("2026-10-15T12:00:00Z", 150.0);

		counter.onActivity(activity);

		assertEquals(Distance.ZERO, getResult().countedDescent());
	}

	@Test
	void shouldNotCountActivityBeforeStartDate() {
		Running activity = createRunning("2026-09-30T23:59:59Z", 150.0);

		counter.onActivity(activity);

		assertEquals(Distance.ZERO, getResult().countedDescent());
	}

	@Test
	void shouldNotCountActivityAtStartDate() {
		Running activity = createRunning(START_DATE.toString(), 150.0);

		counter.onActivity(activity);

		assertEquals(Distance.ZERO, getResult().countedDescent());
	}

	@Test
	void shouldNotCountActivityAtEndDate() {
		Running activity = createRunning(END_DATE.toString(), 150.0);

		counter.onActivity(activity);

		assertEquals(Distance.ZERO, getResult().countedDescent());
	}

	@Test
	void shouldNotCountActivityAfterEndDate() {
		Running activity = createRunning("2026-11-01T00:00:01Z", 150.0);

		counter.onActivity(activity);

		assertEquals(Distance.ZERO, getResult().countedDescent());
	}

	@Test
	void shouldAccumulateDescentFromMultipleActivities() {
		counter.onActivity(createRunning("2026-10-05T10:00:00Z", 150.0));
		counter.onActivity(createRunning("2026-10-10T10:00:00Z", 200.0));
		counter.onActivity(createRunning("2026-10-15T10:00:00Z", 100.0));

		assertEquals(new Distance(450.0), getResult().countedDescent());
	}

	@Test
	void shouldReportCompletedWhenRequiredDescentIsReached() {
		counter.onActivity(createRunning("2026-10-15T10:00:00Z", 500.0));

		assertTrue(getResult().isCompleted());
	}

	@Test
	void shouldReportCompletedWhenCountedDescentExceedsRequiredDescent() {
		counter.onActivity(createRunning("2026-10-15T10:00:00Z", 600.0));

		assertTrue(getResult().isCompleted());
	}

	@Test
	void shouldReportNotCompletedWhenRequiredDescentIsNotReached() {
		counter.onActivity(createRunning("2026-10-15T10:00:00Z", 499.0));

		assertFalse(getResult().isCompleted());
	}

	@Test
	void shouldReturnConfiguredValuesInResult() {
		RunningDownhillDescentResult result = getResult();

		assertEquals(NAME, result.name());
		assertEquals(START_DATE, result.startDate());
		assertEquals(END_DATE, result.endDate());
		assertEquals(Distance.ZERO, result.countedDescent());
		assertEquals(REQUIRED_DESCENT, result.requiredDescent());
		assertFalse(result.isCompleted());
	}
	
	@Test
	void shouldNotCountNegativeDescent() {
	    Running activity = createRunning("2026-10-15T12:00:00Z", -100.0);

	    counter.onActivity(activity);

	    assertEquals(Distance.ZERO, getResult().countedDescent());
	}
	
	@Test
	void shouldCountRunningActivityWithZeroDescent() {
	    Running activity = createRunning("2026-10-15T12:00:00Z", 0.0);

	    counter.onActivity(activity);

	    assertEquals(Distance.ZERO, getResult().countedDescent());
	    assertFalse(getResult().isCompleted());
	}

	@Test
	void shouldNotCountRunningActivityWithoutElevationData() {
		Running activity = createRunningWithoutElevationData("2026-10-15T10:00:00Z");

		counter.onActivity(activity);

		assertEquals(Distance.ZERO, getResult().countedDescent());
		assertFalse(getResult().isCompleted());
	}

	@Test
	void shouldNotCountRunningActivityWithNullTotalDescent() {
		Running activity = createRunningWithNullTotalDescent("2026-10-15T10:00:00Z");

		counter.onActivity(activity);

		assertEquals(Distance.ZERO, getResult().countedDescent());
		assertFalse(getResult().isCompleted());
	}

	private RunningDownhillDescentResult getResult() {
		return (RunningDownhillDescentResult) counter.getResult();
	}

	private Running createRunning(String startTime, double totalDescent) {
		Running running = createRunningWithoutElevationData(startTime);

		ActivityElevationData elevationData = new ActivityElevationData();
		elevationData.setTotalDescent(new Distance(totalDescent));

		running.setElevationData(elevationData);

		return running;
	}

	private Running createRunningWithoutElevationData(String startTime) {
		Running running = new Running(createActivityIds());

		running.setSport(Sport.RUNNING);
		running.setStartTime(Instant.parse(startTime));

		return running;
	}

	private Running createRunningWithNullTotalDescent(String startTime) {
		Running running = createRunningWithoutElevationData(startTime);

		running.setElevationData(new ActivityElevationData());

		return running;
	}

	private Walking createWalking(String startTime, double totalDescent) {
		Walking walking = new Walking(createActivityIds());

		walking.setSport(Sport.WALKING);
		walking.setStartTime(Instant.parse(startTime));

		ActivityElevationData elevationData = new ActivityElevationData();
		elevationData.setTotalDescent(new Distance(totalDescent));

		walking.setElevationData(elevationData);

		return walking;
	}

	private ActivityIds createActivityIds() {
		return new ActivityIds(
				new ActivityId("12345"),
				new SourceFileHash("67890"));
	}

}
