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
	 * manufacturer code
	 */
	private Integer manufacturer;
	
	/**
	 * product code
	 */
	private Integer product;
	
	/**
	 * Serial number of watches which created this activity.
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
