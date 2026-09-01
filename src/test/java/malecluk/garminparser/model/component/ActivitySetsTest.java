package malecluk.garminparser.model.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class ActivitySetsTest {

	@Test
	void add_storesSetAndAllowsLookupByNumber() {
		ActivitySets sets = new ActivitySets();

		ActivitySet set = createSet(3);

		sets.add(set);

		assertEquals(1, sets.size());
		assertEquals(set, sets.get(3));
		assertTrue(sets.contains(3));
		assertFalse(sets.isEmpty());
	}

	@Test
	void get_returnsNullWhenSetDoesNotExist() {
		ActivitySets sets = new ActivitySets();

		assertNull(sets.get(1));
	}

	@Test
	void contains_returnsFalseWhenSetDoesNotExist() {
		ActivitySets sets = new ActivitySets();

		assertFalse(sets.contains(1));
	}

	@Test
	void isEmpty_returnsTrueForNewCollection() {
		ActivitySets sets = new ActivitySets();

		assertTrue(sets.isEmpty());
	}

	@Test
	void add_rejectsNullSet() {
		ActivitySets sets = new ActivitySets();

		assertThrows(NullPointerException.class, () -> sets.add(null));
	}

	@Test
	void add_rejectsSetWithoutNumber() {
		ActivitySets sets = new ActivitySets();

		ActivitySet set = new ActivitySet();

		assertThrows(NullPointerException.class, () -> sets.add(set));
	}

	@Test
	void add_rejectsDuplicateSetNumber() {
		ActivitySets sets = new ActivitySets();

		sets.add(createSet(1));

		ActivitySet duplicate = createSet(1);

		assertThrows(IllegalArgumentException.class, () -> sets.add(duplicate));
	}

	@Test
	void setList_returnsSetsInAscendingNumberOrder() {
		ActivitySets sets = new ActivitySets();

		ActivitySet set3 = createSet(3);
		ActivitySet set1 = createSet(1);
		ActivitySet set2 = createSet(2);

		sets.add(set3);
		sets.add(set1);
		sets.add(set2);

		assertEquals(List.of(set1, set2, set3), sets.setList());
	}

	@Test
	void setList_returnsUnmodifiableList() {
		ActivitySets sets = new ActivitySets();

		ActivitySet set1 = createSet(1);
		sets.add(set1);

		List<ActivitySet> result = sets.setList();

		assertThrows(
				UnsupportedOperationException.class,
				() -> result.add(createSet(2))
		);
	}

	@Test
	void setList_returnsSnapshot() {
		ActivitySets sets = new ActivitySets();

		ActivitySet set1 = createSet(1);
		sets.add(set1);

		List<ActivitySet> result = sets.setList();

		ActivitySet set2 = createSet(2);
		sets.add(set2);

		assertEquals(List.of(set1), result);
		assertEquals(List.of(set1, set2), sets.setList());
	}
	
	@Test
	void size_returnsNumberOfSets() {
		ActivitySets sets = new ActivitySets();

		assertEquals(0, sets.size());

		sets.add(createSet(1));
		assertEquals(1, sets.size());

		sets.add(createSet(2));
		assertEquals(2, sets.size());

		sets.add(createSet(3));
		assertEquals(3, sets.size());
	}

	private ActivitySet createSet(int number) {
		ActivitySet set = new ActivitySet();
		set.setNumber(number);
		return set;
	}
}
