package malecluk.garminparser.model.activities;

/**
 * Represents a rucking activity.
 *
 * Rucking is a hiking activity performed while carrying additional weight.
 * Common hiking and outdoor movement data is inherited from {@link Hiking}.
 */
public class Rucking extends Hiking {
	
	/**
	 * Weight of the carried pack in tenths of a kilogram.
	 */
	private Integer packWeightTenthsKg;

	//-----------------------
	
	/**
	 * Returns the weight of the carried pack.
	 * @return pack weight in tenths of a kilogram
	 */
	public Integer getPackWeightTenthsKg() {
		return packWeightTenthsKg;
	}

	/**
	 * Sets the weight of the carried pack.
	 * @param packWeightTenthsKg pack weight in tenths of a kilogram
	 */
	public void setPackWeightTenthsKg(Integer packWeightTenthsKg) {
		this.packWeightTenthsKg = packWeightTenthsKg;
	}
}
