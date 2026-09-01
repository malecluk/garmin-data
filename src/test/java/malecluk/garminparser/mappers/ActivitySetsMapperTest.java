package malecluk.garminparser.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.garmin.fit.DateTime;
import com.garmin.fit.SetMesg;
import com.garmin.fit.SetType;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivitySet;

class ActivitySetsMapperTest {

	private ActivitySetsMapper mapper;
	private final ActivityIds activityIds = new ActivityIds(new ActivityId("12345"), new SourceFileHash("67890"));

	@BeforeEach
	void setUp() {
		mapper = new ActivitySetsMapper();
	}

	@Test
	void map_mapsAllSetValues() {
		Activity activity = new Activity(activityIds);

		SetMesg message = createSetMessage(3);

		DateTime timestamp = new DateTime(1735689600);
		DateTime startTime = new DateTime(1735689480);

		message.setTimestamp(timestamp);
		message.setStartTime(startTime);
		message.setDuration(123.456f);
		message.setSetType(SetType.ACTIVE);

		mapper.map(activity, List.of(message));

		assertNotNull(activity.getSets());
		assertEquals(1, activity.getSets().setList().size());

		ActivitySet set = activity.getSets().get(3);

		assertNotNull(set);

		assertEquals(3, set.getNumber());

		assertEquals(timestamp.getDate().toInstant(), set.getSetTimestamp());

		assertEquals(startTime.getDate().toInstant(), set.getStartTime());

		assertEquals(Duration.ofMillis(123456), set.getDuration());

		assertEquals(1, set.getSetType());
	}

	@Test
	void map_ordersSetsByMessageIndexRegardlessOfInputOrder() {
		Activity activity = new Activity(activityIds);

		SetMesg set2 = createSetMessage(2);
		SetMesg set0 = createSetMessage(0);
		SetMesg set1 = createSetMessage(1);

		mapper.map(activity, List.of(set2, set0, set1));

		assertEquals(3, activity.getSets().setList().size());

		assertEquals(0, activity.getSets().get(0).getNumber());
		assertEquals(1, activity.getSets().get(1).getNumber());
		assertEquals(2, activity.getSets().get(2).getNumber());

		assertEquals(List.of(0, 1, 2), activity.getSets().setList().stream().map(ActivitySet::getNumber).toList());
	}

	@Test
	void map_withNullMessages_keepsEmptySets() {
		Activity activity = new Activity(activityIds);

		mapper.map(activity, null);

		assertNotNull(activity.getSets());
		assertEquals(0, activity.getSets().setList().size());
	}

	@Test
	void map_withEmptyMessages_keepsEmptySets() {
		Activity activity = new Activity(activityIds);

		mapper.map(activity, List.of());

		assertNotNull(activity.getSets());
		assertEquals(0, activity.getSets().setList().size());
	}

	@Test
	void map_preservesMissingOptionalValuesAsNull() {
		Activity activity = new Activity(activityIds);

		SetMesg message = createSetMessage(0);
		message.setSetType(SetType.ACTIVE);

		mapper.map(activity, List.of(message));

		ActivitySet set = activity.getSets().get(0);

		assertNotNull(set);

		assertEquals(0, set.getNumber());

		assertNull(set.getSetTimestamp());
		assertNull(set.getStartTime());
		assertNull(set.getDuration());

		assertEquals(1, set.getSetType());
	}

	@Test
	void map_rejectsDuplicateSetNumbers() {
		Activity activity = new Activity(activityIds);

		SetMesg set1 = createSetMessage(1);
		SetMesg duplicateSet1 = createSetMessage(1);

		assertThrows(IllegalArgumentException.class, () -> mapper.map(activity, List.of(set1, duplicateSet1)));
	}

	@Test
	void map_replacesExistingSets() {
		Activity activity = new Activity(activityIds);

		SetMesg originalSet = createSetMessage(0);
		mapper.map(activity, List.of(originalSet));

		assertEquals(1, activity.getSets().setList().size());
		assertNotNull(activity.getSets().get(0));

		SetMesg newSet = createSetMessage(5);
		mapper.map(activity, List.of(newSet));

		assertEquals(1, activity.getSets().setList().size());
		assertNull(activity.getSets().get(0));
		assertNotNull(activity.getSets().get(5));
		assertEquals(5, activity.getSets().get(5).getNumber());
	}

	@Test
	void map_convertsFractionalDurationToMilliseconds() {
		Activity activity = new Activity(activityIds);

		SetMesg message = createSetMessage(0);
		message.setDuration(12.3456f);
		message.setSetType(SetType.ACTIVE);

		mapper.map(activity, List.of(message));

		assertEquals(Duration.ofMillis(12346), activity.getSets().get(0).getDuration());
	}

	@Test
	void map_withNullSetType_preservesNullSetType() {
		Activity activity = new Activity(activityIds);
		SetMesg message = createSetMessage(0);
		message.setSetType(null);
		
		mapper.map(activity, List.of(message));
		
		ActivitySet set = activity.getSets().get(0);
		assertNotNull(set);
		assertEquals(0, set.getNumber());
		assertNull(set.getSetType());
	}

	private SetMesg createSetMessage(int messageIndex) {
		SetMesg message = new SetMesg();
		message.setMessageIndex(messageIndex);
		message.setSetType(SetType.ACTIVE);
		return message;
	}
}
