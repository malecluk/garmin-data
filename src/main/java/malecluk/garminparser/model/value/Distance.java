package malecluk.garminparser.model.value;

public record Distance(Double meters) {
	
	public Distance(Number meters) {
        this(meters.doubleValue());
    }

    public Double toMeters() {
        return meters;
    }

    public Double toKilometers() {
        return meters / 1000.0;
    }
}
