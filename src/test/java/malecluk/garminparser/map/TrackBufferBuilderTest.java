
package malecluk.garminparser.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import malecluk.garminparser.model.value.Position;

class TrackBufferBuilderTest {

	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

	private CoordinateTransformer coordinateTransformer;
	private TrackBufferBuilder bufferBuilder;
	private CoordinateReferenceSystem projection;

	@BeforeEach
	void setUp() {
		coordinateTransformer = new CoordinateTransformer();
		bufferBuilder = new TrackBufferBuilder(coordinateTransformer);
		projection = coordinateTransformer.determineProjection(new Position(50.0, 14.0));
	}

	@Test
	void build_withSingleSegment_returnsNonEmptyPolygonInWgs84() {
		List<LineString> segments = List.of(createSegment(14.0, 50.0, 14.01, 50.01));

		Geometry result = bufferBuilder.build(segments, projection);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertTrue(result.getArea() > 0.0);
		assertTrue(result.getGeometryType().equals("Polygon") || result.getGeometryType().equals("MultiPolygon"));

		// Result coordinates must be geographic coordinates, not UTM metres.
		Coordinate coordinate = result.getCoordinates()[0];
		assertTrue(coordinate.getX() >= -180.0 && coordinate.getX() <= 180.0);
		assertTrue(coordinate.getY() >= -90.0 && coordinate.getY() <= 90.0);
	}

	@Test
	void build_withTwoDistantSegments_keepsBuffersSeparate() {
		List<LineString> segments = List.of(createSegment(14.0, 50.0, 14.001, 50.0),
				createSegment(14.02, 50.0, 14.021, 50.0));

		Geometry result = bufferBuilder.build(segments, projection);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals("MultiPolygon", result.getGeometryType());
		assertEquals(2, result.getNumGeometries());
	}

	@Test
	void build_withNearbySegments_returnsSingleMergedBuffer() {
		List<LineString> segments = List.of(createSegment(14.0, 50.0, 14.001, 50.0),
				createSegment(14.0015, 50.0, 14.0025, 50.0));

		Geometry result = bufferBuilder.build(segments, projection);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals("Polygon", result.getGeometryType());
		assertEquals(1, result.getNumGeometries());
	}

	@Test
	void build_withEmptySegments_returnsEmptyGeometry() {
		Geometry result = bufferBuilder.build(List.of(), projection);

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	void build_withNullSegments_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> bufferBuilder.build(null, projection));
	}

	@Test
	void build_withNullProjection_throwsNullPointerException() {
		assertThrows(NullPointerException.class,
				() -> bufferBuilder.build(List.of(createSegment(14.0, 50.0, 14.01, 50.01)), null));
	}

	@Test
	void build_withNullSegment_throwsNullPointerException() {
		assertThrows(NullPointerException.class,
				() -> bufferBuilder.build(java.util.Arrays.asList((LineString) null), projection));
	}

	private LineString createSegment(double longitude1, double latitude1, double longitude2, double latitude2) {

		return GEOMETRY_FACTORY.createLineString(
				new Coordinate[] { new Coordinate(longitude1, latitude1), new Coordinate(longitude2, latitude2) });
	}
}
