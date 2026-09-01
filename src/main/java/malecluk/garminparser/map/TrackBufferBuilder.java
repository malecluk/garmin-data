
package malecluk.garminparser.map;

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.springframework.stereotype.Component;

/**
 * Builds a geographic buffer around an activity track.
 *
 * <p>
 * The input track segments are expected to use WGS84 coordinates, with
 * longitude as X and latitude as Y. The segments are transformed into the
 * activity's projected coordinate reference system before buffering.
 * </p>
 *
 * <p>
 * The buffer distance is expressed in metres. The resulting geometry is
 * transformed back to WGS84 so that buffers from different activities can later
 * be combined in a common coordinate reference system.
 * </p>
 */
@Component
public class TrackBufferBuilder {

	private static final Logger log = LogManager.getLogger(TrackBufferBuilder.class);

	private static final double BUFFER_DISTANCE_METRES = 50.0;
	private static final double SIMPLIFICATION_TOLERANCE_METRES = 2.0;

	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

	private final CoordinateTransformer coordinateTransformer;

	public TrackBufferBuilder(CoordinateTransformer coordinateTransformer) {
		this.coordinateTransformer = Objects.requireNonNull(coordinateTransformer,
				"coordinateTransformer must not be null");
	}

	/**
	 * Builds a buffer around the supplied track segments.
	 *
	 * <p>
	 * The same projection must be used for all segments belonging to the activity.
	 * Segments are combined before buffering, so overlapping portions of the track
	 * do not produce separate buffer geometries.
	 * </p>
	 *
	 * @param segments   track segments expressed in WGS84
	 * @param projection projected coordinate reference system for the activity
	 * @return buffered geometry expressed in WGS84; an empty geometry if there are
	 *         no segments
	 * @throws NullPointerException if the segments, projection, or a segment is
	 *                              {@code null}
	 */
	public Geometry build(List<LineString> segments, CoordinateReferenceSystem projection) {

		Objects.requireNonNull(segments, "segments must not be null");
		Objects.requireNonNull(projection, "projection must not be null");

		log.debug("Processing " + segments.size() + " segemnt(s)");

		if (segments.isEmpty()) {
			return new org.locationtech.jts.geom.GeometryFactory().createGeometryCollection();
		}

		for (LineString segment : segments) {
			Objects.requireNonNull(segment, "segments must not contain null elements");
		}

		List<LineString> projectedSegments = segments.stream()
				.map(segment -> (LineString) coordinateTransformer.transformToProjection(segment, projection)).toList();

		MultiLineString projectedTrack = GEOMETRY_FACTORY
				.createMultiLineString(projectedSegments.toArray(LineString[]::new));

		Geometry simplifiedTrack = DouglasPeuckerSimplifier.simplify(projectedTrack, SIMPLIFICATION_TOLERANCE_METRES);

		Geometry projectedBuffer = simplifiedTrack.buffer(BUFFER_DISTANCE_METRES);

		return coordinateTransformer.transformToWgs84(projectedBuffer, projection);
	}
}
