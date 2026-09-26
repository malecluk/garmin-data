package malecluk.garminparser.model.component;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of laps belonging to an activity.
 */
public record ActivityLaps(List<ActivityLap> lapsList) {

	public ActivityLaps() {
		this(new ArrayList<>());
	}

	/**
	 * Adds a lap to this collection.
	 * @param lap lap to add
	 */
	public void add(ActivityLap lap) {
		lapsList.add(lap);
	}

	/**
	 * Checks whether no laps are available.
	 * @return {@code true} if the collection is empty
	 */
	public boolean isEmpty() {
		return lapsList.isEmpty();
	}
}