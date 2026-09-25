package malecluk.garminparser.model.component;

import java.util.List;

public record HeartRateZones(List<HeartRateZone> zonesList) {

	public HeartRateZone get(int zoneNumber) {

		return zonesList.stream().filter(zone -> zone.number().equals(zoneNumber)).findFirst().orElse(null);

	}

	public boolean hasZone(int zoneNumber) {

		return zonesList.stream().anyMatch(zone -> zone.number().equals(zoneNumber));

	}
}
