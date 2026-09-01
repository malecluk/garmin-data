package malecluk.garminparser.model.component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Holds the sets recorded during an activity.
 *
 * <p>Sets are identified by their number rather than by their position
 * in the underlying collection. The set number is taken directly from
 * the Garmin FIT {@code SetMesg.message_index} field.</p>
 *
 * <p>The order in which sets are added therefore has no effect on their
 * logical order. {@link #setList()} returns the current sets sorted by
 * their number.</p>
 */
public class ActivitySets {
	
	private final Map<Integer, ActivitySet> setsByNumber = new TreeMap<>();
	
	/**
	 * Adds a set to this collection.
	 *
	 * <p>The set is stored according to its set number, so the order in which sets are
	 * added does not determine their order in the collection.</p>
	 *
	 * @param set set to add; must not be {@code null} and must have a number
	 * @throws NullPointerException if {@code set} or its number is {@code null}
	 * @throws IllegalArgumentException if a set with the same number already exists
	 */
	public void add(ActivitySet set) {
		Objects.requireNonNull(set, "set must not be null");
		Objects.requireNonNull(set.getNumber(), "set number must not be null");

		if (setsByNumber.putIfAbsent(set.getNumber(), set) != null) {
			throw new IllegalArgumentException("Set with number " + set.getNumber() + " already exists");
		}
	}
	
	/**
	 * Returns the set with the specified number.
	 * @param number number identifying the set
	 * @return the set with the specified number, or {@code null} if no such set exists
	 */
	public ActivitySet get(Integer number) {
		return setsByNumber.get(number);
	}
	
	/**
	 * Returns whether this collection contains a set with the specified number.
	 * @param number number identifying the set
	 * @return {@code true} if a set with the specified number exists, otherwise {@code false}
	 */
	public boolean contains(Integer number) {
		return setsByNumber.containsKey(number);
	}
	
	/**
	 * Returns an unmodifiable snapshot of the current collection in ascending order by set number.
	 *
	 * <p>Changes made to this collection after this method returns are not reflected in the returned list.</p>
	 *
	 * @return an unmodifiable snapshot of the current collection
	 */
	public List<ActivitySet> setList() {
		return List.copyOf(setsByNumber.values());
	}
	
	/**
	 * Returns the number of sets in this collection.
	 * @return number of sets
	 */
	public int size() {
		return setsByNumber.size();
	}
	
	/**
	 * Checks whether no sets are available.
	 *
	 * @return {@code true} if the collection is empty
	 */
	public boolean isEmpty() {
		return setsByNumber.isEmpty();
	}
}
