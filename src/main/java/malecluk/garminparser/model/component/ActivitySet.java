package malecluk.garminparser.model.component;

import java.time.Duration;
import java.time.Instant;

/**
 * Represents one set recorded during an activity.
 *
 * <p>A set is a Garmin FIT activity segment represented by a
 * {@code SetMesg}. For example, yoga activities can use sets to
 * represent individual yoga positions.</p>
 *
 * @param setTimestamp timestamp for this set
 * @param startTime the start time of the set
 * @param duration the duration of the set
 * @param messageIndex the zero-based index of the set within the FIT file
 * @param setType the Garmin FIT set type
 */
public class ActivitySet {
	
	/**
     * Number identifying the set within the activity.
     * <p>The value corresponds to the Garmin FIT {@code SetMesg.message_index} field.</p>
     */
	private Integer number;
	
	/**
     * Timestamp associated with the set message.
     * <p>This value corresponds to the Garmin FIT {@code SetMesg.timestamp} field.</p>
     */
	private Instant setTimestamp;
	
	/**
     * Start time of the set.
     * <p>This value corresponds to the Garmin FIT {@code SetMesg.start_time} field.</p>
     */
	private Instant startTime;
	
	/**
     * Duration of the set.
     * <p>This value corresponds to the Garmin FIT {@code SetMesg.duration} field.</p>
     */
	private Duration duration;
	
	/**
     * Garmin FIT set type.
     * <p>This value corresponds to the Garmin FIT {@code SetMesg.set_type} field.</p>
     */
	private Integer setType;
		
	//-----------------------

	/**
     * Returns the number identifying this set.
     * @return the set number
     */
	public Integer getNumber() {
		return number;
	}

	/**
     * Sets the number identifying this set.
     * @param number the set number
     */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
     * Returns the timestamp associated with the set message.
     * @return the set timestamp
     */
	public Instant getSetTimestamp() {
		return setTimestamp;
	}

	/**
     * Sets the timestamp associated with the set message.
     * @param setTimestamp the set timestamp
     */
	public void setSetTimestamp(Instant setTimestamp) {
		this.setTimestamp = setTimestamp;
	}
	
	/**
     * Returns the start time of the set.
     * @return the set start time
     */
	public Instant getStartTime() {
		return startTime;
	}

	/**
     * Sets the start time of the set.
     * @param startTime the set start time
     */
	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	/**
     * Returns the duration of the set.
     * @return the set duration
     */
	public Duration getDuration() {
		return duration;
	}

	/**
     * Sets the duration of the set.
     * @param duration the set duration
     */
	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	/**
     * Returns the Garmin FIT set type.
     * @return the set type
     */
	public Integer getSetType() {
		return setType;
	}

	/**
     * Sets the Garmin FIT set type.
     * @param setType the set type
     */
	public void setSetType(Integer setType) {
		this.setType = setType;
	}
}
