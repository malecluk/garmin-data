package malecluk.garminparser.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.activities.OutdoorMovingActivity;

class OutdoorMovingMapperTest {

	private OutdoorMovingMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new OutdoorMovingMapper();
	}

	@Test
	void setOutdoorMovingSessionParams_mapsEnhancedAvgSpeedAndTotalDistance() {
		OutdoorMovingActivity activity = new OutdoorMovingActivity();
		SessionMesg session = mock(SessionMesg.class);

		when(session.getEnhancedAvgSpeed()).thenReturn(1.234f);
		when(session.getTotalDistance()).thenReturn(5086.46f);

		mapper.setOutdoorMovingSessionParams(activity, session);

		assertEquals(1.234f, activity.getEnhancedAvgSpeed());
		assertNotNull(activity.getTotalDistance());
		assertEquals(5086.46f, activity.getTotalDistance().meters());
	}

	@Test
	void setOutdoorMovingSessionParams_withNullEnhancedAvgSpeed_mapsNullSpeed() {
		OutdoorMovingActivity activity = new OutdoorMovingActivity();
		SessionMesg session = mock(SessionMesg.class);

		when(session.getEnhancedAvgSpeed()).thenReturn(null);
		when(session.getTotalDistance()).thenReturn(5086.46f);

		mapper.setOutdoorMovingSessionParams(activity, session);

		assertNull(activity.getEnhancedAvgSpeed());
		assertNotNull(activity.getTotalDistance());
		assertEquals(5086.46f, activity.getTotalDistance().meters());
	}

	@Test
	void setOutdoorMovingSessionParams_withNullDistance_mapsNullDistance() {
		OutdoorMovingActivity activity = new OutdoorMovingActivity();
		SessionMesg session = mock(SessionMesg.class);

		when(session.getEnhancedAvgSpeed()).thenReturn(1.234f);
		when(session.getTotalDistance()).thenReturn(null);

		mapper.setOutdoorMovingSessionParams(activity, session);

		assertEquals(1.234f, activity.getEnhancedAvgSpeed());
		assertNull(activity.getTotalDistance());
	}

	@Test
	void setOutdoorMovingSessionParams_withNullValues_mapsNullValues() {
		OutdoorMovingActivity activity = new OutdoorMovingActivity();
		SessionMesg session = mock(SessionMesg.class);

		when(session.getEnhancedAvgSpeed()).thenReturn(null);
		when(session.getTotalDistance()).thenReturn(null);

		mapper.setOutdoorMovingSessionParams(activity, session);

		assertNull(activity.getEnhancedAvgSpeed());
		assertNull(activity.getTotalDistance());
	}
}
