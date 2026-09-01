package malecluk.garminparser.model.component;

import java.util.List;
import java.util.Objects;

/**
 * Collection of heart-rate zones belonging to an activity.
 */
public record HeartRateZones(List<HeartRateZone> zonesList) {

	/**
	 * Creates a heart-rate zones collection.
	 *
	 * <p>
	 * The supplied list must not be {@code null}. A defensive copy is created using
	 * {@link List#copyOf(List)}, so subsequent changes to the original list cannot
	 * affect this collection and the returned list is unmodifiable.
	 * </p>
	 *
	 * @throws NullPointerException if {@code zonesList} is {@code null}
	 */
	public HeartRateZones {
		zonesList = List.copyOf(Objects.requireNonNull(zonesList, "zonesList must not be null"));
	}

	/**
	 * Finds a heart-rate zone by its actual zone number.
	 *
	 * <p>
	 * The zone number is matched against {@link HeartRateZone#number()}, not
	 * against the position of the zone in the list.
	 * </p>
	 *
	 * @param zoneNumber actual zone number to find
	 * @return the matching heart-rate zone, or {@code null} if no such zone exists
	 */
	public HeartRateZone get(int zoneNumber) {

		return zonesList.stream().filter(zone -> zone.number().equals(zoneNumber)).findFirst().orElse(null);
	}

	/**
	 * Checks whether a heart-rate zone with the specified actual zone number
	 * exists.
	 *
	 * <p>
	 * The number is matched against {@link HeartRateZone#number()}, not against the
	 * position of the zone in the list.
	 * </p>
	 *
	 * @param zoneNumber zone number to look for
	 * @return {@code true} if the zone is present
	 */
	public boolean hasZone(int zoneNumber) {

		return zonesList.stream().anyMatch(zone -> zone.number().equals(zoneNumber));
	}
}
