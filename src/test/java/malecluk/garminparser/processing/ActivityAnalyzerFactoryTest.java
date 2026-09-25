package malecluk.garminparser.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.processing.results.MeditationDaysResult;
import malecluk.garminparser.processing.results.WalkingAvgPaceTimeResult;
import malecluk.garminparser.processing.results.WalkingInHRZonesTimeResults;

class ActivityAnalyzerFactoryTest {

	private final ActivityAnalyzerFactory factory = new ActivityAnalyzerFactory();

	@Test
	void createAnalyzers_withEmptyConfiguration_returnsEmptyList() {

		ActivityProcessingProperties properties = new ActivityProcessingProperties();

		List<ActivityAnalyzer> result = factory.createAnalyzers(properties);

		assertEquals(0, result.size());
	}

	@Test
	void createAnalyzers_withWalkingAvgPaceTime_createsCorrectCounter() {

		WalkingAvgPaceTimeProperties config = new WalkingAvgPaceTimeProperties();

		LocalDateTime start = LocalDateTime.of(2026, 9, 1, 0, 0, 1);
		LocalDateTime end = LocalDateTime.of(2026, 9, 30, 23, 59, 59);
		Duration requiredDuration = Duration.ofHours(3);
		Float maxAveragePace = 10.5f;

		config.setName("Walking test");
		config.setStartDate(start);
		config.setEndDate(end);
		config.setRequiredDuration(requiredDuration);
		config.setMaxAveragePace(maxAveragePace);

		ActivityProcessingProperties properties = new ActivityProcessingProperties();
		properties.setWalkingAvgPaceTime(List.of(config));

		List<ActivityAnalyzer> result = factory.createAnalyzers(properties);

		assertEquals(1, result.size());

		WalkingAvgPaceTimeCounter analyzer = assertInstanceOf(WalkingAvgPaceTimeCounter.class, result.get(0));

		WalkingAvgPaceTimeResult analyzerResult = assertInstanceOf(WalkingAvgPaceTimeResult.class,
				analyzer.getResult());

		assertEquals("Walking test", analyzerResult.name());
		assertEquals(Duration.ZERO, analyzerResult.countedDuration());
		assertEquals(requiredDuration, analyzerResult.requiredDuration());
		assertEquals(maxAveragePace, analyzerResult.maxAveragePace());
		assertEquals(false, analyzerResult.isCompleted());
	}

	@Test
	void createAnalyzers_withMeditationDays_createsCorrectCounter() {

		MeditationDaysProperties config = new MeditationDaysProperties();

		LocalDateTime start = LocalDateTime.of(2026, 9, 1, 0, 0, 1);
		LocalDateTime end = LocalDateTime.of(2026, 9, 30, 23, 59, 59);
		int requiredDays = 10;

		config.setName("Meditation test");
		config.setStartDate(start);
		config.setEndDate(end);
		config.setRequiredDays(requiredDays);

		ActivityProcessingProperties properties = new ActivityProcessingProperties();
		properties.setMeditationDays(List.of(config));

		List<ActivityAnalyzer> result = factory.createAnalyzers(properties);

		assertEquals(1, result.size());

		MeditationDaysCounter analyzer = assertInstanceOf(MeditationDaysCounter.class, result.get(0));

		MeditationDaysResult analyzerResult = assertInstanceOf(MeditationDaysResult.class, analyzer.getResult());

		assertEquals("Meditation test", analyzerResult.name());
		assertEquals(0, analyzerResult.meditatedDays());
		assertEquals(requiredDays, analyzerResult.requiredDays());
		assertEquals(false, analyzerResult.isCompleted());
	}

