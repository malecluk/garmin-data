package malecluk.garminparser.model.component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Collection of laps belonging to an activity.
 *
 * <p>Laps are identified by their lap number and are stored in ascending order by
 * that number. The order in which laps are added does not affect the order in
 * which they are returned.</p>
 *
 * <p>Each lap number must be unique within the collection and must not be {@code null}.</p>
 */
public class ActivityLaps {

	private final Map<Integer, ActivityLap> lapsByNumber = new TreeMap<>();

	/**
	 * Adds a lap to this collection.
	 *
	 * <p>The lap is stored according to its lap number, so the order in which laps are
	 * added does not determine their order in the collection.</p>
	 *
	 * @param lap lap to add; must not be {@code null} and must have a number
	 * @throws NullPointerException if {@code lap} or its number is {@code null}
	 * @throws IllegalArgumentException if a lap with the same number already exists
	 */
	public void add(ActivityLap lap) {
		Objects.requireNonNull(lap, "lap must not be null");
		Objects.requireNonNull(lap.getNumber(), "lap number must not be null");

		if (lapsByNumber.putIfAbsent(lap.getNumber(), lap) != null) {
			throw new IllegalArgumentException("Lap with number " + lap.getNumber() + " already exists");
		}
	}

	/**
	 * Returns the lap with the specified number.
	 * @param number number identifying the lap
	 * @return the lap with the specified number, or {@code null} if no such lap exists
	 */
	public ActivityLap get(Integer number) {
		return lapsByNumber.get(number);
	}

	/**
	 * Checks whether a lap with the specified number exists.
	 * @param number number identifying the lap
	 * @return {@code true} if a lap with the specified number exists
	 */
	public boolean contains(Integer number) {
		return lapsByNumber.containsKey(number);
	}

	/**
	 * Returns all laps in ascending order by lap number.
	 *
	 * <p>The returned list is an unmodifiable snapshot of the current collection.</p>
	 *
	 * @return laps ordered by lap number
	 */
	public List<ActivityLap> lapsList() {
		return List.copyOf(lapsByNumber.values());
	}

	/**
	 * Returns the number of laps in this collection.
	 * @return number of laps
	 */
	public int size() {
		return lapsByNumber.size();
	}

	/**
	 * Checks whether no laps are available.
	 *
	 * @return {@code true} if the collection is empty
	 */
	public boolean isEmpty() {
		return lapsByNumber.isEmpty();
	}
}
