package malecluk.garminparser.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.ActivityElevationData;

class ActivityElevationDataMapperTest {

	private final ActivityElevationDataMapper mapper = new ActivityElevationDataMapper();

	@Test
	void setActivityElevationData_mapsAscentAndDescent() {
		Activity activity = new Activity();
		SessionMesg session = mock(SessionMesg.class);

		when(session.getTotalAscent()).thenReturn(123);
		when(session.getTotalDescent()).thenReturn(67);

		mapper.setActivityElevationData(activity, session);

		assertNotNull(activity.getElevationData());
		assertEquals(123, activity.getElevationData().getTotalAscent().meters());
		assertEquals(67, activity.getElevationData().getTotalDescent().meters());
	}

	@Test
	void setActivityElevationData_whenElevationDataIsNull_createsElevationData() {
		Activity activity = new Activity();
		SessionMesg session = mock(SessionMesg.class);

		when(session.getTotalAscent()).thenReturn(100);
		when(session.getTotalDescent()).thenReturn(50);

		assertNull(activity.getElevationData());

		mapper.setActivityElevationData(activity, session);

		assertNotNull(activity.getElevationData());
	}

	@Test
	void setActivityElevationData_whenElevationDataExists_reusesExistingInstance() {
		Activity activity = new Activity();
		ActivityElevationData elevationData = new ActivityElevationData();
		activity.setElevationData(elevationData);

		SessionMesg session = mock(SessionMesg.class);

		when(session.getTotalAscent()).thenReturn(100);
		when(session.getTotalDescent()).thenReturn(50);

		mapper.setActivityElevationData(activity, session);

		assertSame(elevationData, activity.getElevationData());
		assertEquals(100, activity.getElevationData().getTotalAscent().meters());
		assertEquals(50, activity.getElevationData().getTotalDescent().meters());
	}

	@Test
	void setActivityElevationData_withNullAscentAndDescent_mapsNullValues() {
		Activity activity = new Activity();
		SessionMesg session = mock(SessionMesg.class);

		when(session.getTotalAscent()).thenReturn(null);
		when(session.getTotalDescent()).thenReturn(null);

		mapper.setActivityElevationData(activity, session);

		assertNotNull(activity.getElevationData());
		assertNull(activity.getElevationData().getTotalAscent());
		assertNull(activity.getElevationData().getTotalDescent());
	}
}
