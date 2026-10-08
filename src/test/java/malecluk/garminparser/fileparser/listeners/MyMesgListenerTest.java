package malecluk.garminparser.fileparser.listeners;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.garmin.fit.ActivityClass;
import com.garmin.fit.ActivityMesg;
import com.garmin.fit.BloodPressureMesg;
import com.garmin.fit.DateTime;
import com.garmin.fit.DeviceInfoMesg;
import com.garmin.fit.DeviceSettingsMesg;
import com.garmin.fit.DisplayHeart;
import com.garmin.fit.DisplayMeasure;
import com.garmin.fit.DisplayPosition;
import com.garmin.fit.DisplayPower;
import com.garmin.fit.Event;
import com.garmin.fit.EventType;
import com.garmin.fit.File;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.Gender;
import com.garmin.fit.HrZoneCalc;
import com.garmin.fit.HrZoneMesg;
import com.garmin.fit.Language;
import com.garmin.fit.LapMesg;
import com.garmin.fit.MesgNum;
import com.garmin.fit.PwrZoneCalc;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.SetMesg;
import com.garmin.fit.Sport;
import com.garmin.fit.SportMesg;
import com.garmin.fit.SubSport;
import com.garmin.fit.TimeInZoneMesg;
import com.garmin.fit.TimestampCorrelationMesg;
import com.garmin.fit.UserProfileMesg;
import com.garmin.fit.ZonesTargetMesg;

import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;

class MyMesgListenerTest {

	@Test
	void onMesg_withActivityMesg_storesActivityMesg() {
		// Arrange
		ActivityMesg sourceMesg = new ActivityMesg();
		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();
		assertEquals(1, messages.getActivityMesgList().size());
		ActivityMesg storedMesg = messages.getActivityMesgList().get(0);
		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
	}

	@Test
	void onMesg_withDeviceInfoMesg_storesDeviceInfoMesg() {
		// Arrange
		DeviceInfoMesg sourceMesg = new DeviceInfoMesg();
		sourceMesg.setDeviceIndex((short) 1);
		sourceMesg.setDeviceType((short) 2);
		sourceMesg.setManufacturer(1);
		sourceMesg.setSerialNumber(123456789L);
		sourceMesg.setProductName("Test device");
		sourceMesg.setBatteryLevel((short) 85);
		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();
		assertEquals(1, messages.getDeviceInfoMesgList().size());
		DeviceInfoMesg storedMesg = messages.getDeviceInfoMesgList().get(0);
		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
		assertEquals(sourceMesg.getDeviceIndex(), storedMesg.getDeviceIndex());
		assertEquals(sourceMesg.getDeviceType(), storedMesg.getDeviceType());
		assertEquals(sourceMesg.getManufacturer(), storedMesg.getManufacturer());
		assertEquals(sourceMesg.getSerialNumber(), storedMesg.getSerialNumber());
		assertEquals(sourceMesg.getProductName(), storedMesg.getProductName());
		assertEquals(sourceMesg.getBatteryLevel(), storedMesg.getBatteryLevel());
	}

	@Test
	void onMesg_withDeviceSettingsMesg_storesDeviceSettingsMesg() {
		// Arrange
		DeviceSettingsMesg sourceMesg = new DeviceSettingsMesg();
		sourceMesg.setActiveTimeZone((short) 2);
		sourceMesg.setUtcOffset(7200L);
		sourceMesg.setTimeOffset(0, 3600L);
		sourceMesg.setTimeOffset(1, 7200L);
		sourceMesg.setTimeZoneOffset(0, 1.0f);
		sourceMesg.setTimeZoneOffset(1, 0.25f);
		sourceMesg.setNumberOfScreens((short) 5);
		sourceMesg.setAutosyncMinSteps(1000);
		sourceMesg.setAutosyncMinTime(30);
		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();
		assertEquals(1, messages.getDeviceSettingsMesgList().size());
		DeviceSettingsMesg storedMesg = messages.getDeviceSettingsMesgList().get(0);
		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
		assertEquals(sourceMesg.getActiveTimeZone(), storedMesg.getActiveTimeZone());
		assertEquals(sourceMesg.getUtcOffset(), storedMesg.getUtcOffset());
		assertEquals(sourceMesg.getTimeOffset(0), storedMesg.getTimeOffset(0));
		assertEquals(sourceMesg.getTimeOffset(1), storedMesg.getTimeOffset(1));
		assertEquals(sourceMesg.getTimeZoneOffset(0), storedMesg.getTimeZoneOffset(0));
		assertEquals(sourceMesg.getTimeZoneOffset(1), storedMesg.getTimeZoneOffset(1));
		assertEquals(sourceMesg.getNumberOfScreens(), storedMesg.getNumberOfScreens());
		assertEquals(sourceMesg.getAutosyncMinSteps(), storedMesg.getAutosyncMinSteps());
		assertEquals(sourceMesg.getAutosyncMinTime(), storedMesg.getAutosyncMinTime());
	}

