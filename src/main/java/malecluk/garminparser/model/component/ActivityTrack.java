package malecluk.garminparser.model.component;

import java.util.List;

/**
 * Represents the ordered collection of track points forming an activity route.
 * <p>
 * The points are stored in chronological order, so their order represents the
 * time progression of the activity track.
 * </p>
 * <p>
 * The collection is immutable. The list supplied to the constructor is
 * defensively copied, and the list returned by {@link #getPoints()} cannot be
 * modified.
 * </p>
 */
public class ActivityTrack {

	private final List<TrackPoint> points;

	/**
	 * Creates an activity track from the given ordered track points.
	 *
	 * @param points
	 *            track points in chronological order
	 */
	public ActivityTrack(List<TrackPoint> points) {
		this.points = List.copyOf(points);
	}

	/**
	 * Returns the immutable list of track points in chronological order.
	 *
	 * @return ordered, immutable track points
	 */
	public List<TrackPoint> getPoints() {
		return points;
	}
}
