
package malecluk.garminparser.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

class ActivityAreaMergerTest {

	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

	private ActivityAreaMerger merger;

	@BeforeEach
	void setUp() {
		merger = new ActivityAreaMerger();
	}

	@Test
	void merge_withOverlappingAreas_returnsSingleMergedArea() {
		Polygon first = createRectangle(0.0, 0.0, 2.0, 2.0);
		Polygon second = createRectangle(1.0, 1.0, 3.0, 3.0);

		Geometry result = merger.merge(List.of(first, second));

		assertFalse(result.isEmpty());
		assertTrue(result.isValid());
		assertEquals(1, result.getNumGeometries());
		assertEquals("Polygon", result.getGeometryType());
		assertEquals(7.0, result.getArea(), 1.0e-9);
	}

	@Test
	void merge_withDisjointAreas_preservesSeparateComponents() {
		Polygon first = createRectangle(0.0, 0.0, 1.0, 1.0);
		Polygon second = createRectangle(3.0, 3.0, 4.0, 4.0);

		Geometry result = merger.merge(List.of(first, second));

		assertFalse(result.isEmpty());
		assertTrue(result.isValid());
		assertEquals("MultiPolygon", result.getGeometryType());
		assertEquals(2, result.getNumGeometries());
		assertEquals(2.0, result.getArea(), 1.0e-9);
	}

	@Test
	void merge_withTouchingAreas_returnsSingleMergedArea() {
		Polygon first = createRectangle(0.0, 0.0, 1.0, 1.0);
		Polygon second = createRectangle(1.0, 0.0, 2.0, 1.0);

		Geometry result = merger.merge(List.of(first, second));

		assertFalse(result.isEmpty());
		assertTrue(result.isValid());
		assertEquals(1, result.getNumGeometries());
		assertEquals("Polygon", result.getGeometryType());
		assertEquals(2.0, result.getArea(), 1.0e-9);
	}

	@Test
	void merge_withSingleArea_preservesGeometry() {
		Polygon area = createRectangle(0.0, 0.0, 2.0, 3.0);

		Geometry result = merger.merge(List.of(area));

		assertTrue(result.equalsTopo(area));
		assertEquals(area.getArea(), result.getArea(), 1.0e-9);
	}

	@Test
	void merge_withEmptyCollection_returnsEmptyGeometry() {
		Geometry result = merger.merge(List.of());

		assertTrue(result.isEmpty());
	}

	@Test
	void merge_withOnlyEmptyGeometries_returnsEmptyGeometry() {
		Geometry empty = GEOMETRY_FACTORY.createGeometryCollection();

		Geometry result = merger.merge(List.of(empty, empty));

		assertTrue(result.isEmpty());
	}

	@Test
	void merge_withNullCollection_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> merger.merge(null));
	}

	@Test
	void merge_withNullElement_throwsNullPointerException() {
		assertThrows(NullPointerException.class,
				() -> merger.merge(java.util.Arrays.asList(createRectangle(0.0, 0.0, 1.0, 1.0), null)));
	}

	private Polygon createRectangle(double minX, double minY, double maxX, double maxY) {

		return GEOMETRY_FACTORY.createPolygon(
				new org.locationtech.jts.geom.Coordinate[] { new org.locationtech.jts.geom.Coordinate(minX, minY),
						new org.locationtech.jts.geom.Coordinate(maxX, minY),
						new org.locationtech.jts.geom.Coordinate(maxX, maxY),
						new org.locationtech.jts.geom.Coordinate(minX, maxY),
						new org.locationtech.jts.geom.Coordinate(minX, minY) });
	}
}
