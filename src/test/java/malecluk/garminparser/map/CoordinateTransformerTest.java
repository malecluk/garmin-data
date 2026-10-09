
package malecluk.garminparser.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import malecluk.garminparser.model.value.Position;

class CoordinateTransformerTest {

	private static final double COORDINATE_TOLERANCE = 1.0e-7;

	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

	private CoordinateTransformer transformer;

	@BeforeEach
	void setUp() {
		transformer = new CoordinateTransformer();
	}

	@Test
	void determineProjection_withPositionInNorthernHemisphere_returnsCorrectUtmZone() throws Exception {
		Position position = new Position(50.0, 14.0);

		CoordinateReferenceSystem projection = transformer.determineProjection(position);

		assertEquals("EPSG:32633", CRS.lookupIdentifier(projection, true));
	}

	@Test
	void determineProjection_withPositionInSouthernHemisphere_returnsCorrectUtmZone() throws Exception {
		Position position = new Position(-33.9, 151.2);

		CoordinateReferenceSystem projection = transformer.determineProjection(position);

		assertEquals("EPSG:32756", CRS.lookupIdentifier(projection, true));
	}

	@Test
	void determineProjection_withLongitudeAtNegative180_returnsFirstUtmZone() throws Exception {
		Position position = new Position(10.0, -180.0);

		CoordinateReferenceSystem projection = transformer.determineProjection(position);

		assertEquals("EPSG:32601", CRS.lookupIdentifier(projection, true));
	}

	@Test
	void determineProjection_withLongitudeAtPositive180_returnsLastUtmZone() throws Exception {
		Position position = new Position(10.0, 180.0);

		CoordinateReferenceSystem projection = transformer.determineProjection(position);

		assertEquals("EPSG:32660", CRS.lookupIdentifier(projection, true));
	}

	@Test
	void determineProjection_withNullPosition_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> transformer.determineProjection(null));
	}

	@Test
	void determineProjection_withMissingLatitude_throwsNullPointerException() {
		Position position = new Position(null, 14.0);

		assertThrows(NullPointerException.class, () -> transformer.determineProjection(position));
	}

	@Test
	void determineProjection_withMissingLongitude_throwsNullPointerException() {
		Position position = new Position(50.0, null);

		assertThrows(NullPointerException.class, () -> transformer.determineProjection(position));
	}

	@Test
	void determineProjection_withLatitudeOutsideValidRange_throwsIllegalArgumentException() {
		Position position = new Position(91.0, 14.0);

		assertThrows(IllegalArgumentException.class, () -> transformer.determineProjection(position));
	}

	@Test
	void determineProjection_withLongitudeOutsideValidRange_throwsIllegalArgumentException() {
		Position position = new Position(50.0, 181.0);

		assertThrows(IllegalArgumentException.class, () -> transformer.determineProjection(position));
	}

	@Test
	void determineProjection_withLatitudeOutsideUtmRange_throwsIllegalArgumentException() {
		Position position = new Position(85.0, 14.0);

		assertThrows(IllegalArgumentException.class, () -> transformer.determineProjection(position));
	}

	@Test
	void determineProjection_withNonFiniteCoordinate_throwsIllegalArgumentException() {
		Position position = new Position(50.0, Double.NaN);

		assertThrows(IllegalArgumentException.class, () -> transformer.determineProjection(position));
	}

	@Test
	void transformToProjection_withWgs84LineString_returnsProjectedGeometry() throws Exception {
		LineString original = createTestLineString();
		CoordinateReferenceSystem projection = transformer.determineProjection(new Position(50.0, 14.0));

		Geometry projected = transformer.transformToProjection(original, projection);

		assertNotNull(projected);
		assertEquals(original.getNumPoints(), projected.getNumPoints());
		assertTrue(projected.getCoordinates()[0].getX() > 100_000.0);
		assertTrue(projected.getCoordinates()[0].getX() < 900_000.0);

		// The input geometry must remain unchanged.
		assertEquals(14.0, original.getCoordinateN(0).getX());
		assertEquals(50.0, original.getCoordinateN(0).getY());
	}

	@Test
	void transformToWgs84_afterProjection_roundTripsCoordinates() {
		LineString original = createTestLineString();
		CoordinateReferenceSystem projection = transformer.determineProjection(new Position(50.0, 14.0));

		Geometry projected = transformer.transformToProjection(original, projection);
		Geometry restored = transformer.transformToWgs84(projected, projection);

		assertEquals(original.getNumPoints(), restored.getNumPoints());

		for (int i = 0; i < original.getNumPoints(); i++) {
			Coordinate expected = original.getCoordinateN(i);
			Coordinate actual = restored.getCoordinates()[i];

			assertEquals(expected.getX(), actual.getX(), COORDINATE_TOLERANCE);
			assertEquals(expected.getY(), actual.getY(), COORDINATE_TOLERANCE);
		}
	}

	@Test
	void transformToProjection_withNullGeometry_throwsNullPointerException() {
		CoordinateReferenceSystem projection = transformer.determineProjection(new Position(50.0, 14.0));

		assertThrows(NullPointerException.class, () -> transformer.transformToProjection(null, projection));
	}

	@Test
	void transformToProjection_withNullProjection_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> transformer.transformToProjection(createTestLineString(), null));
	}

	@Test
	void transformToWgs84_withNullGeometry_throwsNullPointerException() {
		CoordinateReferenceSystem projection = transformer.determineProjection(new Position(50.0, 14.0));

		assertThrows(NullPointerException.class, () -> transformer.transformToWgs84(null, projection));
	}

	@Test
	void transformToWgs84_withNullProjection_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> transformer.transformToWgs84(createTestLineString(), null));
	}

	private LineString createTestLineString() {
		return GEOMETRY_FACTORY.createLineString(new Coordinate[] { new Coordinate(14.0, 50.0),
				new Coordinate(14.01, 50.01), new Coordinate(14.02, 50.015) });
	}
}
