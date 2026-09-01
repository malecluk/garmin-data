
package malecluk.garminparser.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

import malecluk.garminparser.model.component.ActivityTrack;
import malecluk.garminparser.model.component.TrackPoint;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

class TrackGeometryBuilderTest {

	private TrackGeometryBuilder builder;

	@BeforeEach
	void setUp() {
		builder = new TrackGeometryBuilder();
	}

	@Test
	void build_withTwoValidPoints_returnsOneLineString() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(50.1, 14.1));

		List<LineString> segments = builder.build(track);

		assertEquals(1, segments.size());
		assertEquals(2, segments.get(0).getNumPoints());
	}

	@Test
	void build_preservesPointOrderAndUsesLongitudeAsX() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(50.1, 14.1), point(50.2, 14.2));

		LineString segment = builder.build(track).get(0);

		assertCoordinate(segment.getCoordinateN(0), 14.0, 50.0);
		assertCoordinate(segment.getCoordinateN(1), 14.1, 50.1);
		assertCoordinate(segment.getCoordinateN(2), 14.2, 50.2);
	}

	@Test
	void build_withMissingCoordinate_splitsTrackIntoSegments() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(50.1, 14.1), point(50.2, null), point(50.3, 14.3),
				point(50.4, 14.4));

		List<LineString> segments = builder.build(track);

		assertEquals(2, segments.size());
		assertEquals(2, segments.get(0).getNumPoints());
		assertEquals(2, segments.get(1).getNumPoints());

		assertCoordinate(segments.get(0).getCoordinateN(1), 14.1, 50.1);
		assertCoordinate(segments.get(1).getCoordinateN(0), 14.3, 50.3);
	}

	@Test
	void build_withMissingLatitude_splitsTrackIntoSegments() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(null, 14.1), point(50.2, 14.2));

		List<LineString> segments = builder.build(track);

		assertTrue(segments.isEmpty());
	}

	@Test
	void build_withNaNCoordinate_splitsTrackIntoSegments() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(Double.NaN, 14.1), point(50.2, 14.2));

		List<LineString> segments = builder.build(track);

		assertTrue(segments.isEmpty());
	}

	@Test
	void build_withInfiniteCoordinate_splitsTrackIntoSegments() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(50.1, Double.POSITIVE_INFINITY), point(50.2, 14.2));

		List<LineString> segments = builder.build(track);

		assertTrue(segments.isEmpty());
	}

	@Test
	void build_withLatitudeOutsideValidRange_splitsTrack() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(90.1, 14.1), point(50.2, 14.2));

		List<LineString> segments = builder.build(track);

		assertTrue(segments.isEmpty());
	}

	@Test
	void build_withLongitudeOutsideValidRange_splitsTrack() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(50.1, 180.1), point(50.2, 14.2));

		List<LineString> segments = builder.build(track);

		assertTrue(segments.isEmpty());
	}

	@Test
	void build_withSingleValidPoint_returnsEmptyList() {
		ActivityTrack track = createTrack(point(50.0, 14.0));

		List<LineString> segments = builder.build(track);

		assertTrue(segments.isEmpty());
	}

	@Test
	void build_withEmptyTrack_returnsEmptyList() {
		ActivityTrack track = createTrack();

		List<LineString> segments = builder.build(track);

		assertTrue(segments.isEmpty());
	}

	@Test
	void build_withMultipleInvalidGaps_returnsSeparateSegments() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(50.1, 14.1), point(null, null), point(50.2, 14.2),
				point(50.3, 14.3), point(Double.NaN, 14.4), point(50.4, 14.4), point(50.5, 14.5));

		List<LineString> segments = builder.build(track);

		assertEquals(3, segments.size());
		assertEquals(2, segments.get(0).getNumPoints());
		assertEquals(2, segments.get(1).getNumPoints());
		assertEquals(2, segments.get(2).getNumPoints());
	}

	@Test
	void build_withAntimeridianCrossing_splitsTrack() {
		ActivityTrack track = createTrack(point(10.0, 179.9), point(10.1, -179.9));

		List<LineString> segments = builder.build(track);

		assertTrue(segments.isEmpty());
	}

	@Test
	void build_withCoordinatesAtValidBoundaries_acceptsPoints() {
		ActivityTrack track = createTrack(point(-90.0, 0.0), point(90.0, 0.0));

		List<LineString> segments = builder.build(track);

		assertEquals(1, segments.size());
		assertEquals(2, segments.get(0).getNumPoints());
	}

	@Test
	void build_returnsUnmodifiableList() {
		ActivityTrack track = createTrack(point(50.0, 14.0), point(50.1, 14.1));

		List<LineString> segments = builder.build(track);

		assertThrows(UnsupportedOperationException.class, () -> segments.add(segments.get(0)));
	}

	@Test
	void build_withNullTrack_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> builder.build(null));
	}

	private ActivityTrack createTrack(TrackPoint... points) {
		return new ActivityTrack(List.of(points));
	}

	private TrackPoint point(Double latitude, Double longitude) {
		Position position = new Position(latitude, longitude);

		return new TrackPoint(Instant.parse("2026-10-09T10:00:00Z"), position, Distance.ZERO, null);
	}

	private void assertCoordinate(Coordinate coordinate, double expectedLongitude, double expectedLatitude) {

		assertEquals(expectedLongitude, coordinate.getX(), 1e-9);
		assertEquals(expectedLatitude, coordinate.getY(), 1e-9);
	}
}
