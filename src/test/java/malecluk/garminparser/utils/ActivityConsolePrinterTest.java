package malecluk.garminparser.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;
import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.SubSport;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Breathing;
import malecluk.garminparser.model.activities.FloorClimbing;
import malecluk.garminparser.model.activities.Hiking;
import malecluk.garminparser.model.activities.Meditation;
import malecluk.garminparser.model.activities.Rucking;
import malecluk.garminparser.model.activities.Running;
import malecluk.garminparser.model.activities.TrainingActivity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.activities.Yoga;
import malecluk.garminparser.model.component.ActivityDeviceInfo;
import malecluk.garminparser.model.component.ActivityElevationData;
import malecluk.garminparser.model.component.ActivityLap;
import malecluk.garminparser.model.component.ActivityLaps;
import malecluk.garminparser.model.component.ActivitySet;
import malecluk.garminparser.model.component.ActivitySets;
import malecluk.garminparser.model.component.HeartRateZone;
import malecluk.garminparser.model.component.HeartRateZones;
import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

class ActivityConsolePrinterTest {

	private ActivityConsolePrinter printer;
	private final ActivityIds activityIds = new ActivityIds(new ActivityId("12345"), new SourceFileHash("67890"));

	@BeforeEach
	void setUp() {
		printer = new ActivityConsolePrinter();
	}

