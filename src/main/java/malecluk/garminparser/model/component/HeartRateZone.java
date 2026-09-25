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
		 * Zero-based heart-rate zone number.
		 *
		 * <p>The number is used to identify the zone independently of its position
		 * in a {@link HeartRateZones} list.</p>
		 */
		Integer number,
		
		/**
		 * Minimum heart rate belonging to this zone, in beats per minute.
		 *
		 * <p>The value is inclusive. A {@code null} value means that this zone has
		 * no lower heart-rate boundary.</p>
		 */
        Integer minBpm,
        
        /**
         * Maximum heart rate belonging to this zone, in beats per minute.
         *
         * <p>The value is inclusive. A {@code null} value means that this zone has
         * no upper heart-rate boundary.</p>
         */
        Integer maxBpm,
        
        /**
         * Duration spent in this heart-rate zone during the activity.
         */
        Duration time
) {
}
