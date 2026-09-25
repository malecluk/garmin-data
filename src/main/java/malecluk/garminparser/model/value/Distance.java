package malecluk.garminparser.model.value;

/**
 * Represents a distance stored internally in meters.
 */
public record Distance(Double meters) {
	
	/**
	 * Creates a distance from a numeric value expressed in meters.
	 * @param meters distance in meters
	 */
	public Distance(Number meters) {
        this(meters.doubleValue());
    }

	/**
	 * Returns the distance in meters.
	 * @return distance in meters
	 */
    public Double toMeters() {
        return meters;
    }

    /**
     * Returns the distance in kilometers.
     * @return distance in kilometers
     */
    public Double toKilometers() {
        return meters / 1000.0;
    }
}
