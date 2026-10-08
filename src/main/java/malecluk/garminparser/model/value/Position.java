package malecluk.garminparser.model.value;

/**
 * Represents a geographic position using latitude and longitude.
 *
 * <p>
 * Both coordinates are expressed in degrees and may be {@code null}. A
 * {@code null} coordinate means that the corresponding coordinate is
 * unavailable and must not be interpreted as a geographic coordinate with value
 * zero.
 * </p>
 *
 * <p>
 * A position is considered unavailable only when both coordinates are
 * {@code null}. In that case, {@link #fromSemicircles(Integer, Integer)}
 * returns {@code null}.
 * </p>
 *
 * <p>
 * A position with latitude or longitude equal to {@code 0.0} is a valid
 * geographic coordinate and is therefore distinct from a position containing a
 * {@code null} coordinate.
 * </p>
 *
 * @param latitude  geographic latitude in degrees, or {@code null} if
 *                  unavailable
 * @param longitude geographic longitude in degrees, or {@code null} if
 *                  unavailable
 */
public record Position(Double latitude, Double longitude) {
	
	private static final double SEMICIRCLES_TO_DEGREES = 180.0 / 2147483648.0;

	/**
	 * Creates a position from FIT coordinates in semicircles.
	 *
	 * <p>
	 * FIT coordinates are converted to degrees. If both coordinates are
	 * {@code null}, no position is available and this method returns {@code null}.
	 * If only one coordinate is {@code null}, a position is created with the
	 * corresponding coordinate remaining {@code null}.
	 * </p>
	 *
	 * @param latitude  latitude in FIT semicircles, or {@code null} if unavailable
	 * @param longitude longitude in FIT semicircles, or {@code null} if unavailable
	 * @return position in degrees, or {@code null} if both coordinates are missing
	 */
	public static Position fromSemicircles(Integer latitude, Integer longitude) {

		if (latitude == null && longitude == null) {
			return null;
		}

		return new Position(toDegrees(latitude), toDegrees(longitude));
	}

	/**
	 * Converts a FIT coordinate from semicircles to degrees.
	 *
	 * <p>FIT stores geographic coordinates as signed 32-bit semicircle values.
	 * One full circle corresponds to {@code 2^32} semicircles, therefore
	 * one semicircle corresponds to {@code 180 / 2^31} degrees.</p>
	 * 
	 * <p>To get degrees from signed 32-bit semicircle values do this {@code semicircles * (180.0 / 2^31)}.
	 * 2^31 = 2147483648 - that's from where constant comes from.</p>
	 *
	 * @param semicircles coordinate in FIT semicircles
	 * @return coordinate in degrees, or {@code null} if the value is missing
	 */
	private static Double toDegrees(Integer semicircles) {
		return semicircles == null ? null : semicircles * SEMICIRCLES_TO_DEGREES;
	}
}
