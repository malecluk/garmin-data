package malecluk.garminparser.model.component;

import java.time.Instant;
import java.util.Objects;

import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

/**
 * Represents a single point in an activity track.
 * <p>
 * A track point contains the timestamp, geographical position, cumulative
 * distance and enhanced altitude recorded at a specific point during the
 * activity.
 * </p>
 */
public record TrackPoint(Instant timestamp, Position position, Distance distance, Float elevation) {

	/**
	 * Creates a track point.
	 *
	 * @param timestamp time when the point was recorded
	 * @param position  geographical position of the point
	 * @param distance  cumulative distance recorded at this point
	 * @param elevation enhanced altitude recorded at this point
	 */
	public TrackPoint {
		Objects.requireNonNull(timestamp, "timestamp must not be null");
		Objects.requireNonNull(position, "position must not be null");
	}
}
