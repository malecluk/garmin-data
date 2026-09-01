package malecluk.garminparser.processing.results;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ActivityProcessingResultTest {

	@Test
	void constructor_withNullResults_throwsNullPointerException() {
		NullPointerException exception = assertThrows(NullPointerException.class,
				() -> new ActivityProcessingResult(null));

		assertEquals("results must not be null", exception.getMessage());
	}

	@Test
	void constructor_copiesResultsList() {
		List<ActivityListenerResult> results = new ArrayList<>();
		ActivityProcessingResult processingResult = new ActivityProcessingResult(results);

		results.add(mock(ActivityListenerResult.class));

		assertTrue(processingResult.results().isEmpty());
	}

	@Test
	void results_areUnmodifiable() {
		ActivityListenerResult listenerResult = mock(ActivityListenerResult.class);

		ActivityProcessingResult processingResult = new ActivityProcessingResult(List.of(listenerResult));

		assertThrows(UnsupportedOperationException.class,
				() -> processingResult.results().add(mock(ActivityListenerResult.class)));
	}

}
