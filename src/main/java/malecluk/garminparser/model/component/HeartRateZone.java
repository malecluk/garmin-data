package malecluk.garminparser.model.component;

import java.time.Duration;

public record HeartRateZone(
		Integer number,
        Integer minBpm,
        Integer maxBpm,
        Duration time
) {
}
