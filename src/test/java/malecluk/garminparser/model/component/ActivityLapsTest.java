package malecluk.garminparser.model.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ActivityLapsTest {

	@Test
	void add_storesLapAndAllowsLookupByNumber() {
		ActivityLaps laps = new ActivityLaps();

		ActivityLap lap = new ActivityLap();
		lap.setNumber(3);

		laps.add(lap);

		assertEquals(1, laps.size());
		assertEquals(lap, laps.get(3));
		assertTrue(laps.contains(3));
		assertFalse(laps.isEmpty());
	}

	@Test
	void add_storesLapsInAscendingNumberOrder() {
		ActivityLaps laps = new ActivityLaps();

		ActivityLap lap3 = createLap(3);
		ActivityLap lap1 = createLap(1);
		ActivityLap lap2 = createLap(2);

		laps.add(lap3);
		laps.add(lap1);
		laps.add(lap2);

		assertEquals(java.util.List.of(lap1, lap2, lap3), laps.lapsList());
	}

	@Test
	void get_returnsNullWhenLapDoesNotExist() {
		ActivityLaps laps = new ActivityLaps();

		assertNull(laps.get(1));
	}

	@Test
	void contains_returnsFalseWhenLapDoesNotExist() {
		ActivityLaps laps = new ActivityLaps();

		assertFalse(laps.contains(1));
	}

	@Test
	void add_rejectsDuplicateLapNumber() {
		ActivityLaps laps = new ActivityLaps();

		laps.add(createLap(1));

		ActivityLap duplicate = createLap(1);

		assertThrows(IllegalArgumentException.class, () -> laps.add(duplicate));
	}

	@Test
	void add_rejectsNullLap() {
		ActivityLaps laps = new ActivityLaps();

		assertThrows(NullPointerException.class, () -> laps.add(null));
	}

	@Test
	void add_rejectsLapWithoutNumber() {
		ActivityLaps laps = new ActivityLaps();

		ActivityLap lap = new ActivityLap();

		assertThrows(NullPointerException.class, () -> laps.add(lap));
	}

	@Test
	void lapsList_returnsUnmodifiableSnapshot() {
		ActivityLaps laps = new ActivityLaps();

		ActivityLap lap = createLap(1);
		laps.add(lap);

		var result = laps.lapsList();

		assertThrows(UnsupportedOperationException.class, () -> result.add(createLap(2)));

		assertEquals(1, laps.size());
	}

	private ActivityLap createLap(int number) {
		ActivityLap lap = new ActivityLap();
		lap.setNumber(number);
		return lap;
	}
}