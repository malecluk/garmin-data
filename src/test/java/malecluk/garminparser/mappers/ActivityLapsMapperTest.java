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
import com.garmin.fit.LapMesg;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivityLap;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

class ActivityLapsMapperTest {

	private ActivityLapsMapper mapper;
	private final ActivityIds activityIds = new ActivityIds(new ActivityId("12345"), new SourceFileHash("67890"));

	@BeforeEach
	void setUp() {
		mapper = new ActivityLapsMapper();
	}

	@Test
	void map_mapsAllLapValues() {
		Activity activity = new Activity(activityIds);

		LapMesg message = createLapMessage(3);

		DateTime startTime = new DateTime(1735689600);
		DateTime endTime = new DateTime(1735690200);

		message.setStartTime(startTime);
		message.setTimestamp(endTime);

		message.setStartPositionLat(500000000);
		message.setStartPositionLong(250000000);
		message.setEndPositionLat(510000000);
		message.setEndPositionLong(260000000);

		message.setTotalElapsedTime(123.456f);
		message.setTotalTimerTime(120.123f);

		message.setTotalDistance(1234.5f);

		message.setAvgSpeed(2.5f);
		message.setMaxSpeed(3.75f);

		message.setAvgHeartRate((short) 125);
		message.setMaxHeartRate((short) 151);

		message.setAvgCadence((short) 82);
		message.setMaxCadence((short) 96);

		message.setTotalCalories(123);

		message.setTotalAscent(15);
		message.setTotalDescent(12);

		message.setMinAltitude(245.5f);
		message.setMaxAltitude(263.75f);

		message.setAvgTemperature((byte) 18);
		message.setMinTemperature((byte) 15);
		message.setMaxTemperature((byte) 21);

		mapper.map(activity, List.of(message));

		assertNotNull(activity.getLaps());
		assertEquals(1, activity.getLaps().size());

		ActivityLap lap = activity.getLaps().get(3);

		assertNotNull(lap);

		assertEquals(3, lap.getNumber());

		assertEquals(
				startTime.getDate().toInstant(),
				lap.getStartTime());

		assertEquals(
				endTime.getDate().toInstant(),
				lap.getEndTime());

		assertEquals(
				new Position(
						500000000 * (180.0 / 2147483648.0),
						250000000 * (180.0 / 2147483648.0)),
				lap.getStartPosition());

		assertEquals(
				new Position(
						510000000 * (180.0 / 2147483648.0),
						260000000 * (180.0 / 2147483648.0)),
				lap.getEndPosition());

		assertEquals(
				Duration.ofMillis(123456),
				lap.getElapsedTime());

		assertEquals(
				Duration.ofMillis(120123),
				lap.getTimerTime());

		assertEquals(
				new Distance(1234.5),
				lap.getTotalDistance());

		assertEquals(2.5f, lap.getAverageSpeed());
		assertEquals(3.75f, lap.getMaximumSpeed());

		assertEquals(125, lap.getAverageHeartRate());
		assertEquals(151, lap.getMaximumHeartRate());

		assertEquals(82.0f, lap.getAverageCadence());
		assertEquals(96.0f, lap.getMaximumCadence());

		assertEquals(123, lap.getTotalCalories());

		assertEquals(
				new Distance(15),
				lap.getTotalAscent());

		assertEquals(
				new Distance(12),
				lap.getTotalDescent());

		assertEquals(245.5, lap.getMinimumAltitude(), 0.15); // TODO check why this big delta is needed
		assertEquals(263.75, lap.getMaximumAltitude(), 0.15); // TODO check why this big delta is needed

		assertEquals(18.0f, lap.getAverageTemperature());
		assertEquals(15.0f, lap.getMinimumTemperature());
		assertEquals(21.0f, lap.getMaximumTemperature());
	}

	@Test
	void map_ordersLapsByMessageIndexRegardlessOfInputOrder() {
		Activity activity = new Activity(activityIds);

		LapMesg lap2 = createLapMessage(2);
		LapMesg lap0 = createLapMessage(0);
		LapMesg lap1 = createLapMessage(1);

		mapper.map(activity, List.of(lap2, lap0, lap1));

		assertEquals(3, activity.getLaps().size());

		assertEquals(0, activity.getLaps().get(0).getNumber());
		assertEquals(1, activity.getLaps().get(1).getNumber());
		assertEquals(2, activity.getLaps().get(2).getNumber());

		assertEquals(
				List.of(0, 1, 2),
				activity.getLaps()
						.lapsList()
						.stream()
						.map(ActivityLap::getNumber)
						.toList());
	}

	@Test
	void map_withNullMessages_keepsEmptyLaps() {
		Activity activity = new Activity(activityIds);

		mapper.map(activity, null);

		assertNotNull(activity.getLaps());
		assertEquals(0, activity.getLaps().size());
	}

	@Test
	void map_withEmptyMessages_keepsEmptyLaps() {
		Activity activity = new Activity(activityIds);

		mapper.map(activity, List.of());

		assertNotNull(activity.getLaps());
		assertEquals(0, activity.getLaps().size());
	}

	@Test
	void map_preservesMissingOptionalValuesAsNull() {
		Activity activity = new Activity(activityIds);

		LapMesg message = createLapMessage(0);

		mapper.map(activity, List.of(message));

		ActivityLap lap = activity.getLaps().get(0);

		assertNotNull(lap);

		assertEquals(0, lap.getNumber());

		assertNull(lap.getStartTime());
		assertNull(lap.getEndTime());

		assertNull(lap.getStartPosition());
		assertNull(lap.getEndPosition());

		assertNull(lap.getElapsedTime());
		assertNull(lap.getTimerTime());

		assertNull(lap.getTotalDistance());

		assertNull(lap.getAverageSpeed());
		assertNull(lap.getMaximumSpeed());

		assertNull(lap.getAverageHeartRate());
		assertNull(lap.getMaximumHeartRate());

		assertNull(lap.getAverageCadence());
		assertNull(lap.getMaximumCadence());

		assertNull(lap.getTotalCalories());

		assertNull(lap.getTotalAscent());
		assertNull(lap.getTotalDescent());

		assertNull(lap.getMinimumAltitude());
		assertNull(lap.getMaximumAltitude());

		assertNull(lap.getAverageTemperature());
		assertNull(lap.getMinimumTemperature());
		assertNull(lap.getMaximumTemperature());
	}

	@Test
	void map_rejectsDuplicateLapNumbers() {
		Activity activity = new Activity(activityIds);

		LapMesg lap1 = createLapMessage(1);
		LapMesg duplicateLap1 = createLapMessage(1);

		assertThrows(
				IllegalArgumentException.class,
				() -> mapper.map(
						activity,
						List.of(lap1, duplicateLap1)));
	}

	private LapMesg createLapMessage(int messageIndex) {
		LapMesg message = new LapMesg();
		message.setMessageIndex(messageIndex);
		return message;
	}
}