package malecluk.garminparser.model.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

class TrackPointTest {

	private static final Instant TIMESTAMP = Instant.parse("2026-10-08T10:15:30Z");
	private static final Position POSITION = new Position(50.123456, 14.654321);
	private static final Distance DISTANCE = new Distance(1234.567);
	private static final Float ELEVATION = 321.987f;

	@Test
	void constructor_storesAllValues() {
		TrackPoint trackPoint = new TrackPoint(TIMESTAMP, POSITION, DISTANCE, ELEVATION);

		assertEquals(TIMESTAMP, trackPoint.timestamp());
		assertEquals(POSITION, trackPoint.position());
		assertEquals(DISTANCE, trackPoint.distance());
		assertEquals(ELEVATION, trackPoint.elevation());
	}

	@Test
	void constructor_rejectsNullTimestamp() {
		assertThrows(NullPointerException.class, () -> new TrackPoint(null, POSITION, DISTANCE, ELEVATION));
	}

	@Test
	void constructor_rejectsNullPosition() {
		assertThrows(NullPointerException.class, () -> new TrackPoint(TIMESTAMP, null, DISTANCE, ELEVATION));
	}

	@Test
	void constructor_allowsNullDistance() {
		TrackPoint trackPoint = new TrackPoint(TIMESTAMP, POSITION, null, ELEVATION);

		assertNull(trackPoint.distance());
	}

	@Test
	void constructor_allowsNullElevation() {
		TrackPoint trackPoint = new TrackPoint(TIMESTAMP, POSITION, DISTANCE, null);

		assertNull(trackPoint.elevation());
	}
}
