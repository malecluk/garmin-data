package malecluk.garminparser.model.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

class ActivityTrackTest {

	@Test
	void constructor_storesPoints() {
		TrackPoint point1 = createPoint(1);
		TrackPoint point2 = createPoint(2);

		ActivityTrack track = new ActivityTrack(List.of(point1, point2));

		assertEquals(List.of(point1, point2), track.getPoints());
	}

	@Test
	void getPoints_returnsPointsInSameOrder() {
		TrackPoint point1 = createPoint(1);
		TrackPoint point2 = createPoint(2);
		TrackPoint point3 = createPoint(3);

		ActivityTrack track = new ActivityTrack(List.of(point1, point2, point3));

		assertEquals(point1, track.getPoints().get(0));
		assertEquals(point2, track.getPoints().get(1));
		assertEquals(point3, track.getPoints().get(2));
	}

	@Test
	void constructor_copiesInputList() {
		TrackPoint point1 = createPoint(1);
		TrackPoint point2 = createPoint(2);

		List<TrackPoint> points = new ArrayList<>(List.of(point1));

		ActivityTrack track = new ActivityTrack(points);

		points.add(point2);

		assertEquals(List.of(point1), track.getPoints());
	}

	@Test
	void getPoints_returnsImmutableList() {
		TrackPoint point = createPoint(1);

		ActivityTrack track = new ActivityTrack(List.of(point));

		assertThrows(UnsupportedOperationException.class,
				() -> track.getPoints().add(createPoint(2)));
	}

	@Test
	void constructor_rejectsNullPoints() {
		assertThrows(NullPointerException.class, () -> new ActivityTrack(null));
	}

	@Test
	void constructor_supportsEmptyList() {
		ActivityTrack track = new ActivityTrack(List.of());

		assertEquals(List.of(), track.getPoints());
	}

	private TrackPoint createPoint(int id) {
		return new TrackPoint(
				Instant.parse("2026-10-08T10:00:" + String.format("%02d", id) + "Z"),
				new Position(50.0 + id * 0.001, 14.0 + id * 0.001),
				new Distance(id * 100),
				100.0f + id);
	}
}