	@Test
	void onMesg_withFileIdMesg_storesFileIdMesg() {
		// Arrange
		FileIdMesg sourceMesg = new FileIdMesg();
		sourceMesg.setType(File.ACTIVITY);
		sourceMesg.setManufacturer(1);
		sourceMesg.setProduct(123);
		sourceMesg.setSerialNumber(987654321L);
		sourceMesg.setTimeCreated(new DateTime(1700000000L));
		sourceMesg.setNumber(42);
		sourceMesg.setProductName("Test Garmin");
		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();
		assertEquals(1, messages.getFileIdMesgList().size());
		FileIdMesg storedMesg = messages.getFileIdMesgList().get(0);
		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
		assertEquals(sourceMesg.getType(), storedMesg.getType());
		assertEquals(sourceMesg.getManufacturer(), storedMesg.getManufacturer());
		assertEquals(sourceMesg.getProduct(), storedMesg.getProduct());
		assertEquals(sourceMesg.getSerialNumber(), storedMesg.getSerialNumber());
		assertEquals(sourceMesg.getTimeCreated().getTimestamp(), storedMesg.getTimeCreated().getTimestamp());
		assertEquals(sourceMesg.getNumber(), storedMesg.getNumber());
		assertEquals(sourceMesg.getProductName(), storedMesg.getProductName());
	}

	@Test
	void onMesg_withHrZoneMesg_storesHrZoneMesg() {
		// Arrange
		HrZoneMesg sourceMesg = new HrZoneMesg();
		sourceMesg.setMessageIndex(2);
		sourceMesg.setHighBpm((short) 150);
		sourceMesg.setName("Zone 3");
		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();
		assertEquals(1, messages.getHrZoneMesgList().size());
		HrZoneMesg storedMesg = messages.getHrZoneMesgList().get(0);
		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
		assertEquals(sourceMesg.getMessageIndex(), storedMesg.getMessageIndex());
		assertEquals(sourceMesg.getHighBpm(), storedMesg.getHighBpm());
		assertEquals(sourceMesg.getName(), storedMesg.getName());
	}

