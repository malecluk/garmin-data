package malecluk.garminparser.model;

/**
 * Contains the identifiers associated with an activity.
 *
 * <p>The activity identifier identifies the recorded activity itself, while
 * the source file hash identifies the exact FIT file from which the activity
 * was parsed.</p>
 *
 * @param activityId identifier of the activity
 * @param sourceFileHash hash of the source FIT file
 */
public record ActivityIds(
		ActivityId activityId,
		SourceFileHash sourceFileHash) {
}