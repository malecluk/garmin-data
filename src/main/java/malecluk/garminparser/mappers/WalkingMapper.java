package malecluk.garminparser.mappers;

import org.springframework.stereotype.Component;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.activities.Walking;

@Component
public class WalkingMapper {

	public void setWalkingSessionParams(Walking w, SessionMesg m) {
		w.setTotalStrides(m.getTotalStrides());
	}
}
