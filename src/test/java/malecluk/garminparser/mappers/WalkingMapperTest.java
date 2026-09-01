package malecluk.garminparser.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;
import malecluk.garminparser.model.activities.Walking;

class WalkingMapperTest {

    private final WalkingMapper mapper = new WalkingMapper();
    private final ActivityIds activityIds = new ActivityIds(new ActivityId("12345"), new SourceFileHash("67890"));

    @Test
    void setWalkingSessionParams_withTotalStrides_setsValue() {
        Walking walking = new Walking(activityIds);
        SessionMesg session = mock(SessionMesg.class);

        when(session.getTotalStrides()).thenReturn(315l);

        mapper.setWalkingSessionParams(walking, session);

        assertEquals(Long.valueOf(315), walking.getTotalStrides());
    }

    @Test
    void setWalkingSessionParams_withNullTotalStrides_setsNull() {
        Walking walking = new Walking(activityIds);
        SessionMesg session = mock(SessionMesg.class);

        when(session.getTotalStrides()).thenReturn(null);

        mapper.setWalkingSessionParams(walking, session);

        assertNull(walking.getTotalStrides());
    }
}
