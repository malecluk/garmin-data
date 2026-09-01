package malecluk.garminparser.model.value;

import java.util.Objects;

/**
 * Represents a distance stored internally in meters.
 */
public record Distance(Double meters) implements Comparable<Distance> {

	/**
	 * A distance of zero meters.
	 */
	public static final Distance ZERO = new Distance(0.0);

	/**
	 * TODO add me
	 * @param meters
	 */
	public Distance {
	    Objects.requireNonNull(meters, "meters must not be null");
	}
	
	/**
	 * Creates a distance from a numeric value expressed in meters.
	 * 
	 * @param meters distance in meters
	 */
	public Distance(Number meters) {
		this(meters.doubleValue());
	}

	/**
	 * Returns the distance in meters.
	 * 
	 * @return distance in meters
	 */
	public Double toMeters() {
		return meters;
	}

	/**
	 * Returns the distance in kilometers.
	 * 
	 * @return distance in kilometers
	 */
	public Double toKilometers() {
		return meters / 1000.0;
	}

	/**
	 * Returns a new distance created by adding the specified distance to this
	 * distance.
	 *
	 * @param add distance to add
	 * @return new distance representing the sum of this distance and the specified
	 *         distance
	 * @throws NullPointerException if the specified distance is {@code null}
	 */
	public Distance add(Distance add) {
		Objects.requireNonNull(add, "add must not be null");
		return new Distance(meters + add.meters());
	}

	/**
	 * Returns a new distance created by subtracting the specified distance from
	 * this distance.
	 *
	 * @param subtract distance to subtract
	 * @return new distance representing the difference between this distance and
	 *         the specified distance
	 * @throws NullPointerException if the specified distance is {@code null}
	 */
	public Distance minus(Distance subtract) {
		Objects.requireNonNull(subtract, "subtract must not be null");
		return new Distance(meters - subtract.meters());
	}

	/**
	 * Returns whether this distance is negative.
	 *
	 * @return {@code true} if the distance is less than zero, otherwise
	 *         {@code false}
	 */
	public boolean isNegative() {
		return meters < 0;
	}

	/**
	 * Compares this distance with the specified distance based on their values in
	 * meters.
	 *
	 * @param other distance to compare with
	 * @return a negative integer, zero, or a positive integer as this distance is
	 *         less than, equal to, or greater than the specified distance
	 * @throws NullPointerException if the specified distance is {@code null}
	 */
	@Override
	public int compareTo(Distance other) {
		Objects.requireNonNull(other, "other must not be null");

		return Double.compare(meters, other.meters());
	}
}
