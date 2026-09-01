package malecluk.garminparser.model.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;

class ActivitySetTest {

	@Test
	void gettersAndSetters_storeAndReturnValues() {
		ActivitySet set = new ActivitySet();

		Integer number = 3;
		Instant setTimestamp = Instant.parse("2026-09-15T10:15:30Z");
		Instant startTime = Instant.parse("2026-09-15T10:14:00Z");
		Duration duration = Duration.ofSeconds(90);
		Integer setType = 1;

		set.setNumber(number);
		set.setSetTimestamp(setTimestamp);
		set.setStartTime(startTime);
		set.setDuration(duration);
		set.setSetType(setType);

		assertEquals(number, set.getNumber());
		assertEquals(setTimestamp, set.getSetTimestamp());
		assertEquals(startTime, set.getStartTime());
		assertEquals(duration, set.getDuration());
		assertEquals(setType, set.getSetType());
	}

	@Test
	void newSet_hasNullValues() {
		ActivitySet set = new ActivitySet();

		assertNull(set.getNumber());
		assertNull(set.getSetTimestamp());
		assertNull(set.getStartTime());
		assertNull(set.getDuration());
		assertNull(set.getSetType());
	}
}
