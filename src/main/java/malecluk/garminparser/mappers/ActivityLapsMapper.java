package malecluk.garminparser.mappers;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;

import com.garmin.fit.DateTime;
import com.garmin.fit.LapMesg;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivityLap;
import malecluk.garminparser.model.component.ActivityLaps;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

/**
 * Maps Garmin FIT lap messages to the activity lap domain model.
 */
@Component
public class ActivityLapsMapper {

	/**
	 * Maps FIT lap messages to the activity.
	 *
	 * <p>
	 * If no lap messages are available, the activity keeps its default empty
	 * {@link ActivityLaps} collection.
	 * </p>
	 *
	 * @param activity activity to update
	 * @param messages FIT lap messages
	 */
	public void map(Activity activity, List<LapMesg> messages) {

		if (messages == null || messages.isEmpty()) {
			return;
		}

		ActivityLaps laps = new ActivityLaps();

		for (LapMesg message : messages) {
			laps.add(mapLap(message));
		}

		activity.setLaps(laps);
	}

	/**
	 * Maps a single FIT lap message to an activity lap.
	 *
	 * @param message FIT lap message
	 * @return mapped activity lap
	 */
	private ActivityLap mapLap(LapMesg message) {

		ActivityLap lap = new ActivityLap();

		lap.setNumber(message.getMessageIndex());

		lap.setStartTime(toInstant(message.getStartTime()));
		lap.setEndTime(toInstant(message.getTimestamp()));

		lap.setStartPosition(toPosition(message.getStartPositionLat(), message.getStartPositionLong()));

		lap.setEndPosition(toPosition(message.getEndPositionLat(), message.getEndPositionLong()));

		lap.setElapsedTime(toDuration(message.getTotalElapsedTime()));
		lap.setTimerTime(toDuration(message.getTotalTimerTime()));

		lap.setTotalDistance(toDistance(message.getTotalDistance()));

		lap.setAverageSpeed(message.getAvgSpeed());
		lap.setMaximumSpeed(message.getMaxSpeed());

		lap.setAverageHeartRate(toInteger(message.getAvgHeartRate()));
		lap.setMaximumHeartRate(toInteger(message.getMaxHeartRate()));

		lap.setAverageCadence(toFloat(message.getAvgCadence()));
		lap.setMaximumCadence(toFloat(message.getMaxCadence()));

		lap.setTotalCalories(toInteger(message.getTotalCalories()));

		lap.setTotalAscent(toDistance(message.getTotalAscent()));
		lap.setTotalDescent(toDistance(message.getTotalDescent()));

		lap.setMinimumAltitude(toDouble(message.getMinAltitude()));
		lap.setMaximumAltitude(toDouble(message.getMaxAltitude()));

		lap.setAverageTemperature(toFloat(message.getAvgTemperature()));
		lap.setMinimumTemperature(toFloat(message.getMinTemperature()));
		lap.setMaximumTemperature(toFloat(message.getMaxTemperature()));

		return lap;
	}

	/**
	 * Converts a Garmin FIT timestamp to an application {@link Instant}.
	 *
	 * @param dateTime FIT timestamp
	 * @return converted instant, or {@code null} if the value is missing
	 */
	private Instant toInstant(DateTime dateTime) {

		return dateTime == null ? null : dateTime.getDate().toInstant();
	}

	/**
	 * Converts a FIT duration expressed in seconds to {@link Duration}.
	 *
	 * @param seconds duration in seconds
	 * @return converted duration, or {@code null} if the value is missing
	 */
	private Duration toDuration(Number seconds) {

		return seconds == null ? null : Duration.ofMillis(Math.round(seconds.doubleValue() * 1000));
	}

	/**
	 * Converts a FIT distance expressed in meters to {@link Distance}.
	 *
	 * @param meters distance in meters
	 * @return converted distance, or {@code null} if the value is missing
	 */
	private Distance toDistance(Number meters) {

		return meters == null ? null : new Distance(meters);
	}

	/**
	 * Converts FIT position values from semicircles to degrees.
	 *
	 * @param latitude  latitude in semicircles
	 * @param longitude longitude in semicircles
	 * @return converted position, or {@code null} if both values are missing
	 */
	private Position toPosition(Integer latitude, Integer longitude) {

		if (latitude == null && longitude == null) {
			return null;
		}

		return new Position(toDegrees(latitude), toDegrees(longitude));
	}

	/**
	 * Converts a FIT coordinate from semicircles to degrees.
	 *
	 * @param semicircles coordinate in FIT semicircles
	 * @return coordinate in degrees, or {@code null} if the value is missing
	 */
	private Double toDegrees(Integer semicircles) {

		return semicircles == null ? null : semicircles * (180.0 / 2147483648.0);
	}

	/**
	 * Converts a numeric FIT value to an integer.
	 *
	 * @param value FIT numeric value
	 * @return integer value, or {@code null} if the value is missing
	 */
	private Integer toInteger(Number value) {

		return value == null ? null : value.intValue();
	}

	/**
	 * Converts a numeric FIT value to a float.
	 *
	 * @param value FIT numeric value
	 * @return float value, or {@code null} if the value is missing
	 */
	private Float toFloat(Number value) {

		return value == null ? null : value.floatValue();
	}

	/**
	 * Converts a numeric FIT value to a double.
	 *
	 * @param value FIT numeric value
	 * @return double value, or {@code null} if the value is missing
	 */
	private Double toDouble(Number value) {

		return value == null ? null : value.doubleValue();
	}
}
