package malecluk.garminparser.mappers.sportspecific;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.garmin.fit.Field;
import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;
import malecluk.garminparser.model.activities.Rucking;

class RuckingMapperTest {

	private RuckingMapper mapper;

	private final ActivityIds activityIds = new ActivityIds(
			new ActivityId("12345"),
			new SourceFileHash("67890"));

	@BeforeEach
	void setUp() {
		mapper = new RuckingMapper();
	}

	@Test
	void setRuckingSessionParams_withPackWeight_setsPackWeight() {
		Rucking rucking = new Rucking(activityIds);
		SessionMesg sessionMesg = mock(SessionMesg.class);
		Field field = mock(Field.class);

		when(sessionMesg.getField(220)).thenReturn(field);
		when(field.getValue()).thenReturn(30);

		mapper.setRuckingSessionParams(rucking, sessionMesg);

		assertEquals(Integer.valueOf(30), rucking.getPackWeightTenthsKg());
	}

	@Test
	void setRuckingSessionParams_withoutPackWeightField_setsNullPackWeight() {
		Rucking rucking = new Rucking(activityIds);
		SessionMesg sessionMesg = mock(SessionMesg.class);

		when(sessionMesg.getField(220)).thenReturn(null);

		mapper.setRuckingSessionParams(rucking, sessionMesg);

		assertNull(rucking.getPackWeightTenthsKg());
	}

	@Test
	void setRuckingSessionParams_withNullPackWeightValue_setsNullPackWeight() {
		Rucking rucking = new Rucking(activityIds);
		SessionMesg sessionMesg = mock(SessionMesg.class);
		Field field = mock(Field.class);

		when(sessionMesg.getField(220)).thenReturn(field);
		when(field.getValue()).thenReturn(null);

		mapper.setRuckingSessionParams(rucking, sessionMesg);

		assertNull(rucking.getPackWeightTenthsKg());
	}
}