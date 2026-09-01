package malecluk.garminparser.mappers;

import com.garmin.fit.RecordMesg;

import malecluk.garminparser.model.component.ActivityTrack;
import malecluk.garminparser.model.component.TrackPoint;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Maps Garmin FIT {@link RecordMesg} messages to an {@link ActivityTrack}.
 *
 * <p>
 * The resulting track points are ordered chronologically by their timestamp,
 * regardless of the order of the record messages in the FIT file.
 * </p>
 */
@Component
public class TrackPointMapper {

	private static final Logger log = LogManager.getLogger(TrackPointMapper.class);

	/**
	 * Maps record messages to an activity track and orders the track points
	 * chronologically.
	 *
	 * @param recordMessages FIT record messages to map
	 * @return activity track containing the mapped points ordered by timestamp
	 * @throws NullPointerException if {@code recordMessages} is {@code null}
	 */
	public ActivityTrack map(List<RecordMesg> recordMessages) {
		Objects.requireNonNull(recordMessages, "recordMessages must not be null");

		List<TrackPoint> points = recordMessages.stream().filter(recordMessage -> {
			Objects.requireNonNull(recordMessage, "recordMessage must not be null");

			if (recordMessage.getPositionLat() == null && recordMessage.getPositionLong() == null) {
				log.warn("Skipping FIT record without GPS coordinates.");
				return false;
			}

			return true;
		}).map(this::map).sorted(Comparator.comparing(TrackPoint::timestamp)).toList();

		return new ActivityTrack(points);
	}

	private TrackPoint map(RecordMesg recordMessage) {
		Objects.requireNonNull(recordMessage, "recordMessage must not be null");
		Objects.requireNonNull(recordMessage.getTimestamp(), "recordMessage timestamp must not be null");

		return new TrackPoint(recordMessage.getTimestamp().getDate().toInstant(),
				Position.fromSemicircles(recordMessage.getPositionLat(), recordMessage.getPositionLong()),
				recordMessage.getDistance() == null ? null : new Distance(recordMessage.getDistance()),
				recordMessage.getEnhancedAltitude());
	}
}