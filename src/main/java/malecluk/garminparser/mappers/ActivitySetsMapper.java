package malecluk.garminparser.mappers;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;

import com.garmin.fit.DateTime;
import com.garmin.fit.SetMesg;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivitySet;
import malecluk.garminparser.model.component.ActivitySets;

/**
 * Maps Garmin FIT set messages to the activity set domain model.
 */
@Component
public class ActivitySetsMapper {

	/**
	 * Maps FIT set messages to the activity.
	 *
	 * <p>
	 * If no set messages are available, the activity keeps its default empty
	 * {@link ActivitySets} collection.
	 * </p>
	 *
	 * @param activity activity to update
	 * @param messages FIT set messages
	 */
	public void map(Activity activity, List<SetMesg> messages) {

		if (messages == null || messages.isEmpty()) {
			return;
		}

		ActivitySets sets = new ActivitySets();

		for (SetMesg message : messages) {
			sets.add(mapSet(message));
		}

		activity.setSets(sets);
	}

	/**
	 * Maps a single FIT set message to an activity set.
	 * 
	 * @param message FIT set message
	 * @return mapped activity set
	 */
	private ActivitySet mapSet(SetMesg message) {

		ActivitySet set = new ActivitySet();

		set.setNumber(message.getMessageIndex());

		set.setSetTimestamp(toInstant(message.getTimestamp()));
		set.setStartTime(toInstant(message.getStartTime()));
		set.setDuration(toDuration(message.getDuration()));
		set.setSetType(message.getSetType() != null ? message.getSetType().intValue() : null);

		return set;
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
}
