package malecluk.garminparser.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.component.HeartRateZone;
import malecluk.garminparser.model.component.HeartRateZones;
import malecluk.garminparser.processing.results.WalkingInHRZonesTimeResults;

public class WalkingInHRZonesTimeCounterTest {

	private static final Instant START = Instant.parse("2026-09-01T00:00:00Z");
	private static final Instant END = Instant.parse("2026-09-30T23:59:59Z");

	@Test
	void shouldCountTimeFromRequiredZones() {
		WalkingInHRZonesTimeCounter counter = new WalkingInHRZonesTimeCounter("September walking", Duration.ofHours(3),
				START, END, List.of(2, 3));

		Walking walking = createWalking(Instant.parse("2026-09-15T10:00:00Z"),
				List.of(createZone(0, Duration.ofSeconds(100)), createZone(1, Duration.ofSeconds(200)),
						createZone(2, Duration.ofSeconds(3600)), createZone(3, Duration.ofSeconds(180))));

		counter.onActivity(walking);

		WalkingInHRZonesTimeResults result = (WalkingInHRZonesTimeResults) counter.getResult();

		assertEquals(Duration.ofSeconds(3780), result.countedDuration());
		assertEquals(Duration.ofHours(3), result.requiredDuration());
		assertFalse(result.isCompleted());
		assertEquals("September walking", result.name());
	}

	@Test
	void shouldCompleteWhenRequiredDurationIsReached() {
		WalkingInHRZonesTimeCounter counter = new WalkingInHRZonesTimeCounter("September walking", Duration.ofHours(1),
				START, END, List.of(2, 3));

		Walking walking = createWalking(Instant.parse("2026-09-15T10:00:00Z"),
				List.of(createZone(0, Duration.ZERO), createZone(1, Duration.ZERO),
						createZone(2, Duration.ofMinutes(40)), createZone(3, Duration.ofMinutes(20))));

		counter.onActivity(walking);

		WalkingInHRZonesTimeResults result = (WalkingInHRZonesTimeResults) counter.getResult();

		assertEquals(Duration.ofHours(1), result.countedDuration());
		assertEquals(Duration.ofHours(1), result.requiredDuration());
		assertTrue(result.isCompleted());
	}

	@Test
	void shouldIgnoreNonWalkingActivity() {
		WalkingInHRZonesTimeCounter counter = new WalkingInHRZonesTimeCounter("September walking", Duration.ofHours(1),
				START, END, List.of(2, 3));

		Activity activity = new Activity();
		activity.setSport(Sport.MEDITATION);

		counter.onActivity(activity);

		WalkingInHRZonesTimeResults result = (WalkingInHRZonesTimeResults) counter.getResult();

		assertEquals(Duration.ZERO, result.countedDuration());
		assertFalse(result.isCompleted());
	}

	@Test
	void shouldIgnoreWalkingOutsideDateRange() {
		WalkingInHRZonesTimeCounter counter = new WalkingInHRZonesTimeCounter("September walking", Duration.ofHours(1),
				START, END, List.of(2, 3));

		Walking walking = createWalking(Instant.parse("2026-10-01T10:00:00Z"), List.of(createZone(0, Duration.ZERO),
				createZone(1, Duration.ZERO), createZone(2, Duration.ofHours(2)), createZone(3, Duration.ZERO)));

		counter.onActivity(walking);

		WalkingInHRZonesTimeResults result = (WalkingInHRZonesTimeResults) counter.getResult();

		assertEquals(Duration.ZERO, result.countedDuration());
		assertFalse(result.isCompleted());
	}

	@Test
	void shouldAccumulateMultipleWalkingActivities() {
		WalkingInHRZonesTimeCounter counter = new WalkingInHRZonesTimeCounter("September walking", Duration.ofHours(1),
				START, END, List.of(2, 3));

		counter.onActivity(createWalking(Instant.parse("2026-09-10T10:00:00Z"),
				List.of(createZone(0, Duration.ZERO), createZone(1, Duration.ZERO),
						createZone(2, Duration.ofMinutes(20)), createZone(3, Duration.ofMinutes(10)))));

		counter.onActivity(createWalking(Instant.parse("2026-09-20T10:00:00Z"),
				List.of(createZone(0, Duration.ZERO), createZone(1, Duration.ZERO),
						createZone(2, Duration.ofMinutes(15)), createZone(3, Duration.ofMinutes(15)))));

		WalkingInHRZonesTimeResults result = (WalkingInHRZonesTimeResults) counter.getResult();

		assertEquals(Duration.ofHours(1), result.countedDuration());
		assertTrue(result.isCompleted());
	}

	private Walking createWalking(Instant startTime, List<HeartRateZone> zones) {
		Walking walking = new Walking();

		walking.setSport(Sport.WALKING);
		walking.setStartTime(startTime);
		walking.setHeartRateZones(new HeartRateZones(zones));

		return walking;
	}

	private HeartRateZone createZone(int number, Duration time) {
		return new HeartRateZone(number, null, null, time);
	}
}
