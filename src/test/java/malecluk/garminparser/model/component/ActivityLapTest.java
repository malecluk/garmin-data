package malecluk.garminparser.model.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import malecluk.garminparser.model.value.Distance;
import malecluk.garminparser.model.value.Position;

class ActivityLapTest {

	@Test
	void gettersAndSetters_storeAndReturnValues() {
		ActivityLap lap = new ActivityLap();

		Integer number = 3;
		Instant startTime = Instant.parse("2026-09-15T10:14:00Z");
		Instant endTime = Instant.parse("2026-09-15T10:15:30Z");
		Position startPosition = new Position(50.087451, 14.420671);
		Position endPosition = new Position(50.088100, 14.421500);
		Duration elapsedTime = Duration.ofSeconds(95);
		Duration timerTime = Duration.ofSeconds(90);
		Distance totalDistance = new Distance(1000);
		Float averageSpeed = 3.5f;
		Float maximumSpeed = 5.2f;
		Integer averageHeartRate = 125;
		Integer maximumHeartRate = 145;
		Float averageCadence = 82.5f;
		Float maximumCadence = 95.0f;
		Integer totalCalories = 120;
		Distance totalAscent = new Distance(25);
		Distance totalDescent = new Distance(18);
		Double minimumAltitude = 245.5;
		Double maximumAltitude = 270.0;
		Float averageTemperature = 18.5f;
		Float minimumTemperature = 16.0f;
		Float maximumTemperature = 21.5f;

		lap.setNumber(number);
		lap.setStartTime(startTime);
		lap.setEndTime(endTime);
		lap.setStartPosition(startPosition);
		lap.setEndPosition(endPosition);
		lap.setElapsedTime(elapsedTime);
		lap.setTimerTime(timerTime);
		lap.setTotalDistance(totalDistance);
		lap.setAverageSpeed(averageSpeed);
		lap.setMaximumSpeed(maximumSpeed);
		lap.setAverageHeartRate(averageHeartRate);
		lap.setMaximumHeartRate(maximumHeartRate);
		lap.setAverageCadence(averageCadence);
		lap.setMaximumCadence(maximumCadence);
		lap.setTotalCalories(totalCalories);
		lap.setTotalAscent(totalAscent);
		lap.setTotalDescent(totalDescent);
		lap.setMinimumAltitude(minimumAltitude);
		lap.setMaximumAltitude(maximumAltitude);
		lap.setAverageTemperature(averageTemperature);
		lap.setMinimumTemperature(minimumTemperature);
		lap.setMaximumTemperature(maximumTemperature);

		assertEquals(number, lap.getNumber());
		assertEquals(startTime, lap.getStartTime());
		assertEquals(endTime, lap.getEndTime());
		assertEquals(startPosition, lap.getStartPosition());
		assertEquals(endPosition, lap.getEndPosition());
		assertEquals(elapsedTime, lap.getElapsedTime());
		assertEquals(timerTime, lap.getTimerTime());
		assertEquals(totalDistance, lap.getTotalDistance());
		assertEquals(averageSpeed, lap.getAverageSpeed());
		assertEquals(maximumSpeed, lap.getMaximumSpeed());
		assertEquals(averageHeartRate, lap.getAverageHeartRate());
		assertEquals(maximumHeartRate, lap.getMaximumHeartRate());
		assertEquals(averageCadence, lap.getAverageCadence());
		assertEquals(maximumCadence, lap.getMaximumCadence());
		assertEquals(totalCalories, lap.getTotalCalories());
		assertEquals(totalAscent, lap.getTotalAscent());
		assertEquals(totalDescent, lap.getTotalDescent());
		assertEquals(minimumAltitude, lap.getMinimumAltitude());
		assertEquals(maximumAltitude, lap.getMaximumAltitude());
		assertEquals(averageTemperature, lap.getAverageTemperature());
		assertEquals(minimumTemperature, lap.getMinimumTemperature());
		assertEquals(maximumTemperature, lap.getMaximumTemperature());
	}

	@Test
	void newLap_hasNullValues() {
		ActivityLap lap = new ActivityLap();

		assertNull(lap.getNumber());
		assertNull(lap.getStartTime());
		assertNull(lap.getEndTime());
		assertNull(lap.getStartPosition());
		assertNull(lap.getEndPosition());
		assertNull(lap.getElapsedTime());
		assertNull(lap.getTimerTime());
		assertNull(lap.getTotalDistance());
		assertNull(lap.getAverageSpeed());
		assertNull(lap.getMaximumSpeed());
		assertNull(lap.getAverageHeartRate());
		assertNull(lap.getMaximumHeartRate());
		assertNull(lap.getAverageCadence());
		assertNull(lap.getMaximumCadence());
		assertNull(lap.getTotalCalories());
		assertNull(lap.getTotalAscent());
		assertNull(lap.getTotalDescent());
		assertNull(lap.getMinimumAltitude());
		assertNull(lap.getMaximumAltitude());
		assertNull(lap.getAverageTemperature());
		assertNull(lap.getMinimumTemperature());
		assertNull(lap.getMaximumTemperature());
	}
}