	@Test
	void toConsoleString_coversAllActivitySectionsAndBranches() {
		Activity activity = createBaseActivity();

		ActivityDeviceInfo deviceInfo = new ActivityDeviceInfo();
		deviceInfo.setManufacturer(1);
		deviceInfo.setProduct(1234);
		deviceInfo.setSerialNumber(987654321L);
		activity.setDeviceInfo(deviceInfo);

		ActivityElevationData elevationData = new ActivityElevationData();
		elevationData.setTotalAscent(new Distance(123.4));
		elevationData.setTotalDescent(new Distance(98.7));
		activity.setElevationData(elevationData);

		activity.setHeartRateZones(new HeartRateZones(java.util.List.of(
				new HeartRateZone(0, 100, 119, Duration.ofSeconds(12)),
				new HeartRateZone(2, 140, 159, Duration.ofSeconds(34))
		)));

		ActivityLaps laps = createLaps();
		activity.setLaps(laps);

		/*
		 * Walking also covers OutdoorMovingActivity.
		 * Positive speed exercises the normal pace conversion branch.
		 */
		Walking walking = new Walking(activityIds);
		copyBaseData(activity, walking);
		walking.setEnhancedAvgSpeed(1.5f);
		walking.setTotalDistance(new Distance(1234.5));
		walking.setTotalStrides(4321L);
		walking.setDeviceInfo(deviceInfo);
		walking.setElevationData(elevationData);
		walking.setHeartRateZones(activity.getHeartRateZones());
		walking.setLaps(laps);

		String walkingOutput = printer.toConsoleString(walking);

		assertAll(
				() -> assertTrue(walkingOutput.contains("BaseActivity results:")),
				() -> assertTrue(walkingOutput.contains("sport:")),
				() -> assertTrue(walkingOutput.contains("subSport:")),
				() -> assertTrue(walkingOutput.contains("sportProfileName:")),
				() -> assertTrue(walkingOutput.contains("startTime:")),
				() -> assertTrue(walkingOutput.contains("timestamp:")),
				() -> assertTrue(walkingOutput.contains("fileCreationTime:")),
				() -> assertTrue(walkingOutput.contains("totalElapsedTime:")),
				() -> assertTrue(walkingOutput.contains("totalTimerTime:")),

				() -> assertTrue(walkingOutput.contains("ActivityElevationData:")),
				() -> assertTrue(walkingOutput.contains("totalAscent:")),
				() -> assertTrue(walkingOutput.contains("totalDescent:")),

				() -> assertTrue(walkingOutput.contains("ActivityDeviceInfo:")),
				() -> assertTrue(walkingOutput.contains("manufacturer:")),
				() -> assertTrue(walkingOutput.contains("product:")),
				() -> assertTrue(walkingOutput.contains("serialNumber:")),

				() -> assertTrue(walkingOutput.contains("HeartRateZones results:")),
				() -> assertTrue(walkingOutput.contains("0: min=100")),
				() -> assertTrue(walkingOutput.contains("2: min=140")),

				() -> assertTrue(walkingOutput.contains("Moving results:")),
				() -> assertTrue(walkingOutput.contains("enhancedAvgSpeed:")),
				() -> assertTrue(walkingOutput.contains("totalDistance:")),
				() -> assertTrue(walkingOutput.contains("CMP: Avg. pace:")),

				() -> assertTrue(walkingOutput.contains("Walking results:")),
				() -> assertTrue(walkingOutput.contains("totalStrides:      4321")),
				() -> assertTrue(walkingOutput.contains("CMP: total steps:  8642")),

				() -> assertTrue(walkingOutput.contains("ActivityLaps results:")),
				() -> assertTrue(walkingOutput.contains("parameter")),
				() -> assertTrue(walkingOutput.contains("lap0")),
				() -> assertTrue(walkingOutput.contains("lap1")),
				() -> assertTrue(walkingOutput.contains("startPosition")),
				() -> assertTrue(walkingOutput.contains("number")),
				() -> assertTrue(walkingOutput.contains("minimumAltitude")),
				() -> assertTrue(walkingOutput.contains("maximumAltitude")),
				() -> assertTrue(walkingOutput.contains("averageTemperature")),
				() -> assertTrue(walkingOutput.contains("minimumTemperature")),
				() -> assertTrue(walkingOutput.contains("maximumTemperature"))
		);

		/*
		 * Exercise convertSpeedToPace(null).
		 */
		Walking walkingWithoutSpeed = new Walking(activityIds);
		copyBaseData(activity, walkingWithoutSpeed);
		walkingWithoutSpeed.setEnhancedAvgSpeed(null);
		walkingWithoutSpeed.setTotalDistance(new Distance(100));

		String nullSpeedOutput = printer.toConsoleString(walkingWithoutSpeed);

		assertTrue(nullSpeedOutput.contains("--:-- min/km"));

		/*
		 * Exercise convertSpeedToPace(speed <= 0).
		 */
		Walking walkingWithZeroSpeed = new Walking(activityIds);
		copyBaseData(activity, walkingWithZeroSpeed);
		walkingWithZeroSpeed.setEnhancedAvgSpeed(0.0f);
		walkingWithZeroSpeed.setTotalDistance(new Distance(100));

		String zeroSpeedOutput = printer.toConsoleString(walkingWithZeroSpeed);

		assertTrue(zeroSpeedOutput.contains("--:-- min/km"));

		/*
		 * Exercise the null branches of elevation/device/heart-rate/laps.
		 */
		Activity minimalActivity = createBaseActivity();
		minimalActivity.setElevationData(null);
		minimalActivity.setDeviceInfo(null);
		minimalActivity.setHeartRateZones(null);
		minimalActivity.setLaps(null);

		String minimalOutput = printer.toConsoleString(minimalActivity);

		assertAll(
				() -> assertTrue(minimalOutput.contains("elevationData:      null")),
				() -> assertTrue(minimalOutput.contains("deviceInfo:         null")),
				() -> assertFalse(minimalOutput.contains("HeartRateZones results:")),
				() -> assertFalse(minimalOutput.contains("ActivityLaps results:"))
		);

		/*
		 * Exercise all remaining instanceof branches.
		 */
		assertAll(
				() -> assertTrue(printer.toConsoleString(new Running(activityIds)).contains("Running results:")),
				() -> assertTrue(printer.toConsoleString(new Meditation(activityIds)).contains("Meditation results:")),
				() -> assertTrue(printer.toConsoleString(new FloorClimbing(activityIds)).contains("Floor Climbing results:")),
				() -> assertTrue(createHiking().contains("Hiking results:")),
				() -> assertTrue(createRucking().contains("Rucking results:")),
				() -> assertTrue(printer.toConsoleString(new TrainingActivity(activityIds)).contains("Training results:")),
				() -> assertTrue(printer.toConsoleString(new Yoga(activityIds)).contains("Yoga results:")),
				() -> assertTrue(printer.toConsoleString(new Breathing(activityIds)).contains("Breathing results:"))
		);
	}

	@Test
	void toConsoleString_withEmptyLaps_printsNoLapsMessage() {
		Activity activity = createBaseActivity();
		activity.setLaps(new ActivityLaps());

		String output = printer.toConsoleString(activity);

		assertAll(
				() -> assertTrue(output.contains("ActivityLaps results:")),
				() -> assertTrue(output.contains("-- no laps --"))
		);
	}
	
