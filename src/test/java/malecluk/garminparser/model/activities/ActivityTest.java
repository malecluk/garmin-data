package malecluk.garminparser.model.activities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;

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
}
