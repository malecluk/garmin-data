package malecluk.garminparser.model.activities;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.SourceFileHash;
import malecluk.garminparser.model.component.ActivityTrack;

class OutdoorMovingActivityTest {

	@Test
	void setTrack_withTrack_setsTrack() {
		OutdoorMovingActivity activity = new OutdoorMovingActivity(createActivityIds());
		ActivityTrack track = new ActivityTrack(List.of());

		activity.setTrack(track);

		assertSame(track, activity.getTrack());
	}

	private ActivityIds createActivityIds() {
		return new ActivityIds(new ActivityId("123456789-1234567890"),
				new SourceFileHash("abcdef123456"));
	}
}
