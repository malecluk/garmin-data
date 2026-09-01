package malecluk.garminparser.model.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;

class HeartRateZonesTest {

    @Test
    void get_returnsZoneByZoneNumber_notByListIndex() {
        HeartRateZone zone0 = new HeartRateZone(0, null, 88, Duration.ZERO);
        HeartRateZone zone1 = new HeartRateZone(1, 89, 106, Duration.ofMinutes(1));
        HeartRateZone zone4 = new HeartRateZone(4, 142, 159, Duration.ofMinutes(4));
        HeartRateZone zone5 = new HeartRateZone(5, 160, 177, Duration.ofMinutes(5));

        HeartRateZones zones = new HeartRateZones(
                List.of(zone0, zone1, zone4, zone5));

        assertEquals(zone4, zones.get(4));
        assertNull(zones.get(3));
    }

    @Test
    void hasZone_returnsTrueOnlyWhenZoneNumberExists() {
        HeartRateZone zone0 = new HeartRateZone(0, null, 88, Duration.ZERO);
        HeartRateZone zone1 = new HeartRateZone(1, 89, 106, Duration.ofMinutes(1));
        HeartRateZone zone4 = new HeartRateZone(4, 142, 159, Duration.ofMinutes(4));
        HeartRateZone zone5 = new HeartRateZone(5, 160, 177, Duration.ofMinutes(5));

        HeartRateZones zones = new HeartRateZones(
                List.of(zone0, zone1, zone4, zone5));

        assertTrue(zones.hasZone(4));
        assertFalse(zones.hasZone(3));
    }
}

