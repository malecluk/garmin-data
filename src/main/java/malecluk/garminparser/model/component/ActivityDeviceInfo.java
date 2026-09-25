package malecluk.garminparser.model.component;

/**
 * Holder object for informations about device (watches) which created activity.
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