	@Test
	void createAnalyzers_withWalkingHRZones_createsCorrectCounter() {

		WalkingInHRZonesTimeProperties config = new WalkingInHRZonesTimeProperties();

		LocalDateTime start = LocalDateTime.of(2026, 9, 1, 0, 0, 1);
		LocalDateTime end = LocalDateTime.of(2026, 9, 30, 23, 59, 59);
		Duration requiredDuration = Duration.ofHours(3);
		List<Integer> requiredZones = List.of(2, 3);

		config.setName("HR zones test");
		config.setStartDate(start);
		config.setEndDate(end);
		config.setRequiredDuration(requiredDuration);
		config.setRequiredZones(requiredZones);

		ActivityProcessingProperties properties = new ActivityProcessingProperties();
		properties.setWalkingHRZones(List.of(config));

		List<ActivityAnalyzer> result = factory.createAnalyzers(properties);

		assertEquals(1, result.size());

		WalkingInHRZonesTimeCounter analyzer = assertInstanceOf(WalkingInHRZonesTimeCounter.class, result.get(0));

		WalkingInHRZonesTimeResults analyzerResult = assertInstanceOf(WalkingInHRZonesTimeResults.class,
				analyzer.getResult());

		assertEquals("HR zones test", analyzerResult.name());
		assertEquals(Duration.ZERO, analyzerResult.countedDuration());
		assertEquals(requiredDuration, analyzerResult.requiredDuration());
		assertEquals(false, analyzerResult.isCompleted());
	}

	@Test
	void createAnalyzers_withMultipleConfigurations_createsAllAnalyzers() {

		WalkingAvgPaceTimeProperties walking1 = new WalkingAvgPaceTimeProperties();
		walking1.setName("Walking 1");
		walking1.setStartDate(LocalDateTime.of(2026, 9, 1, 0, 0));
		walking1.setEndDate(LocalDateTime.of(2026, 9, 10, 0, 0));
		walking1.setRequiredDuration(Duration.ofHours(1));
		walking1.setMaxAveragePace(10.0f);

		WalkingAvgPaceTimeProperties walking2 = new WalkingAvgPaceTimeProperties();
		walking2.setName("Walking 2");
		walking2.setStartDate(LocalDateTime.of(2026, 9, 11, 0, 0));
		walking2.setEndDate(LocalDateTime.of(2026, 9, 20, 0, 0));
		walking2.setRequiredDuration(Duration.ofHours(2));
		walking2.setMaxAveragePace(9.0f);

		MeditationDaysProperties meditation1 = new MeditationDaysProperties();
		meditation1.setName("Meditation 1");
		meditation1.setStartDate(LocalDateTime.of(2026, 9, 1, 0, 0));
		meditation1.setEndDate(LocalDateTime.of(2026, 9, 10, 0, 0));
		meditation1.setRequiredDays(3);

		MeditationDaysProperties meditation2 = new MeditationDaysProperties();
		meditation2.setName("Meditation 2");
		meditation2.setStartDate(LocalDateTime.of(2026, 9, 11, 0, 0));
		meditation2.setEndDate(LocalDateTime.of(2026, 9, 20, 0, 0));
		meditation2.setRequiredDays(5);

		MeditationDaysProperties meditation3 = new MeditationDaysProperties();
		meditation3.setName("Meditation 3");
		meditation3.setStartDate(LocalDateTime.of(2026, 9, 21, 0, 0));
		meditation3.setEndDate(LocalDateTime.of(2026, 9, 30, 0, 0));
		meditation3.setRequiredDays(7);

		WalkingInHRZonesTimeProperties hrZones = new WalkingInHRZonesTimeProperties();
		hrZones.setName("HR zones");
		hrZones.setStartDate(LocalDateTime.of(2026, 9, 1, 0, 0));
		hrZones.setEndDate(LocalDateTime.of(2026, 9, 30, 23, 59));
		hrZones.setRequiredDuration(Duration.ofHours(3));
		hrZones.setRequiredZones(List.of(2, 3));

		ActivityProcessingProperties properties = new ActivityProcessingProperties();
		properties.setWalkingAvgPaceTime(List.of(walking1, walking2));
		properties.setMeditationDays(List.of(meditation1, meditation2, meditation3));
		properties.setWalkingHRZones(List.of(hrZones));

		List<ActivityAnalyzer> result = factory.createAnalyzers(properties);

		assertEquals(6, result.size());

		assertInstanceOf(WalkingAvgPaceTimeCounter.class, result.get(0));
		assertInstanceOf(WalkingAvgPaceTimeCounter.class, result.get(1));

		assertInstanceOf(MeditationDaysCounter.class, result.get(2));
		assertInstanceOf(MeditationDaysCounter.class, result.get(3));
		assertInstanceOf(MeditationDaysCounter.class, result.get(4));

		assertInstanceOf(WalkingInHRZonesTimeCounter.class, result.get(5));
	}
}
