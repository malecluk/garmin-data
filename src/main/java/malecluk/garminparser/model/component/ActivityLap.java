package malecluk.garminparser.model.component;

import java.time.Duration;
import java.time.Instant;

import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

/**
 * Represents a single lap of an activity.
 *
 * <p>A lap contains metrics measured or calculated for one segment of an
 * activity, such as its duration, distance, speed, heart rate, cadence,
 * calories, elevation and temperature.</p>
 *
 * <p>Values are represented by object types rather than primitives so that
 * {@code null} can distinguish an unavailable or missing value from a valid
 * value such as zero.</p>
 */
public class ActivityLap {
	
	/**
     * Sequential number identifying the lap within the activity.
     */
	private Integer number;

	/**
     * Time at which the lap started.
     */
    private Instant startTime;
    
    /**
     * Time at which the lap ended.
     */
    private Instant endTime;

    /**
     * Position at which the lap started.
     */
    private Position startPosition;
    
    /**
     * Position at which the lap ended.
     */
    private Position endPosition;

    /**
     * Total elapsed time of the lap, including periods when the activity was not moving.
     */
    private Duration elapsedTime;
    
    /**
     * Total timer time of the lap, representing the time counted by the
     * activity timer.
     */
    private Duration timerTime;

    /**
     * Total distance covered during the lap.
     */
    private Distance totalDistance;

    /**
     * Average speed during the lap.
     */
    private Float averageSpeed;
    
    /**
     * Maximum speed recorded during the lap.
     */
    private Float maximumSpeed;

    /**
     * Average heart rate during the lap.
     */
    private Integer averageHeartRate;
    
    /**
     * Maximum heart rate recorded during the lap.
     */
    private Integer maximumHeartRate;

    /**
     * Average cadence during the lap.
     */
    private Float averageCadence;
    
    /**
     * Maximum cadence recorded during the lap.
     */
    private Float maximumCadence;

    /**
     * Total calories burned during the lap.
     */
    private Integer totalCalories;

    /**
     * Total elevation gain during the lap.
     */
    private Distance totalAscent;
    
    /**
     * Total elevation loss during the lap.
     */
    private Distance totalDescent;

    /**
     * Minimum altitude recorded during the lap.
     */
    private Double minimumAltitude;
    
    /**
     * Maximum altitude recorded during the lap.
     */
    private Double maximumAltitude;

    /**
     * Average temperature recorded during the lap.
     */
    private Float averageTemperature;
    
    /**
     * Minimum temperature recorded during the lap.
     */
    private Float minimumTemperature;
    
    /**
     * Maximum temperature recorded during the lap.
     */
    private Float maximumTemperature;
    
    //-----------------------

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public Position getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(Position startPosition) {
		this.startPosition = startPosition;
	}

	public Position getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(Position endPosition) {
		this.endPosition = endPosition;
	}

	public Duration getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(Duration elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public Duration getTimerTime() {
		return timerTime;
	}

	public void setTimerTime(Duration timerTime) {
		this.timerTime = timerTime;
	}

	public Distance getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(Distance totalDistance) {
		this.totalDistance = totalDistance;
	}

	public Float getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(Float averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public Float getMaximumSpeed() {
		return maximumSpeed;
	}

	public void setMaximumSpeed(Float maximumSpeed) {
		this.maximumSpeed = maximumSpeed;
	}

	public Integer getAverageHeartRate() {
		return averageHeartRate;
	}

	public void setAverageHeartRate(Integer averageHeartRate) {
		this.averageHeartRate = averageHeartRate;
	}

	public Integer getMaximumHeartRate() {
		return maximumHeartRate;
	}

	public void setMaximumHeartRate(Integer maximumHeartRate) {
		this.maximumHeartRate = maximumHeartRate;
	}

	public Float getAverageCadence() {
		return averageCadence;
	}

	public void setAverageCadence(Float averageCadence) {
		this.averageCadence = averageCadence;
	}

	public Float getMaximumCadence() {
		return maximumCadence;
	}

	public void setMaximumCadence(Float maximumCadence) {
		this.maximumCadence = maximumCadence;
	}

	public Integer getTotalCalories() {
		return totalCalories;
	}

	public void setTotalCalories(Integer totalCalories) {
		this.totalCalories = totalCalories;
	}

	public Distance getTotalAscent() {
		return totalAscent;
	}

	public void setTotalAscent(Distance totalAscent) {
		this.totalAscent = totalAscent;
	}

	public Distance getTotalDescent() {
		return totalDescent;
	}

	public void setTotalDescent(Distance totalDescent) {
		this.totalDescent = totalDescent;
	}

	public Double getMinimumAltitude() {
		return minimumAltitude;
	}

	public void setMinimumAltitude(Double minimumAltitude) {
		this.minimumAltitude = minimumAltitude;
	}

	public Double getMaximumAltitude() {
		return maximumAltitude;
	}

	public void setMaximumAltitude(Double maximumAltitude) {
		this.maximumAltitude = maximumAltitude;
	}

	public Float getAverageTemperature() {
		return averageTemperature;
	}

	public void setAverageTemperature(Float averageTemperature) {
		this.averageTemperature = averageTemperature;
	}

	public Float getMinimumTemperature() {
		return minimumTemperature;
	}

	public void setMinimumTemperature(Float minimumTemperature) {
		this.minimumTemperature = minimumTemperature;
	}

	public Float getMaximumTemperature() {
		return maximumTemperature;
	}

	public void setMaximumTemperature(Float maximumTemperature) {
		this.maximumTemperature = maximumTemperature;
	}

    
    
}
