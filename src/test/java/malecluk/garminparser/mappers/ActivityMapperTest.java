package malecluk.garminparser.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.garmin.fit.DateTime;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.SubSport;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivityDeviceInfo;

class ActivityMapperTest {

	private ActivityMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ActivityMapper();
	}

	@Test
	void setBaseSessionParams_mapsAllBaseSessionValues() {
		Activity activity = new Activity();
		SessionMesg session = mock(SessionMesg.class);

		Instant startTime = Instant.parse("2026-09-15T21:12:51Z");
		Instant timestamp = Instant.parse("2026-09-15T21:22:34Z");

		DateTime startDateTime = mock(DateTime.class);
		DateTime timestampDateTime = mock(DateTime.class);

		when(startDateTime.getDate()).thenReturn(Date.from(startTime));
		when(timestampDateTime.getDate()).thenReturn(Date.from(timestamp));

		when(session.getSport()).thenReturn(com.garmin.fit.Sport.WALKING);
		when(session.getSportProfileName()).thenReturn("Chůze se zátěží");
		when(session.getStartTime()).thenReturn(startDateTime);
		when(session.getSubSport()).thenReturn(com.garmin.fit.SubSport.RUCKING);
		when(session.getTimestamp()).thenReturn(timestampDateTime);
		when(session.getTotalElapsedTime()).thenReturn(582.378f);
		when(session.getTotalTimerTime()).thenReturn(570.123f);

		mapper.setBaseSessionParams(activity, session);

		assertEquals(Sport.WALKING, activity.getSport());
		assertEquals("Chůze se zátěží", activity.getSportProfileName());
		assertEquals(startTime, activity.getStartTime());
		assertEquals(SubSport.RUCKING, activity.getSubSport());
		assertEquals(timestamp, activity.getTimestamp());

		assertEquals(Duration.ofMillis(582378), activity.getTotalElapsedTime());
		assertEquals(Duration.ofMillis(570123), activity.getTotalTimerTime());
	}

	@Test
	void setBaseSessionParams_withNullSportAndSubSport_mapsToUnknown() {
		Activity activity = new Activity();
		SessionMesg session = mock(SessionMesg.class);

		Instant startTime = Instant.parse("2026-09-15T21:12:51Z");
		Instant timestamp = Instant.parse("2026-09-15T21:22:34Z");

		DateTime startDateTime = mock(DateTime.class);
		DateTime timestampDateTime = mock(DateTime.class);

		when(startDateTime.getDate()).thenReturn(Date.from(startTime));
		when(timestampDateTime.getDate()).thenReturn(Date.from(timestamp));

		when(session.getSport()).thenReturn(null);
		when(session.getSubSport()).thenReturn(null);
		when(session.getStartTime()).thenReturn(startDateTime);
		when(session.getTimestamp()).thenReturn(timestampDateTime);
		when(session.getTotalElapsedTime()).thenReturn(100.0f);
		when(session.getTotalTimerTime()).thenReturn(90.0f);

		mapper.setBaseSessionParams(activity, session);

		assertEquals(Sport.UNKNOWN, activity.getSport());
		assertEquals(SubSport.UNKNOWN, activity.getSubSport());
		assertEquals(startTime, activity.getStartTime());
		assertEquals(timestamp, activity.getTimestamp());
		assertEquals(Duration.ofSeconds(100), activity.getTotalElapsedTime());
		assertEquals(Duration.ofSeconds(90), activity.getTotalTimerTime());
	}

	@Test
	void setBaseSessionParams_withUnsupportedSportAndSubSport_mapsToUnknown() {
		Activity activity = new Activity();
		SessionMesg session = mock(SessionMesg.class);

		Instant startTime = Instant.parse("2026-09-15T21:12:51Z");
		Instant timestamp = Instant.parse("2026-09-15T21:22:34Z");

		DateTime startDateTime = mock(DateTime.class);
		DateTime timestampDateTime = mock(DateTime.class);

		when(startDateTime.getDate()).thenReturn(Date.from(startTime));
		when(timestampDateTime.getDate()).thenReturn(Date.from(timestamp));

		when(session.getSport()).thenReturn(com.garmin.fit.Sport.CYCLING);
		when(session.getSubSport()).thenReturn(com.garmin.fit.SubSport.TREADMILL);
		when(session.getStartTime()).thenReturn(startDateTime);
		when(session.getTimestamp()).thenReturn(timestampDateTime);
		when(session.getTotalElapsedTime()).thenReturn(123.456f);
		when(session.getTotalTimerTime()).thenReturn(120.001f);

		mapper.setBaseSessionParams(activity, session);

		assertEquals(Sport.UNKNOWN, activity.getSport());
		assertEquals(SubSport.UNKNOWN, activity.getSubSport());
	}

	@Test
	void setBaseFileIdParams_mapsFileCreationTimeAndDeviceInfo() {
		Activity activity = new Activity();
		FileIdMesg fileId = mock(FileIdMesg.class);

		Instant fileCreationTime = Instant.parse("2026-09-15T21:30:45Z");
		DateTime createdDateTime = mock(DateTime.class);

		when(createdDateTime.getDate()).thenReturn(Date.from(fileCreationTime));
		when(fileId.getTimeCreated()).thenReturn(createdDateTime);
		when(fileId.getManufacturer()).thenReturn(1);
		when(fileId.getProduct()).thenReturn(1234);
		when(fileId.getSerialNumber()).thenReturn(987654321L);

		mapper.setBaseFileIdParams(activity, fileId);

		assertEquals(fileCreationTime, activity.getFileCreationTime());

		ActivityDeviceInfo deviceInfo = activity.getDeviceInfo();

		assertNotNull(deviceInfo);
		assertEquals(1, deviceInfo.getManufacturer());
		assertEquals(1234, deviceInfo.getProduct());
		assertEquals(987654321L, deviceInfo.getSerialNumber());
	}
}
