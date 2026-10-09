
package malecluk.garminparser.map;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.operation.union.UnaryUnionOp;
import org.springframework.stereotype.Component;

/**
 * Merges the buffered areas of multiple activities into one geometry.
 *
 * <p>
 * All input geometries must be expressed in WGS84, with longitude as X and
 * latitude as Y. The geometries may have been buffered in different projected
 * coordinate reference systems, but must be transformed back to WGS84 before
 * they are passed to this class.
 * </p>
 *
 * <p>
 * Overlapping or touching areas are merged. Disconnected areas remain separate
 * components of the resulting geometry.
 * </p>
 *
 * <p>
 * This class does not transform coordinates or create buffers.
 * </p>
 */
@Component
public class ActivityAreaMerger {

	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

	/**
	 * Merges the supplied activity areas into a single geometry.
	 *
	 * @param activityAreas buffered activity geometries in WGS84
	 * @return the union of all supplied geometries; an empty geometry collection if
	 *         the collection is empty or contains only empty geometries
	 * @throws NullPointerException if the collection or any of its elements is
	 *                              {@code null}
	 */
	public Geometry merge(Collection<? extends Geometry> activityAreas) {
		Objects.requireNonNull(activityAreas, "activityAreas must not be null");

		for (Geometry area : activityAreas) {
			Objects.requireNonNull(area, "activityAreas must not contain null elements");
		}

		List<Geometry> nonEmptyAreas = activityAreas.stream().filter(area -> !area.isEmpty())
				.map(area -> (Geometry) area).toList();

		if (nonEmptyAreas.isEmpty()) {
			return GEOMETRY_FACTORY.createGeometryCollection();
		}

		return UnaryUnionOp.union(nonEmptyAreas);
	}
}
