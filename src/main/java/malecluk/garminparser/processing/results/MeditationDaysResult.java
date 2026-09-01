package malecluk.garminparser.processing.results;

public record MeditationDaysResult(
		String name,
        Integer meditatedDays,
        Integer requiredDays,
        boolean isCompleted) implements ActivityListenerResult {

}
