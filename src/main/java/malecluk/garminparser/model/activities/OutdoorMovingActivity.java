package malecluk.garminparser.model.activities;

import malecluk.garminparser.model.value.Distance;

/**
 * Represents an outdoor activity involving movement.
 * <p>Extends {@link Activity} with data common to outdoor activities where 
 * distance and average speed are relevant, such as walking, hiking or running.</p>
 */
public class OutdoorMovingActivity extends Activity {
	
	/**
	 * Enhanced average speed during the activity.
	 * Value is expressed in meters per second.
	 */
	private Float enhancedAvgSpeed;
	
	/**
	 * Total distance covered during the activity.
	 */
	private Distance totalDistance;
	
	//-----------------------

	/**
	 * Calculates the average pace from the enhanced average speed.
	 *
	 * <p>The returned value is expressed as decimal minutes per kilometer.
	 * For example, {@code 10.5} represents 10 minutes 30 seconds per kilometer.</p>
	 *
	 * @return average pace in decimal minutes per kilometer, or {@code null}
	 *         if the enhanced average speed is not available or is not positive
	 */
	public Float getAvgPace() {
		
		if (this.getEnhancedAvgSpeed() == null || this.getEnhancedAvgSpeed() <= 0) return null;
		
		// change speed from m/s to km/h
		Float speedInKmH = this.getEnhancedAvgSpeed() * 3.6f;
		Float paceInMinsPerKm = 60 / speedInKmH;
		
		return paceInMinsPerKm;
	}

	//-----------------------
	
	/**
	 * Returns the enhanced average speed.
	 * @return enhanced average speed in meters per second
	 */
	public Float getEnhancedAvgSpeed() {
		return enhancedAvgSpeed;
	}

	/**
	 * Sets the enhanced average speed.
	 * @param enhancedAvgSpeed enhanced average speed in meters per second
	 */
	public void setEnhancedAvgSpeed(Float enhancedAvgSpeed) {
		this.enhancedAvgSpeed = enhancedAvgSpeed;
	}

	/**
	 * Returns the total distance covered during the activity.
	 * @return total activity distance
	 */
	public Distance getTotalDistance() {
		return totalDistance;
	}

	/**
	 * Sets the total distance covered during the activity.
	 * @param totalDistance total activity distance
	 */
	public void setTotalDistance(Distance totalDistance) {
		this.totalDistance = totalDistance;
	}
}
