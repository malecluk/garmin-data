package malecluk.garminparser.model;

/**
 * Identifies the exact source FIT file from which an activity was parsed.
 *
 * <p>The hash is calculated from the complete contents of the source FIT file.
 * It can therefore be used to recognize the same file when it is imported
 * more than once.</p>
 *
 * @param value the hash value of the source FIT file
 */
public record SourceFileHash(String value) {
}