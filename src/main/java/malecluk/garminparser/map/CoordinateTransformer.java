
package malecluk.garminparser.map;

import java.util.Objects;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;

import malecluk.garminparser.model.value.Position;

/**
 * Transforms geometries between WGS84 and a suitable UTM projection.
 *
 * <p>
 * A projection is selected from the reference position of an activity. The
 * selected projection should then be reused for all geometries belonging to
 * that activity.
 * </p>
 *
 * <p>
 * Geometries in WGS84 use longitude as the X coordinate and latitude as the Y
 * coordinate. Projected geometries use the coordinate system's linear units,
 * which for UTM are metres.
 * </p>
 *
 * <p>
 * This class does not create buffers or combine geometries.
 * </p>
 */
@Component
public class CoordinateTransformer {

	private static final int FIRST_UTM_ZONE = 1;
	private static final int LAST_UTM_ZONE = 60;

	private static final double MIN_UTM_LATITUDE = -80.0;
	private static final double MAX_UTM_LATITUDE = 84.0;

	private static final int NORTHERN_UTM_EPSG_BASE = 32600;
	private static final int SOUTHERN_UTM_EPSG_BASE = 32700;

	private static final CoordinateReferenceSystem WGS84 = createWgs84();

	/**
	 * Determines the UTM projection appropriate for an activity's reference
	 * position.
	 *
	 * <p>
	 * The UTM zone is calculated from longitude. The northern or southern
	 * hemisphere is selected from latitude.
	 * </p>
	 *
	 * @param referencePosition reference position of the activity in WGS84
	 * @return the selected UTM coordinate reference system
	 * @throws NullPointerException     if the position or either coordinate is null
	 * @throws IllegalArgumentException if the coordinates are invalid or the
	 *                                  latitude is outside the UTM latitude range
	 * @throws IllegalStateException    if the coordinate reference system cannot be
	 *                                  resolved
	 */
	public CoordinateReferenceSystem determineProjection(Position referencePosition) {
		Objects.requireNonNull(referencePosition, "referencePosition must not be null");

		Double latitude = Objects.requireNonNull(referencePosition.latitude(), "latitude must not be null");
		Double longitude = Objects.requireNonNull(referencePosition.longitude(), "longitude must not be null");

		validateCoordinates(latitude, longitude);

		int zone = calculateUtmZone(longitude);

		int epsgBase = latitude >= 0.0 ? NORTHERN_UTM_EPSG_BASE : SOUTHERN_UTM_EPSG_BASE;

		return decodeEpsg(epsgBase + zone);
	}

	/**
	 * Transforms a geometry from WGS84 into the specified projected coordinate
	 * reference system.
	 *
	 * <p>
	 * The input geometry must contain WGS84 coordinates, with longitude as X and
	 * latitude as Y. The input geometry is not modified.
	 * </p>
	 *
	 * @param geometry   geometry expressed in WGS84
	 * @param projection target projected coordinate reference system
	 * @return transformed geometry in the target projection
	 * @throws NullPointerException  if the geometry or projection is null
	 * @throws IllegalStateException if the transformation fails
	 */
	public Geometry transformToProjection(Geometry geometry, CoordinateReferenceSystem projection) {

		Objects.requireNonNull(geometry, "geometry must not be null");
		Objects.requireNonNull(projection, "projection must not be null");

		return transform(geometry, WGS84, projection);
	}

	/**
	 * Transforms a geometry from a projected coordinate reference system back into
	 * WGS84.
	 *
	 * <p>
	 * The input geometry is not modified. The returned geometry uses longitude as X
	 * and latitude as Y.
	 * </p>
	 *
	 * @param geometry   geometry expressed in the specified projection
	 * @param projection source projected coordinate reference system
	 * @return transformed geometry in WGS84
	 * @throws NullPointerException  if the geometry or projection is null
	 * @throws IllegalStateException if the transformation fails
	 */
	public Geometry transformToWgs84(Geometry geometry, CoordinateReferenceSystem projection) {

		Objects.requireNonNull(geometry, "geometry must not be null");
		Objects.requireNonNull(projection, "projection must not be null");

		return transform(geometry, projection, WGS84);
	}

	private Geometry transform(Geometry geometry, CoordinateReferenceSystem source, CoordinateReferenceSystem target) {

		try {
			MathTransform mathTransform = CRS.findMathTransform(source, target);
			return JTS.transform(geometry, mathTransform);
		} catch (FactoryException | TransformException exception) {
			throw new IllegalStateException("Failed to transform geometry between coordinate reference systems",
					exception);
		}
	}

	private int calculateUtmZone(double longitude) {
		int zone = (int) Math.floor((longitude + 180.0) / 6.0) + 1;

		// Longitude +180 degrees belongs to the final UTM zone.
		return Math.max(FIRST_UTM_ZONE, Math.min(LAST_UTM_ZONE, zone));
	}

	private void validateCoordinates(double latitude, double longitude) {
		if (!Double.isFinite(latitude) || latitude < -90.0 || latitude > 90.0) {
			throw new IllegalArgumentException("Invalid latitude: " + latitude);
		}

		if (!Double.isFinite(longitude) || longitude < -180.0 || longitude > 180.0) {
			throw new IllegalArgumentException("Invalid longitude: " + longitude);
		}

		if (latitude < MIN_UTM_LATITUDE || latitude > MAX_UTM_LATITUDE) {
			throw new IllegalArgumentException("Latitude outside the supported UTM range: " + latitude);
		}
	}

	private static CoordinateReferenceSystem createWgs84() {
		try {
			// Force longitude-first axis order to match JTS coordinates.
			return CRS.decode("EPSG:4326", true);
		} catch (FactoryException exception) {
			throw new ExceptionInInitializerError(exception);
		}
	}

	private CoordinateReferenceSystem decodeEpsg(int epsgCode) {
		try {
			return CRS.decode("EPSG:" + epsgCode, true);
		} catch (FactoryException exception) {
			throw new IllegalStateException("Failed to resolve coordinate reference system EPSG:" + epsgCode,
					exception);
		}
	}
}
