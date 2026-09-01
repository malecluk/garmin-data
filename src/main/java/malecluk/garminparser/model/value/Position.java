package malecluk.garminparser.model.value;

/**
 * Represents a geographic position using latitude and longitude.
 *
 * <p>Both coordinates may be {@code null}. A {@code null} coordinate means
 * that the corresponding coordinate is unavailable and must not be
 * interpreted as a geographic coordinate with value zero.</p>
 *
 * <p>In particular, a position with latitude and longitude equal to
 * {@code 0.0} is a valid coordinate and is therefore distinct from a
 * position containing {@code null}.</p>
 *
 * @param latitude geographic latitude in degrees
 * @param longitude geographic longitude in degrees
 */
public record Position(
	Double latitude, 
	Double longitude) 
{}
