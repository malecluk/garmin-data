package malecluk.garminparser.model.component;

import java.util.List;

public record HeartRateZones(
	List<HeartRateZone> zonesList
) {

    public HeartRateZone get(int zoneNumber) {
        return zonesList.get(zoneNumber);
    }
}
