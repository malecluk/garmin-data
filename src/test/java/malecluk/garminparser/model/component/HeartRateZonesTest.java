package malecluk.garminparser.model.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class HeartRateZonesTest {

	@Test
	void get_returnsZoneByZoneNumber_notByListIndex() {
		HeartRateZone zone0 = new HeartRateZone(0, null, 88, Duration.ZERO);
		HeartRateZone zone1 = new HeartRateZone(1, 89, 106, Duration.ofMinutes(1));
		HeartRateZone zone4 = new HeartRateZone(4, 142, 159, Duration.ofMinutes(4));
		HeartRateZone zone5 = new HeartRateZone(5, 160, 177, Duration.ofMinutes(5));

		HeartRateZones zones = new HeartRateZones(List.of(zone0, zone1, zone4, zone5));

		assertEquals(zone4, zones.get(4));
		assertNull(zones.get(3));
	}

	@Test
	void hasZone_returnsTrueOnlyWhenZoneNumberExists() {
		HeartRateZone zone0 = new HeartRateZone(0, null, 88, Duration.ZERO);
		HeartRateZone zone1 = new HeartRateZone(1, 89, 106, Duration.ofMinutes(1));
		HeartRateZone zone4 = new HeartRateZone(4, 142, 159, Duration.ofMinutes(4));
		HeartRateZone zone5 = new HeartRateZone(5, 160, 177, Duration.ofMinutes(5));

		HeartRateZones zones = new HeartRateZones(List.of(zone0, zone1, zone4, zone5));

		assertTrue(zones.hasZone(4));
		assertFalse(zones.hasZone(3));
	}

	@Test
	void constructor_makesDefensiveCopyOfList() {
		HeartRateZone zone = new HeartRateZone(1, 100, 120, Duration.ofMinutes(1));
		List<HeartRateZone> source = new ArrayList<>();
		source.add(zone);

		HeartRateZones zones = new HeartRateZones(source);

		source.clear();

		assertEquals(List.of(zone), zones.zonesList());
	}

	@Test
	void zonesList_isUnmodifiable() {
		HeartRateZone zone = new HeartRateZone(1, 100, 120, Duration.ofMinutes(1));
		HeartRateZones zones = new HeartRateZones(List.of(zone));

		assertThrows(UnsupportedOperationException.class, () -> zones.zonesList().add(zone));
	}

	@Test
	void constructor_withNullList_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> new HeartRateZones(null));
	}
}
