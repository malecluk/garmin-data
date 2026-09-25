package malecluk.garminparser.mappers;

import org.springframework.stereotype.Component;

import com.garmin.fit.SessionMesg;

import malecluk.garminparser.model.activities.Walking;

/**
 * Maps walking-specific data from a FIT session message to a {@link Walking} activity.
 */
@Component
public class WalkingMapper {

	/**
     * Maps walking-specific session parameters.
     *
     * <p>Currently maps the total number of strides recorded by Garmin.</p>
     *
     * @param w walking activity to update
     * @param m FIT session message containing walking data
     */
	public void setWalkingSessionParams(Walking w, SessionMesg m) {
		w.setTotalStrides(m.getTotalStrides());
	}
}