	@Test
	void onMesg_withLapMesg_storesLapMesg() {
		// Arrange
		LapMesg sourceMesg = new LapMesg();
		DateTime timestamp = new DateTime(1700000000L);
		DateTime startTime = new DateTime(1700000100L);
		sourceMesg.setMessageIndex(3);
		sourceMesg.setTimestamp(timestamp);
		sourceMesg.setEvent(Event.TIMER);
		sourceMesg.setEventType(EventType.STOP);
		sourceMesg.setStartTime(startTime);
		sourceMesg.setStartPositionLat(500000000);
		sourceMesg.setStartPositionLong(140000000);
		sourceMesg.setEndPositionLat(501000000);
		sourceMesg.setEndPositionLong(141000000);
		sourceMesg.setTotalElapsedTime(123.456f);
		sourceMesg.setTotalTimerTime(120.789f);
		sourceMesg.setTotalDistance(1500.25f);
		sourceMesg.setTotalCycles(3000L);
		sourceMesg.setTotalCalories(120);
		sourceMesg.setAvgSpeed(3.5f);
		sourceMesg.setMaxSpeed(5.2f);
		sourceMesg.setAvgHeartRate((short) 135);
		sourceMesg.setMaxHeartRate((short) 155);
		sourceMesg.setAvgCadence((short) 170);
		sourceMesg.setMaxCadence((short) 185);
		sourceMesg.setTotalAscent(25);
		sourceMesg.setTotalDescent(20);
		sourceMesg.setAvgAltitude(250.5f);
		sourceMesg.setMaxAltitude(260.0f);
		sourceMesg.setMinAltitude(245.5f);
		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();
		assertEquals(1, messages.getLapMesgList().size());
		LapMesg storedMesg = messages.getLapMesgList().get(0);
		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
		assertEquals(sourceMesg.getMessageIndex(), storedMesg.getMessageIndex());
		assertEquals(sourceMesg.getTimestamp().getTimestamp(), storedMesg.getTimestamp().getTimestamp());
		assertEquals(sourceMesg.getStartTime().getTimestamp(), storedMesg.getStartTime().getTimestamp());
		assertEquals(sourceMesg.getEvent(), storedMesg.getEvent());
		assertEquals(sourceMesg.getEventType(), storedMesg.getEventType());
		assertEquals(sourceMesg.getStartPositionLat(), storedMesg.getStartPositionLat());
		assertEquals(sourceMesg.getStartPositionLong(), storedMesg.getStartPositionLong());
		assertEquals(sourceMesg.getEndPositionLat(), storedMesg.getEndPositionLat());
		assertEquals(sourceMesg.getEndPositionLong(), storedMesg.getEndPositionLong());
		assertEquals(sourceMesg.getTotalElapsedTime(), storedMesg.getTotalElapsedTime());
		assertEquals(sourceMesg.getTotalTimerTime(), storedMesg.getTotalTimerTime());
		assertEquals(sourceMesg.getTotalDistance(), storedMesg.getTotalDistance());
		assertEquals(sourceMesg.getTotalCycles(), storedMesg.getTotalCycles());
		assertEquals(sourceMesg.getTotalCalories(), storedMesg.getTotalCalories());
		assertEquals(sourceMesg.getAvgSpeed(), storedMesg.getAvgSpeed());
		assertEquals(sourceMesg.getMaxSpeed(), storedMesg.getMaxSpeed());
		assertEquals(sourceMesg.getAvgHeartRate(), storedMesg.getAvgHeartRate());
		assertEquals(sourceMesg.getMaxHeartRate(), storedMesg.getMaxHeartRate());
		assertEquals(sourceMesg.getAvgCadence(), storedMesg.getAvgCadence());
		assertEquals(sourceMesg.getMaxCadence(), storedMesg.getMaxCadence());
		assertEquals(sourceMesg.getTotalAscent(), storedMesg.getTotalAscent());
		assertEquals(sourceMesg.getTotalDescent(), storedMesg.getTotalDescent());
		assertEquals(sourceMesg.getAvgAltitude(), storedMesg.getAvgAltitude());
		assertEquals(sourceMesg.getMaxAltitude(), storedMesg.getMaxAltitude());
		assertEquals(sourceMesg.getMinAltitude(), storedMesg.getMinAltitude());
	}

	@Test
	void onMesg_withSessionMesg_storesSessionMesg() {
		// Arrange
		SessionMesg sourceMesg = new SessionMesg();

		sourceMesg.setMessageIndex(3);
		sourceMesg.setTimestamp(new DateTime(1700000000L));
		sourceMesg.setStartTime(new DateTime(1700000100L));
		sourceMesg.setStartPositionLat(123456789);
		sourceMesg.setStartPositionLong(-987654321);
		sourceMesg.setTotalElapsedTime(1234.5f);
		sourceMesg.setTotalTimerTime(1200.25f);
		sourceMesg.setTotalDistance(5432.1f);
		sourceMesg.setTotalCalories(456);
		sourceMesg.setAvgHeartRate((short) 125);
		sourceMesg.setMaxHeartRate((short) 158);
		sourceMesg.setAvgSpeed(1.75f);
		sourceMesg.setMaxSpeed(2.5f);
		sourceMesg.setTotalAscent(123);
		sourceMesg.setTotalDescent(98);
		sourceMesg.setNumLaps(4);

		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertEquals(1, messages.getSessionMesgList().size());

		SessionMesg storedMesg = messages.getSessionMesgList().get(0);

		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());

