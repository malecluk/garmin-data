
package malecluk.garminparser.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import org.springframework.stereotype.Component;

import malecluk.garminparser.model.component.ActivityTrack;
import malecluk.garminparser.model.component.TrackPoint;
import malecluk.garminparser.model.value.Position;

/**
 * Builds continuous GPS track segments from an activity track.
 *
 * <p>
 * Points with missing or invalid geographic coordinates split a track into
 * separate segments. Segments containing fewer than two valid points are
 * discarded.
 * </p>
 *
 * <p>
 * Coordinates are represented in WGS84, with longitude as the X coordinate and
 * latitude as the Y coordinate. No distance calculations, simplification,
 * projection, or buffering are performed by this class.
 * </p>
 */
@Component
public class TrackGeometryBuilder {

	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

	/**
	 * Builds continuous line segments from the supplied activity track.
	 *
	 * @param track activity track to convert
	 * @return immutable list of continuous line segments; never {@code null}
	 * @throws NullPointerException if {@code track} is {@code null}
	 */
	public List<LineString> build(ActivityTrack track) {
		Objects.requireNonNull(track, "track must not be null");

		List<LineString> segments = new ArrayList<>();
		List<Coordinate> currentSegment = new ArrayList<>();

		for (TrackPoint point : track.getPoints()) {
			Position position = point.position();

			if (!isValid(position)) {
				addSegmentIfUsable(segments, currentSegment);
				currentSegment.clear();
				continue;
			}

			Coordinate coordinate = new Coordinate(position.longitude(), position.latitude());

			if (crossesAntimeridian(currentSegment, coordinate)) {
				addSegmentIfUsable(segments, currentSegment);
				currentSegment.clear();
			}

			currentSegment.add(coordinate);
		}

		addSegmentIfUsable(segments, currentSegment);

		return List.copyOf(segments);
	}

	private boolean isValid(Position position) {
		if (position == null || position.latitude() == null || position.longitude() == null) {
			return false;
		}

		double latitude = position.latitude();
		double longitude = position.longitude();

		return Double.isFinite(latitude) && Double.isFinite(longitude) && latitude >= -90.0 && latitude <= 90.0
				&& longitude >= -180.0 && longitude <= 180.0;
	}

	private boolean crossesAntimeridian(List<Coordinate> currentSegment, Coordinate nextCoordinate) {

		if (currentSegment.isEmpty()) {
			return false;
		}

		Coordinate previousCoordinate = currentSegment.get(currentSegment.size() - 1);

		return Math.abs(nextCoordinate.getX() - previousCoordinate.getX()) > 180.0;
	}

	private void addSegmentIfUsable(List<LineString> segments, List<Coordinate> coordinates) {

		if (coordinates.size() < 2) {
			return;
		}

		Coordinate[] coordinateArray = coordinates.toArray(Coordinate[]::new);

		segments.add(GEOMETRY_FACTORY.createLineString(coordinateArray));
	}
}
