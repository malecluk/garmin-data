package malecluk.garminparser.model.component;

import java.util.List;

/**
 * Collection of heart-rate zones belonging to an activity.
 */
public record HeartRateZones(List<HeartRateZone> zonesList) {

	/**
	 * Finds a heart-rate zone by its actual zone number.
	 *
	 * <p>The supplied number is matched against
	 * {@link HeartRateZone#number()}, not against the position of the zone
	 * in {@code zonesList}.</p>
	 *
	 * @param zoneNumber zone number to find
	 * @return matching zone, or {@code null} if the zone is not present
	 */
	public HeartRateZone get(int zoneNumber) {

		return zonesList.stream().filter(zone -> zone.number().equals(zoneNumber)).findFirst().orElse(null);

	}

	public boolean hasZone(int zoneNumber) {

		return zonesList.stream().anyMatch(zone -> zone.number().equals(zoneNumber));

	}
}