		assertEquals(sourceMesg.getMessageIndex(), storedMesg.getMessageIndex());
		assertEquals(sourceMesg.getTimestamp().getTimestamp(), storedMesg.getTimestamp().getTimestamp());
		assertEquals(sourceMesg.getStartTime().getTimestamp(), storedMesg.getStartTime().getTimestamp());
		assertEquals(sourceMesg.getStartPositionLat(), storedMesg.getStartPositionLat());
		assertEquals(sourceMesg.getStartPositionLong(), storedMesg.getStartPositionLong());
		assertEquals(sourceMesg.getTotalElapsedTime(), storedMesg.getTotalElapsedTime());
		assertEquals(sourceMesg.getTotalTimerTime(), storedMesg.getTotalTimerTime());
		assertEquals(sourceMesg.getTotalDistance(), storedMesg.getTotalDistance());
		assertEquals(sourceMesg.getTotalCalories(), storedMesg.getTotalCalories());
		assertEquals(sourceMesg.getAvgHeartRate(), storedMesg.getAvgHeartRate());
		assertEquals(sourceMesg.getMaxHeartRate(), storedMesg.getMaxHeartRate());
		assertEquals(sourceMesg.getAvgSpeed(), storedMesg.getAvgSpeed());
		assertEquals(sourceMesg.getMaxSpeed(), storedMesg.getMaxSpeed());
		assertEquals(sourceMesg.getTotalAscent(), storedMesg.getTotalAscent());
		assertEquals(sourceMesg.getTotalDescent(), storedMesg.getTotalDescent());
		assertEquals(sourceMesg.getNumLaps(), storedMesg.getNumLaps());
	}

	@Test
	void onMesg_withSportMesg_storesSportMesg() {
		// Arrange
		SportMesg sourceMesg = new SportMesg();

		sourceMesg.setSport(Sport.WALKING);
		sourceMesg.setSubSport(SubSport.CASUAL_WALKING);
		sourceMesg.setName("Test walking activity");

		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertEquals(1, messages.getSportMesgList().size());

		SportMesg storedMesg = messages.getSportMesgList().get(0);

		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());

		assertEquals(sourceMesg.getSport(), storedMesg.getSport());
		assertEquals(sourceMesg.getSubSport(), storedMesg.getSubSport());
		assertEquals(sourceMesg.getName(), storedMesg.getName());
	}

	@Test
	void onMesg_withTimeInZoneMesg_storesTimeInZoneMesg() {
		// Arrange
		TimeInZoneMesg sourceMesg = new TimeInZoneMesg();

		sourceMesg.setTimestamp(new DateTime(1700000000L));
		sourceMesg.setReferenceMesg(MesgNum.SESSION);
		sourceMesg.setReferenceIndex(2);

		sourceMesg.setTimeInHrZone(0, 120.5f);
		sourceMesg.setTimeInHrZone(1, 300.25f);

		sourceMesg.setTimeInSpeedZone(0, 60.0f);
		sourceMesg.setTimeInSpeedZone(1, 180.5f);

		sourceMesg.setTimeInCadenceZone(0, 30.0f);
		sourceMesg.setTimeInCadenceZone(1, 90.5f);

		sourceMesg.setTimeInPowerZone(0, 45.0f);
		sourceMesg.setTimeInPowerZone(1, 150.5f);

		sourceMesg.setHrZoneHighBoundary(0, (short) 120);
		sourceMesg.setHrZoneHighBoundary(1, (short) 150);

		sourceMesg.setSpeedZoneHighBoundary(0, 1.5f);
		sourceMesg.setSpeedZoneHighBoundary(1, 3.0f);

		sourceMesg.setCadenceZoneHighBondary(0, (short) 90);
		sourceMesg.setCadenceZoneHighBondary(1, (short) 120);

		sourceMesg.setPowerZoneHighBoundary(0, 150);
		sourceMesg.setPowerZoneHighBoundary(1, 250);

		sourceMesg.setHrCalcType(HrZoneCalc.PERCENT_HRR);
		sourceMesg.setMaxHeartRate((short) 185);
		sourceMesg.setRestingHeartRate((short) 55);
		sourceMesg.setThresholdHeartRate((short) 165);

		sourceMesg.setPwrCalcType(PwrZoneCalc.PERCENT_FTP);
		sourceMesg.setFunctionalThresholdPower(250);

		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertEquals(1, messages.getTimeInZoneMesgList().size());

		TimeInZoneMesg storedMesg = messages.getTimeInZoneMesgList().get(0);

		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());

		assertEquals(sourceMesg.getTimestamp().getTimestamp(), storedMesg.getTimestamp().getTimestamp());
		assertEquals(sourceMesg.getReferenceMesg(), storedMesg.getReferenceMesg());
		assertEquals(sourceMesg.getReferenceIndex(), storedMesg.getReferenceIndex());

		assertEquals(sourceMesg.getTimeInHrZone(0), storedMesg.getTimeInHrZone(0));
		assertEquals(sourceMesg.getTimeInHrZone(1), storedMesg.getTimeInHrZone(1));

		assertEquals(sourceMesg.getTimeInSpeedZone(0), storedMesg.getTimeInSpeedZone(0));
		assertEquals(sourceMesg.getTimeInSpeedZone(1), storedMesg.getTimeInSpeedZone(1));

		assertEquals(sourceMesg.getTimeInCadenceZone(0), storedMesg.getTimeInCadenceZone(0));
		assertEquals(sourceMesg.getTimeInCadenceZone(1), storedMesg.getTimeInCadenceZone(1));

		assertEquals(sourceMesg.getTimeInPowerZone(0), storedMesg.getTimeInPowerZone(0));
		assertEquals(sourceMesg.getTimeInPowerZone(1), storedMesg.getTimeInPowerZone(1));

		assertEquals(sourceMesg.getHrZoneHighBoundary(0), storedMesg.getHrZoneHighBoundary(0));
		assertEquals(sourceMesg.getHrZoneHighBoundary(1), storedMesg.getHrZoneHighBoundary(1));

		assertEquals(sourceMesg.getSpeedZoneHighBoundary(0), storedMesg.getSpeedZoneHighBoundary(0));
		assertEquals(sourceMesg.getSpeedZoneHighBoundary(1), storedMesg.getSpeedZoneHighBoundary(1));

		assertEquals(sourceMesg.getCadenceZoneHighBondary(0), storedMesg.getCadenceZoneHighBondary(0));
		assertEquals(sourceMesg.getCadenceZoneHighBondary(1), storedMesg.getCadenceZoneHighBondary(1));

		assertEquals(sourceMesg.getPowerZoneHighBoundary(0), storedMesg.getPowerZoneHighBoundary(0));
		assertEquals(sourceMesg.getPowerZoneHighBoundary(1), storedMesg.getPowerZoneHighBoundary(1));

		assertEquals(sourceMesg.getHrCalcType(), storedMesg.getHrCalcType());
		assertEquals(sourceMesg.getMaxHeartRate(), storedMesg.getMaxHeartRate());
		assertEquals(sourceMesg.getRestingHeartRate(), storedMesg.getRestingHeartRate());
		assertEquals(sourceMesg.getThresholdHeartRate(), storedMesg.getThresholdHeartRate());

		assertEquals(sourceMesg.getPwrCalcType(), storedMesg.getPwrCalcType());
		assertEquals(sourceMesg.getFunctionalThresholdPower(), storedMesg.getFunctionalThresholdPower());
	}

	@Test
	void onMesg_withTimestampCorrelationMesg_storesTimestampCorrelationMesg() {
		// Arrange
		TimestampCorrelationMesg sourceMesg = new TimestampCorrelationMesg();

		sourceMesg.setTimestamp(new DateTime(1700000000L));
		sourceMesg.setFractionalTimestamp(0.5f);
		sourceMesg.setSystemTimestamp(new DateTime(1700000100L));
		sourceMesg.setFractionalSystemTimestamp(0.25f);
		sourceMesg.setLocalTimestamp(1700000200L);
		sourceMesg.setTimestampMs(123);
		sourceMesg.setSystemTimestampMs(456);

		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(sourceMesg);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertEquals(1, messages.getTimestampCorrelationMesgList().size());

		TimestampCorrelationMesg storedMesg = messages.getTimestampCorrelationMesgList().get(0);

		assertNotSame(sourceMesg, storedMesg);
		assertEquals(sourceMesg.getNum(), storedMesg.getNum());

		assertEquals(sourceMesg.getTimestamp().getTimestamp(), storedMesg.getTimestamp().getTimestamp());
		assertEquals(sourceMesg.getFractionalTimestamp(), storedMesg.getFractionalTimestamp());

		assertEquals(sourceMesg.getSystemTimestamp().getTimestamp(), storedMesg.getSystemTimestamp().getTimestamp());
		assertEquals(sourceMesg.getFractionalSystemTimestamp(), storedMesg.getFractionalSystemTimestamp());

		assertEquals(sourceMesg.getLocalTimestamp(), storedMesg.getLocalTimestamp());

		assertEquals(sourceMesg.getTimestampMs(), storedMesg.getTimestampMs());
		assertEquals(sourceMesg.getSystemTimestampMs(), storedMesg.getSystemTimestampMs());
	}

	@Test
	void onMesg_withUserProfileMesg_storesUserProfileMesg() {
		UserProfileMesg sourceMesg = new UserProfileMesg();

		sourceMesg.setMessageIndex(1);
		sourceMesg.setFriendlyName("Test user");
		sourceMesg.setGender(Gender.MALE);
		sourceMesg.setAge((short) 35);
		sourceMesg.setHeight(1.80f);
		sourceMesg.setWeight(75.5f);
		sourceMesg.setLanguage(Language.ENGLISH);
		sourceMesg.setElevSetting(DisplayMeasure.METRIC);
		sourceMesg.setWeightSetting(DisplayMeasure.METRIC);
		sourceMesg.setRestingHeartRate((short) 55);
		sourceMesg.setDefaultMaxRunningHeartRate((short) 185);
		sourceMesg.setDefaultMaxBikingHeartRate((short) 180);
		sourceMesg.setDefaultMaxHeartRate((short) 185);
		sourceMesg.setHrSetting(DisplayHeart.BPM);
		sourceMesg.setSpeedSetting(DisplayMeasure.METRIC);
		sourceMesg.setDistSetting(DisplayMeasure.METRIC);
		sourceMesg.setPowerSetting(DisplayPower.WATTS);
		sourceMesg.setActivityClass(ActivityClass.ATHLETE);
		sourceMesg.setPositionSetting(DisplayPosition.DEGREE_MINUTE_SECOND);
		sourceMesg.setTemperatureSetting(DisplayMeasure.METRIC);
		sourceMesg.setLocalId(1234);

		sourceMesg.setGlobalId(0, (byte) 0x12);
		sourceMesg.setGlobalId(1, (byte) 0x34);
		sourceMesg.setGlobalId(2, (byte) 0x56);
		sourceMesg.setGlobalId(3, (byte) 0x78);

		sourceMesg.setWakeTime(25200L);
		sourceMesg.setSleepTime(82800L);
		sourceMesg.setHeightSetting(DisplayMeasure.METRIC);
		sourceMesg.setUserRunningStepLength(0.85f);
		sourceMesg.setUserWalkingStepLength(0.70f);
		sourceMesg.setDepthSetting(DisplayMeasure.METRIC);
		sourceMesg.setDiveCount(12L);

		MyMesgListener listener = new MyMesgListener();

		listener.onMesg(sourceMesg);

		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertEquals(1, messages.getUserProfileMesgList().size());

		UserProfileMesg storedMesg = messages.getUserProfileMesgList().get(0);

		assertNotSame(sourceMesg, storedMesg);

		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
		assertEquals(sourceMesg.getMessageIndex(), storedMesg.getMessageIndex());
		assertEquals(sourceMesg.getFriendlyName(), storedMesg.getFriendlyName());
		assertEquals(sourceMesg.getGender(), storedMesg.getGender());
		assertEquals(sourceMesg.getAge(), storedMesg.getAge());
		assertEquals(sourceMesg.getHeight(), storedMesg.getHeight());
		assertEquals(sourceMesg.getWeight(), storedMesg.getWeight());
		assertEquals(sourceMesg.getLanguage(), storedMesg.getLanguage());
		assertEquals(sourceMesg.getElevSetting(), storedMesg.getElevSetting());
		assertEquals(sourceMesg.getWeightSetting(), storedMesg.getWeightSetting());
		assertEquals(sourceMesg.getRestingHeartRate(), storedMesg.getRestingHeartRate());
		assertEquals(sourceMesg.getDefaultMaxRunningHeartRate(), storedMesg.getDefaultMaxRunningHeartRate());
		assertEquals(sourceMesg.getDefaultMaxBikingHeartRate(), storedMesg.getDefaultMaxBikingHeartRate());
		assertEquals(sourceMesg.getDefaultMaxHeartRate(), storedMesg.getDefaultMaxHeartRate());
		assertEquals(sourceMesg.getHrSetting(), storedMesg.getHrSetting());
		assertEquals(sourceMesg.getSpeedSetting(), storedMesg.getSpeedSetting());
		assertEquals(sourceMesg.getDistSetting(), storedMesg.getDistSetting());
		assertEquals(sourceMesg.getPowerSetting(), storedMesg.getPowerSetting());
		assertEquals(sourceMesg.getActivityClass(), storedMesg.getActivityClass());
		assertEquals(sourceMesg.getPositionSetting(), storedMesg.getPositionSetting());
		assertEquals(sourceMesg.getTemperatureSetting(), storedMesg.getTemperatureSetting());
		assertEquals(sourceMesg.getLocalId(), storedMesg.getLocalId());

		assertEquals(sourceMesg.getNumGlobalId(), storedMesg.getNumGlobalId());
		for (int i = 0; i < sourceMesg.getNumGlobalId(); i++) {
			assertEquals(sourceMesg.getGlobalId(i), storedMesg.getGlobalId(i));
		}

		assertEquals(sourceMesg.getWakeTime(), storedMesg.getWakeTime());
		assertEquals(sourceMesg.getSleepTime(), storedMesg.getSleepTime());
		assertEquals(sourceMesg.getHeightSetting(), storedMesg.getHeightSetting());
		assertEquals(sourceMesg.getUserRunningStepLength(), storedMesg.getUserRunningStepLength());
		assertEquals(sourceMesg.getUserWalkingStepLength(), storedMesg.getUserWalkingStepLength());
		assertEquals(sourceMesg.getDepthSetting(), storedMesg.getDepthSetting());
		assertEquals(sourceMesg.getDiveCount(), storedMesg.getDiveCount());
	}

	@Test
	void onMesg_withZonesTargetMesg_storesZonesTargetMesg() {
		ZonesTargetMesg sourceMesg = new ZonesTargetMesg();

		sourceMesg.setMaxHeartRate((short) 185);
		sourceMesg.setThresholdHeartRate((short) 165);
		sourceMesg.setFunctionalThresholdPower(250);
		sourceMesg.setHrCalcType(HrZoneCalc.PERCENT_HRR);
		sourceMesg.setPwrCalcType(PwrZoneCalc.PERCENT_FTP);

		MyMesgListener listener = new MyMesgListener();

		listener.onMesg(sourceMesg);

		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertEquals(1, messages.getZonesTargetMesgList().size());

		ZonesTargetMesg storedMesg = messages.getZonesTargetMesgList().get(0);

		assertNotSame(sourceMesg, storedMesg);

		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
		assertEquals(sourceMesg.getMaxHeartRate(), storedMesg.getMaxHeartRate());
		assertEquals(sourceMesg.getThresholdHeartRate(), storedMesg.getThresholdHeartRate());
		assertEquals(sourceMesg.getFunctionalThresholdPower(), storedMesg.getFunctionalThresholdPower());
		assertEquals(sourceMesg.getHrCalcType(), storedMesg.getHrCalcType());
		assertEquals(sourceMesg.getPwrCalcType(), storedMesg.getPwrCalcType());
	}

	@Test
	void onMesg_withSetMesg_storesSetMesg() {
		SetMesg sourceMesg = new SetMesg();

		sourceMesg.setTimestamp(new DateTime(1700000000L));
		sourceMesg.setDuration(120.5f);
		sourceMesg.setRepetitions(10);
		sourceMesg.setWeight(5.5f);
		sourceMesg.setSetType((short) 1);
		sourceMesg.setStartTime(new DateTime(1700000005L));

		sourceMesg.setCategory(0, 1);
		sourceMesg.setCategory(1, 2);

		sourceMesg.setCategorySubtype(0, 10);
		sourceMesg.setCategorySubtype(1, 20);

		sourceMesg.setWeightDisplayUnit(1);
		sourceMesg.setMessageIndex(3);
		sourceMesg.setWktStepIndex(7);

		MyMesgListener listener = new MyMesgListener();

		listener.onMesg(sourceMesg);

		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertEquals(1, messages.getSetMesgList().size());

		SetMesg storedMesg = messages.getSetMesgList().get(0);

		assertNotSame(sourceMesg, storedMesg);

		assertEquals(sourceMesg.getNum(), storedMesg.getNum());
		assertEquals(sourceMesg.getTimestamp().getTimestamp(), storedMesg.getTimestamp().getTimestamp());
		assertEquals(sourceMesg.getDuration(), storedMesg.getDuration());
		assertEquals(sourceMesg.getRepetitions(), storedMesg.getRepetitions());
		assertEquals(sourceMesg.getWeight(), storedMesg.getWeight());
		assertEquals(sourceMesg.getSetType(), storedMesg.getSetType());
		assertEquals(sourceMesg.getStartTime().getTimestamp(), storedMesg.getStartTime().getTimestamp());

		assertEquals(sourceMesg.getNumCategory(), storedMesg.getNumCategory());
		assertEquals(sourceMesg.getCategory(0), storedMesg.getCategory(0));
		assertEquals(sourceMesg.getCategory(1), storedMesg.getCategory(1));

		assertEquals(sourceMesg.getNumCategorySubtype(), storedMesg.getNumCategorySubtype());
		assertEquals(sourceMesg.getCategorySubtype(0), storedMesg.getCategorySubtype(0));
		assertEquals(sourceMesg.getCategorySubtype(1), storedMesg.getCategorySubtype(1));

		assertEquals(sourceMesg.getWeightDisplayUnit(), storedMesg.getWeightDisplayUnit());
		assertEquals(sourceMesg.getMessageIndex(), storedMesg.getMessageIndex());
		assertEquals(sourceMesg.getWktStepIndex(), storedMesg.getWktStepIndex());
	}

	@Test
	void onMesg_withUnknownMessage_doesNothing() {
		MyMesgListener listener = new MyMesgListener();
		BloodPressureMesg bloodPressureMesg = new BloodPressureMesg();

		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertDoesNotThrow(() -> listener.onMesg(bloodPressureMesg));

		assertTrue(messages.getActivityMesgList().isEmpty());
		assertTrue(messages.getDeviceInfoMesgList().isEmpty());
		assertTrue(messages.getDeviceSettingsMesgList().isEmpty());
		assertTrue(messages.getFileIdMesgList().isEmpty());
		assertTrue(messages.getHrZoneMesgList().isEmpty());
		assertTrue(messages.getLapMesgList().isEmpty());
		assertTrue(messages.getSessionMesgList().isEmpty());
		assertTrue(messages.getSportMesgList().isEmpty());
		assertTrue(messages.getTimeInZoneMesgList().isEmpty());
		assertTrue(messages.getTimestampCorrelationMesgList().isEmpty());
		assertTrue(messages.getUserProfileMesgList().isEmpty());
		assertTrue(messages.getZonesTargetMesgList().isEmpty());
		assertTrue(messages.getSetMesgList().isEmpty());
	}

	@Test
	void onMesg_withRecordMesg_storesRecordMesgsInOrder() {
		// Arrange
		RecordMesg first = new RecordMesg();
		first.setTimestamp(new DateTime(1700000000L));
		first.setPositionLat(500000000);
		first.setPositionLong(140000000);

		RecordMesg second = new RecordMesg();
		second.setTimestamp(new DateTime(1700000060L));
		second.setPositionLat(500100000);
		second.setPositionLong(140010000);

		RecordMesg third = new RecordMesg();
		third.setTimestamp(new DateTime(1700000120L));
		third.setPositionLat(500200000);
		third.setPositionLong(140020000);

		MyMesgListener listener = new MyMesgListener();

		// Act
		listener.onMesg(first);
		listener.onMesg(second);
		listener.onMesg(third);

		// Assert
		ParsedFitFileMessagesDTO messages = listener.getMessages();

		assertEquals(3, messages.getRecordMesgList().size());

		RecordMesg storedFirst = messages.getRecordMesgList().get(0);
		RecordMesg storedSecond = messages.getRecordMesgList().get(1);
		RecordMesg storedThird = messages.getRecordMesgList().get(2);

		assertNotSame(first, storedFirst);
		assertNotSame(second, storedSecond);
		assertNotSame(third, storedThird);

		assertEquals(first.getNum(), storedFirst.getNum());
		assertEquals(first.getTimestamp().getTimestamp(), storedFirst.getTimestamp().getTimestamp());
		assertEquals(first.getPositionLat(), storedFirst.getPositionLat());
		assertEquals(first.getPositionLong(), storedFirst.getPositionLong());

		assertEquals(second.getNum(), storedSecond.getNum());
		assertEquals(second.getTimestamp().getTimestamp(), storedSecond.getTimestamp().getTimestamp());
		assertEquals(second.getPositionLat(), storedSecond.getPositionLat());
		assertEquals(second.getPositionLong(), storedSecond.getPositionLong());

		assertEquals(third.getNum(), storedThird.getNum());
		assertEquals(third.getTimestamp().getTimestamp(), storedThird.getTimestamp().getTimestamp());
		assertEquals(third.getPositionLat(), storedThird.getPositionLat());
		assertEquals(third.getPositionLong(), storedThird.getPositionLong());
	}
}
