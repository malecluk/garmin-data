package malecluk.garminparser.model.component;

import java.time.Duration;

/**
 * Represents one heart-rate zone and the amount of time spent in that zone.
 *
 * <p>The zone number corresponds to the zone number defined by the FIT data.
 * A {@code null} minimum represents an open lower boundary, while a
 * {@code null} maximum represents an open upper boundary.</p>
 */
public record HeartRateZone(
		
		/**
		 * Zero-based or FIT-defined zone number.
		 */
		Integer number,
		
		/**
		 * Inclusive lower heart-rate boundary in beats per minute,
		 * or {@code null} when the zone has no lower boundary.
		 */
        Integer minBpm,
        
        /**
         * Inclusive upper heart-rate boundary in beats per minute,
         * or {@code null} when the zone has no upper boundary.
         */
        Integer maxBpm,
        
        /**
         * Time spent in this heart-rate zone.
         */
        Duration time
) {
}
