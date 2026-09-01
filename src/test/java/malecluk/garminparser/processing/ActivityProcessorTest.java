package malecluk.garminparser.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.processing.results.ActivityListenerResult;
import malecluk.garminparser.processing.results.ActivityProcessingResult;

class ActivityProcessorTest {

	@Test
	void process_withMultipleAnalyzers_passesActivityToAllAnalyzers() {

		Activity activity = mock(Activity.class);
		ActivityAnalyzer analyzer1 = mock(ActivityAnalyzer.class);
		ActivityAnalyzer analyzer2 = mock(ActivityAnalyzer.class);
		ActivityAnalyzer analyzer3 = mock(ActivityAnalyzer.class);

		ActivityProcessor processor = createProcessor(analyzer1, analyzer2, analyzer3);

		processor.process(activity);

		verify(analyzer1).onActivity(activity);
		verify(analyzer2).onActivity(activity);
		verify(analyzer3).onActivity(activity);
	}

	@Test
	void process_withNoAnalyzers_doesNotInteractWithAnalyzers() {

		ActivityProcessor processor = createProcessor();

		processor.process(mock(Activity.class));

		// There are no analyzers to interact with.
	}

	@Test
	void getResults_returnsAnalyzerResultsInAnalyzerOrder() {

		ActivityAnalyzer analyzer1 = mock(ActivityAnalyzer.class);
		ActivityAnalyzer analyzer2 = mock(ActivityAnalyzer.class);

		ActivityListenerResult result1 = mock(ActivityListenerResult.class);
		ActivityListenerResult result2 = mock(ActivityListenerResult.class);

		when(analyzer1.getResult()).thenReturn(result1);
		when(analyzer2.getResult()).thenReturn(result2);

		ActivityProcessor processor = createProcessor(analyzer1, analyzer2);

		ActivityProcessingResult result = processor.getResults();

		assertEquals(List.of(result1, result2), result.results());

		verify(analyzer1).getResult();
		verify(analyzer2).getResult();
	}

	@Test
	void processAndGetResults_processesActivityAndReturnsAnalyzerResults() {

		Activity activity = mock(Activity.class);

		ActivityAnalyzer analyzer1 = mock(ActivityAnalyzer.class);
		ActivityAnalyzer analyzer2 = mock(ActivityAnalyzer.class);

		ActivityListenerResult result1 = mock(ActivityListenerResult.class);
		ActivityListenerResult result2 = mock(ActivityListenerResult.class);

		when(analyzer1.getResult()).thenReturn(result1);
		when(analyzer2.getResult()).thenReturn(result2);

		ActivityProcessor processor = createProcessor(analyzer1, analyzer2);

		processor.process(activity);
		ActivityProcessingResult result = processor.getResults();

		verify(analyzer1).onActivity(activity);
		verify(analyzer2).onActivity(activity);

		assertEquals(2, result.results().size());
		assertSame(result1, result.results().get(0));
		assertSame(result2, result.results().get(1));
	}

	private ActivityProcessor createProcessor(ActivityAnalyzer... analyzers) {

		ActivityAnalyzerFactory factory = mock(ActivityAnalyzerFactory.class);
		ActivityProcessingProperties properties = mock(ActivityProcessingProperties.class);

		when(factory.createAnalyzers(properties)).thenReturn(List.of(analyzers));

		return new ActivityProcessor(factory, properties);
	}
}