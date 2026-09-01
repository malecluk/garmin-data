package malecluk.garminparser.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.garmin.fit.MesgNum;
import com.garmin.fit.TimeInZoneMesg;

import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.component.HeartRateZone;
import malecluk.garminparser.model.component.HeartRateZones;

class HeartRateZonesMapperTest {

	private HeartRateZonesMapper mapper;
	private Walking activity;

	@BeforeEach
	void setUp() {
		mapper = new HeartRateZonesMapper();
		activity = new Walking();
	}

	@Test
	void shouldMapSessionHeartRateZones() {
		TimeInZoneMesg mesg = createTimeInZoneMesg(
				MesgNum.SESSION,
				new short[] { 89, 107, 125, 142, 160, 178 },
				new float[] { 1960.299f, 627.694f, 63.0f, 0.0f, 0.0f, 0.0f });

		mapper.map(activity, mesg);

		HeartRateZones zones = activity.getHeartRateZones();

		assertEquals(6, zones.zonesList().size());

		HeartRateZone zone0 = zones.get(0);
		assertEquals(0, zone0.number());
		assertNull(zone0.minBpm());
		assertEquals(Integer.valueOf(88), zone0.maxBpm());
		assertEquals(Duration.ofMillis(1_960_299), zone0.time());

		HeartRateZone zone1 = zones.get(1);
		assertEquals(1, zone1.number());
		assertEquals(Integer.valueOf(89), zone1.minBpm());
		assertEquals(Integer.valueOf(106), zone1.maxBpm());
		assertEquals(Duration.ofMillis(627_694), zone1.time());

		HeartRateZone zone2 = zones.get(2);
		assertEquals(2, zone2.number());
		assertEquals(Integer.valueOf(107), zone2.minBpm());
		assertEquals(Integer.valueOf(124), zone2.maxBpm());
		assertEquals(Duration.ofSeconds(63), zone2.time());

		HeartRateZone zone3 = zones.get(3);
		assertEquals(3, zone3.number());
		assertEquals(Integer.valueOf(125), zone3.minBpm());
		assertEquals(Integer.valueOf(141), zone3.maxBpm());
		assertEquals(Duration.ZERO, zone3.time());

		HeartRateZone zone4 = zones.get(4);
		assertEquals(4, zone4.number());
		assertEquals(Integer.valueOf(142), zone4.minBpm());
		assertEquals(Integer.valueOf(159), zone4.maxBpm());
		assertEquals(Duration.ZERO, zone4.time());

		HeartRateZone zone5 = zones.get(5);
		assertEquals(5, zone5.number());
		assertEquals(Integer.valueOf(160), zone5.minBpm());
		assertEquals(Integer.valueOf(177), zone5.maxBpm());
		assertEquals(Duration.ZERO, zone5.time());
	}

	@Test
	void shouldIgnoreLapHeartRateZones() {
		TimeInZoneMesg mesg = createTimeInZoneMesg(
				MesgNum.LAP,
				new short[] { 89 },
				new float[] { 100.0f });

		mapper.map(activity, mesg);

		assertNull(activity.getHeartRateZones());
	}

	@Test
	void shouldConvertSecondsToDuration() {
		TimeInZoneMesg mesg = createTimeInZoneMesg(
				MesgNum.SESSION,
				new short[] { 89 },
				new float[] { 63.0f });

		mapper.map(activity, mesg);

		assertEquals(
				Duration.ofSeconds(63),
				activity.getHeartRateZones().get(0).time());
	}

	@Test
	void shouldPreserveFractionalSeconds() {
		TimeInZoneMesg mesg = createTimeInZoneMesg(
				MesgNum.SESSION,
				new short[] { 89 },
				new float[] { 63.123f });

		mapper.map(activity, mesg);

		assertEquals(
				Duration.ofMillis(63_123),
				activity.getHeartRateZones().get(0).time());
	}

	@Test
	void shouldCreateFirstZoneWithoutMinimumHeartRate() {
		TimeInZoneMesg mesg = createTimeInZoneMesg(
				MesgNum.SESSION,
				new short[] { 89 },
				new float[] { 100.0f });

		mapper.map(activity, mesg);

		HeartRateZone zone = activity.getHeartRateZones().get(0);

		assertNull(zone.minBpm());
		assertEquals(Integer.valueOf(88), zone.maxBpm());
	}

	@Test
	void shouldCreateLastZoneWithoutMaximumHeartRate() {
		TimeInZoneMesg mesg = createTimeInZoneMesg(
				MesgNum.SESSION,
				new short[] { 89, 107, 125, 142, 160, 178 },
				new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f });

		mapper.map(activity, mesg);

		HeartRateZone zone = activity.getHeartRateZones().get(5);

		assertEquals(Integer.valueOf(160), zone.minBpm());
		assertEquals(Integer.valueOf(177), zone.maxBpm());
	}

	private TimeInZoneMesg createTimeInZoneMesg(
			int referenceMesg,
			short[] boundaries,
			float[] times) {

		TimeInZoneMesg mesg = new TimeInZoneMesg();

		mesg.setReferenceMesg(referenceMesg);

		for (int i = 0; i < boundaries.length; i++) {
			mesg.setHrZoneHighBoundary(i, boundaries[i]);
		}

		for (int i = 0; i < times.length; i++) {
			mesg.setTimeInHrZone(i, times[i]);
		}

		return mesg;
	}
}
