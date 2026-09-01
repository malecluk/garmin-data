package malecluk.garminparser.model.component;

/**
 * Contains Garmin FIT device identification data associated with an activity.
 *
 * <p>The manufacturer and product values are stored as the numeric values
 * provided by the Garmin FIT SDK. They are not converted to human-readable
 * manufacturer or product names.</p>
 */
public class ActivityDeviceInfo {
	
	/**
	 * Garmin FIT manufacturer identifier of the device that recorded the activity.
	 *
	 * <p>The value is stored as provided by the Garmin FIT SDK and is not converted
	 * to a human-readable manufacturer name.</p>
	 */
	private Integer manufacturer;
	
	/**
	 * Garmin FIT product identifier of the device that recorded the activity.
	 *
	 * <p>The value is stored as provided by the Garmin FIT SDK and is not converted
	 * to a human-readable product name.</p>
	 */
	private Integer product;
	
	/**
	 * Serial number of the device that recorded the activity.
	 */
	private Long serialNumber;
	
	//-----------------------
	
	public Integer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Integer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Integer getProduct() {
		return product;
	}

	public void setProduct(Integer product) {
		this.product = product;
	}

	public Long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}
}