	@Test
	void toConsoleString_withSets_printsAllSetValuesInNumberOrder() {
		Activity activity = createBaseActivity();

		ActivitySets sets = new ActivitySets();

		ActivitySet set1 = new ActivitySet();
		set1.setNumber(1);
		set1.setSetTimestamp(Instant.parse("2026-09-20T14:12:30Z"));
		set1.setStartTime(Instant.parse("2026-09-20T14:12:00Z"));
		set1.setDuration(Duration.ofSeconds(120));
		set1.setSetType(1);

		ActivitySet set0 = new ActivitySet();
		set0.setNumber(0);
		set0.setSetTimestamp(Instant.parse("2026-09-20T14:10:30Z"));
		set0.setStartTime(Instant.parse("2026-09-20T14:10:00Z"));
		set0.setDuration(Duration.ofSeconds(90));
		set0.setSetType(0);

		/*
		 * Add sets in reverse order to verify that ActivitySets uses
		 * the set number rather than the insertion order.
		 */
		sets.add(set1);
		sets.add(set0);

		activity.setSets(sets);

		String output = printer.toConsoleString(activity);

		assertAll(
				() -> assertTrue(output.contains("ActivitySets results:")),

				() -> assertTrue(output.contains("parameter")),
				() -> assertTrue(output.contains("set0")),
				() -> assertTrue(output.contains("set1")),

				() -> assertTrue(output.contains("number")),
				() -> assertTrue(output.contains("setTimestamp")),
				() -> assertTrue(output.contains("startTime")),
				() -> assertTrue(output.contains("duration")),
				() -> assertTrue(output.contains("setType")),

				() -> assertTrue(output.contains("00:01:30")),
				() -> assertTrue(output.contains("00:02:00")),

				() -> assertTrue(output.contains("setType")),

				/*
				 * set0 must be printed before set1 even though set1
				 * was added first.
				 */
				() -> assertTrue(
						output.indexOf("set0") < output.indexOf("set1"),
						"Sets should be printed in ascending order by number"
				)
		);
	}

	private Activity createBaseActivity() {
		Activity activity = new Activity(activityIds);

		activity.setSport(Sport.WALKING);
		activity.setSubSport(SubSport.RUCKING);
		activity.setSportProfileName("Test walking");
		activity.setStartTime(Instant.parse("2026-09-20T13:53:05Z"));
		activity.setTimestamp(Instant.parse("2026-09-20T14:12:30Z"));
		activity.setFileCreationTime(Instant.parse("2026-09-20T14:12:31Z"));
		activity.setTotalElapsedTime(Duration.ofSeconds(1165));
		activity.setTotalTimerTime(Duration.ofSeconds(1140));

		return activity;
	}

	private void copyBaseData(Activity source, Activity target) {
		target.setSport(source.getSport());
		target.setSubSport(source.getSubSport());
		target.setSportProfileName(source.getSportProfileName());
		target.setStartTime(source.getStartTime());
		target.setTimestamp(source.getTimestamp());
		target.setFileCreationTime(source.getFileCreationTime());
		target.setTotalElapsedTime(source.getTotalElapsedTime());
		target.setTotalTimerTime(source.getTotalTimerTime());
	}

	private ActivityLaps createLaps() {
		ActivityLaps laps = new ActivityLaps();

		ActivityLap lap0 = new ActivityLap();
		lap0.setNumber(0);
		lap0.setStartTime(Instant.parse("2026-09-20T13:53:05Z"));
		lap0.setEndTime(Instant.parse("2026-09-20T14:00:00Z"));
		lap0.setStartPosition(new Position(50.087451, 14.420671));
		lap0.setElapsedTime(Duration.ofSeconds(415));
		lap0.setTimerTime(Duration.ofSeconds(400));
		lap0.setTotalDistance(new Distance(1234.5));
		lap0.setAverageSpeed(1.5f);
		lap0.setMaximumSpeed(2.1f);
		lap0.setAverageHeartRate(123);
		lap0.setMaximumHeartRate(145);
		lap0.setAverageCadence(82.5f);
		lap0.setMaximumCadence(96.0f);
		lap0.setTotalCalories(123);
		lap0.setTotalAscent(new Distance(25.5));
		lap0.setTotalDescent(new Distance(17.5));
		lap0.setMinimumAltitude(245.5);
		lap0.setMaximumAltitude(270.5);
		lap0.setAverageTemperature(18.5f);
		lap0.setMinimumTemperature(16.0f);
		lap0.setMaximumTemperature(21.0f);

		/*
		 * Second lap intentionally contains null values.
		 * This exercises formatValue(null) and the null branches for
		 * distance/ascent/descent.
		 */
		ActivityLap lap1 = new ActivityLap();
		lap1.setNumber(1);
		lap1.setStartPosition(new Position(50.088001, 14.421002));

		laps.add(lap0);
		laps.add(lap1);

		return laps;
	}

	private String createHiking() {
		Hiking hiking = new Hiking(activityIds);
		copyBaseData(createBaseActivity(), hiking);
		hiking.setEnhancedAvgSpeed(1.0f);
		hiking.setTotalDistance(new Distance(500));

		return printer.toConsoleString(hiking);
	}

	private String createRucking() {
		Rucking rucking = new Rucking(activityIds);
		copyBaseData(createBaseActivity(), rucking);
		rucking.setEnhancedAvgSpeed(1.0f);
		rucking.setTotalDistance(new Distance(500));
		rucking.setPackWeightTenthsKg(30);

		return printer.toConsoleString(rucking);
	}
}

