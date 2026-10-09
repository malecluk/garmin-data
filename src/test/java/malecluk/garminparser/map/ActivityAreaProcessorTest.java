package malecluk.garminparser.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import malecluk.garminparser.model.component.ActivityTrack;
import malecluk.garminparser.model.component.TrackPoint;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

class ActivityAreaProcessorTest {

	private ActivityAreaProcessor processor;

	@BeforeEach
	void setUp() {
		TrackGeometryBuilder trackGeometryBuilder = new TrackGeometryBuilder();
		CoordinateTransformer coordinateTransformer = new CoordinateTransformer();
		TrackBufferBuilder trackBufferBuilder = new TrackBufferBuilder(coordinateTransformer);
		ActivityAreaMerger activityAreaMerger = new ActivityAreaMerger();

		processor = new ActivityAreaProcessor(trackGeometryBuilder, coordinateTransformer, trackBufferBuilder,
				activityAreaMerger);
	}

	@Test
	void process_mergesOverlappingActivityAreas() {
		ActivityTrack firstTrack = createTrack(new Position(50.0000, 14.0000), new Position(50.0010, 14.0010));

		ActivityTrack secondTrack = createTrack(new Position(50.0001, 14.0001), new Position(50.0011, 14.0011));

		Geometry result = processor.process(List.of(firstTrack, secondTrack));

		assertFalse(result.isEmpty());
		assertInstanceOf(Polygon.class, result);
	}

	@Test
	void process_keepsDistantActivityAreasSeparate() {
		ActivityTrack firstTrack = createTrack(new Position(50.0000, 14.0000), new Position(50.0010, 14.0010));

		ActivityTrack secondTrack = createTrack(new Position(50.0000, 14.0500), new Position(50.0010, 14.0510));

		Geometry result = processor.process(List.of(firstTrack, secondTrack));

		assertFalse(result.isEmpty());
		MultiPolygon multiPolygon = assertInstanceOf(MultiPolygon.class, result);
		assertEquals(2, multiPolygon.getNumGeometries());
	}

	@Test
	void process_skipsTrackWithoutUsableCoordinates() {
		ActivityTrack invalidTrack = createTrack(new Position(null, null), new Position(null, null));

		ActivityTrack validTrack = createTrack(new Position(50.0000, 14.0000), new Position(50.0010, 14.0010));

		Geometry result = processor.process(List.of(invalidTrack, validTrack));

		assertFalse(result.isEmpty());
		assertInstanceOf(Polygon.class, result);
	}

	@Test
	void process_returnsEmptyGeometryWhenNoTrackHasUsableCoordinates() {
		ActivityTrack invalidTrack = createTrack(new Position(null, null), new Position(null, null));

		Geometry result = processor.process(List.of(invalidTrack));

		assertTrue(result.isEmpty());
	}

	@Test
	void process_returnsEmptyGeometryForEmptyCollection() {
		Geometry result = processor.process(List.of());

		assertTrue(result.isEmpty());
	}

	@Test
	void process_throwsNullPointerExceptionWhenTracksIsNull() {
		assertThrows(NullPointerException.class, () -> processor.process(null));
	}

	@Test
	void process_throwsNullPointerExceptionWhenTracksContainsNull() {
		assertThrows(NullPointerException.class, () -> processor.process(java.util.Arrays
				.asList(createTrack(new Position(50.0000, 14.0000), new Position(50.0010, 14.0010)), null)));
	}

	private ActivityTrack createTrack(Position firstPosition, Position secondPosition) {
		return new ActivityTrack(
				List.of(new TrackPoint(Instant.parse("2026-10-01T10:00:00Z"), firstPosition, Distance.ZERO, null),
						new TrackPoint(Instant.parse("2026-10-01T10:01:00Z"), secondPosition, Distance.ZERO, null)));
	}
}