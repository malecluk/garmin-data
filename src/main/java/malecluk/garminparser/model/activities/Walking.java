package malecluk.garminparser.model.activities;

/**
 * Represents a walking activity.
 * 
 * <p>A walking activity is an {@link OutdoorMovingActivity} performed primarily by walking.
 * In addition to the common outdoor movement metrics, it stores 
 * the total number of strides recorded during the activity.</p>
 */
public class Walking extends OutdoorMovingActivity {

	/**
	 * Total number of strides recorded during the activity.
	 */
	private Long totalStrides;
	
	//-----------------------
	
	/**
	 * Returns the total number of strides recorded during the activity.
	 * @return total number of strides, or {@code null} if not available
	 */
	public Long getTotalStrides() {
		return totalStrides;
	}

	/**
	 * Sets the total number of strides recorded during the activity.
	 * @param totalStrides total number of strides
	 */
	public void setTotalStrides(Long totalStrides) {
		this.totalStrides = totalStrides;
	}
	
	
}
