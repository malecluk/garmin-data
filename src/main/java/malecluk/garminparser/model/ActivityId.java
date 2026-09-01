package malecluk.garminparser.model;

/**
 * Identifies an activity independently of its source FIT file.
 *
 * <p>The activity ID is generated deterministically from attributes that
 * identify the recorded activity, such as the recording device serial number
 * and the activity start timestamp.</p>
 *
 * @param value the unique activity identifier
 */
public record ActivityId(String value) {
}