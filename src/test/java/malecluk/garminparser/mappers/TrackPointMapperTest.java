package malecluk.garminparser.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.garmin.fit.DateTime;
import com.garmin.fit.RecordMesg;

import malecluk.garminparser.model.component.ActivityTrack;
import malecluk.garminparser.model.component.TrackPoint;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

class TrackPointMapperTest {

	private TrackPointMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new TrackPointMapper();
	}

	@Test
	void map_mapsRecordMessageToTrackPoint() {
		Instant timestamp = Instant.parse("2026-10-08T10:15:30Z");
		RecordMesg record = createRecord(timestamp, 500_000_000, 1_000_000_000, 1234.5f, 456.7f);

		ActivityTrack track = mapper.map(List.of(record));

		assertEquals(1, track.getPoints().size());

		TrackPoint point = track.getPoints().get(0);

		assertEquals(timestamp, point.timestamp());
		assertEquals(new Position(500_000_000 * (180.0 / 2147483648.0), 1_000_000_000 * (180.0 / 2147483648.0)),
				point.position());
		assertEquals(new Distance(record.getDistance()), point.distance());
		assertEquals(record.getEnhancedAltitude(), point.elevation());
	}

	@Test
	void map_mapsTimestampToInstant() {
		Instant timestamp = Instant.parse("2026-10-08T10:15:30Z");
		RecordMesg record = createRecord(timestamp, 100_000_000, 200_000_000, null, null);

		ActivityTrack track = mapper.map(List.of(record));

		assertEquals(timestamp, track.getPoints().get(0).timestamp());
	}

	@Test
	void map_convertsPositionFromSemicirclesToDegrees() {
		int latitude = 123_456_789;
		int longitude = -987_654_321;

		RecordMesg record = createRecord(Instant.parse("2026-10-08T10:15:30Z"), latitude, longitude, null, null);

		ActivityTrack track = mapper.map(List.of(record));

		Position position = track.getPoints().get(0).position();

		assertEquals(latitude * (180.0 / 2147483648.0), position.latitude());
		assertEquals(longitude * (180.0 / 2147483648.0), position.longitude());
	}

	@Test
	void map_mapsDistanceToDistanceValueObject() {
		RecordMesg record = createRecord(Instant.parse("2026-10-08T10:15:30Z"), 100_000_000, 200_000_000, 9876.543f,
				null);

		Distance expected = new Distance(record.getDistance());

		ActivityTrack track = mapper.map(List.of(record));

		assertEquals(expected, track.getPoints().get(0).distance());
	}

	@Test
	void map_mapsEnhancedAltitudeToElevation() {
		RecordMesg record = createRecord(Instant.parse("2026-10-08T10:15:30Z"), 100_000_000, 200_000_000, null,
				321.987f);

		ActivityTrack track = mapper.map(List.of(record));

		assertEquals(record.getEnhancedAltitude(), track.getPoints().get(0).elevation());
	}

	@Test
	void map_withSortedInputPreservesChronologicalOrder() {
		RecordMesg first = createRecord(Instant.parse("2026-10-08T10:00:00Z"), 100_000_000, 200_000_000, 100.0f, 10.0f);

		RecordMesg second = createRecord(Instant.parse("2026-10-08T10:01:00Z"), 110_000_000, 210_000_000, 200.0f,
				20.0f);

		RecordMesg third = createRecord(Instant.parse("2026-10-08T10:02:00Z"), 120_000_000, 220_000_000, 300.0f, 30.0f);

		ActivityTrack track = mapper.map(List.of(first, second, third));

		assertEquals(
				List.of(Instant.parse("2026-10-08T10:00:00Z"), Instant.parse("2026-10-08T10:01:00Z"),
						Instant.parse("2026-10-08T10:02:00Z")),
				track.getPoints().stream().map(TrackPoint::timestamp).toList());
	}

	@Test
	void map_withShuffledInputSortsPointsByTimestamp() {
		RecordMesg first = createRecord(Instant.parse("2026-10-08T10:00:00Z"), 100_000_000, 200_000_000, 100.0f, 10.0f);

		RecordMesg second = createRecord(Instant.parse("2026-10-08T10:01:00Z"), 110_000_000, 210_000_000, 200.0f,
				20.0f);

		RecordMesg third = createRecord(Instant.parse("2026-10-08T10:02:00Z"), 120_000_000, 220_000_000, 300.0f, 30.0f);

		ActivityTrack track = mapper.map(List.of(third, first, second));

		assertEquals(
				List.of(Instant.parse("2026-10-08T10:00:00Z"), Instant.parse("2026-10-08T10:01:00Z"),
						Instant.parse("2026-10-08T10:02:00Z")),
				track.getPoints().stream().map(TrackPoint::timestamp).toList());
	}

	@Test
	void map_withEmptyList_returnsEmptyActivityTrack() {
		ActivityTrack track = mapper.map(List.of());

		assertEquals(List.of(), track.getPoints());
	}

	@Test
	void map_withNullList_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> mapper.map(null));
	}

	@Test
	void map_withNullRecordMessage_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> mapper.map(List.of((RecordMesg) null)));
	}

	@Test
	void map_withNullDistance_mapsNullDistance() {
		RecordMesg record = createRecord(Instant.parse("2026-10-08T10:15:30Z"), 100_000_000, 200_000_000, null, 123.4f);

		ActivityTrack track = mapper.map(List.of(record));

		assertNull(track.getPoints().get(0).distance());
		assertEquals(123.4f, track.getPoints().get(0).elevation());
	}

	@Test
	void map_withNullElevation_mapsNullElevation() {
		RecordMesg record = createRecord(Instant.parse("2026-10-08T10:15:30Z"), 100_000_000, 200_000_000, 123.4f, null);

		ActivityTrack track = mapper.map(List.of(record));

		assertEquals(new Distance(123.4f), track.getPoints().get(0).distance());
		assertNull(track.getPoints().get(0).elevation());
	}

	@Test
	void map_withNullLatitude_preservesNullLatitude() {
		RecordMesg record = createRecord(Instant.parse("2026-10-08T10:15:30Z"), null, 200_000_000, null, null);

		ActivityTrack track = mapper.map(List.of(record));

		Position position = track.getPoints().get(0).position();

		assertNull(position.latitude());
		assertEquals(200_000_000 * (180.0 / 2147483648.0), position.longitude());
	}

	@Test
	void map_withNullLongitude_preservesNullLongitude() {
		RecordMesg record = createRecord(Instant.parse("2026-10-08T10:15:30Z"), 100_000_000, null, null, null);

		ActivityTrack track = mapper.map(List.of(record));

		Position position = track.getPoints().get(0).position();

		assertEquals(100_000_000 * (180.0 / 2147483648.0), position.latitude());
		assertNull(position.longitude());
	}

	@Test
	void map_withBothPositionsNull_skipsRecord() {
		RecordMesg record = createRecord(Instant.parse("2026-10-08T10:15:30Z"), null, null, null, null);

		ActivityTrack track = mapper.map(List.of(record));

		assertEquals(List.of(), track.getPoints());
	}

	@Test
	void map_withRecordWithoutPosition_skipsOnlyInvalidRecord() {
		RecordMesg invalidRecord = createRecord(Instant.parse("2026-10-08T10:15:00Z"), null, null, null, null);

		RecordMesg validRecord = createRecord(Instant.parse("2026-10-08T10:16:00Z"), 100_000_000, 200_000_000, 123.4f,
				321.0f);

		ActivityTrack track = mapper.map(List.of(invalidRecord, validRecord));

		assertEquals(1, track.getPoints().size());
		assertEquals(Instant.parse("2026-10-08T10:16:00Z"), track.getPoints().getFirst().timestamp());
	}

	@Test
	void map_withNullTimestamp_throwsNullPointerException() {
		RecordMesg record = new RecordMesg();
		record.setPositionLat(100_000_000);
		record.setPositionLong(200_000_000);

		assertThrows(NullPointerException.class, () -> mapper.map(List.of(record)));
	}

	private RecordMesg createRecord(Instant timestamp, Integer latitude, Integer longitude, Float distance,
			Float enhancedAltitude) {

		RecordMesg record = new RecordMesg();

		if (timestamp != null) {
			DateTime dateTime = new DateTime(Date.from(timestamp));
			record.setTimestamp(dateTime);
		}

		record.setPositionLat(latitude);
		record.setPositionLong(longitude);
		record.setDistance(distance);
		record.setEnhancedAltitude(enhancedAltitude);

		return record;
	}
}
