package malecluk.garminparser.model.activities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;
import malecluk.garminparser.model.component.ActivityLaps;
import malecluk.garminparser.model.component.ActivitySets;

class ActivityTest {

	@Test
	void constructor_withIds_createsActivityWithIds() {
		ActivityIds activityIds = new ActivityIds(new ActivityId("123456789-1234567890"),
				new SourceFileHash("abcdef123456"));
		Activity activity = new Activity(activityIds);

		assertNotNull(activity);
		assertEquals(activityIds, activity.getIds());
	}

	@Test
	void constructor_withNullIds_throwsNullPointerException() {
		NullPointerException exception = assertThrows(NullPointerException.class, () -> new Activity(null));

		assertEquals("ids must not be null", exception.getMessage());
	}

	@Test
	void setLaps_withLaps_setsLaps() {
		Activity activity = new Activity(createActivityIds());
		ActivityLaps laps = new ActivityLaps();

		activity.setLaps(laps);

		assertEquals(laps, activity.getLaps());
	}

	@Test
	void setLaps_withNull_throwsNullPointerException() {
		Activity activity = new Activity(createActivityIds());

		NullPointerException exception = assertThrows(NullPointerException.class, () -> activity.setLaps(null));

		assertEquals("laps must not be null", exception.getMessage());
	}

	@Test
	void setSets_withSets_setsSets() {
		Activity activity = new Activity(createActivityIds());
		ActivitySets sets = new ActivitySets();

		activity.setSets(sets);

		assertEquals(sets, activity.getSets());
	}

	@Test
	void setSets_withNull_throwsNullPointerException() {
		Activity activity = new Activity(createActivityIds());

		NullPointerException exception = assertThrows(NullPointerException.class, () -> activity.setSets(null));

		assertEquals("sets must not be null", exception.getMessage());
	}

	private ActivityIds createActivityIds() {
		return new ActivityIds(new ActivityId("123456789-1234567890"), new SourceFileHash("abcdef123456"));
	}
}
