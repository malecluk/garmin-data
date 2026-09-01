package malecluk.garminparser.model.component;

import java.util.List;

/**
 * Collection of heart-rate zones belonging to an activity.
 */
public record HeartRateZones(List<HeartRateZone> zonesList) {

	/**
	 * Finds a heart-rate zone by its actual zone number.
	 *
	 * <p>The zone number is matched against {@link HeartRateZone#number()},
	 * not against the position of the zone in the list.</p>
	 *
	 * @param zoneNumber actual zone number to find
	 * @return the matching heart-rate zone, or {@code null} if no such zone exists
	 */
	public HeartRateZone get(int zoneNumber) {

		return zonesList.stream().filter(zone -> zone.number().equals(zoneNumber)).findFirst().orElse(null);
	}

	/**
	 * Checks whether a heart-rate zone with the specified actual zone number exists.
	 *
	 * <p>The number is matched against {@link HeartRateZone#number()}, not against
	 * the position of the zone in the list.</p>
	 *
	 * @param zoneNumber zone number to look for
	 * @return {@code true} if the zone is present
	 */
	public boolean hasZone(int zoneNumber) {

		return zonesList.stream().anyMatch(zone -> zone.number().equals(zoneNumber));
	}
}
