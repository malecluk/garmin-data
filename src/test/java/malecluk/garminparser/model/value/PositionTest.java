package malecluk.garminparser.model.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class PositionTest {

	@Test
	void shouldCreatePositionWithCoordinatesInDegrees() {
		Position position = new Position(50.123456, 14.987654);

		assertEquals(50.123456, position.latitude());
		assertEquals(14.987654, position.longitude());
	}

	@Test
	void shouldAllowNullLatitude() {
		Position position = new Position(null, 14.987654);

		assertNull(position.latitude());
		assertEquals(14.987654, position.longitude());
	}

	@Test
	void shouldAllowNullLongitude() {
		Position position = new Position(50.123456, null);

		assertEquals(50.123456, position.latitude());
		assertNull(position.longitude());
	}

	@Test
	void shouldAllowZeroCoordinates() {
		Position position = new Position(0.0, 0.0);

		assertEquals(0.0, position.latitude());
		assertEquals(0.0, position.longitude());
	}

	@Test
	void shouldReturnNullWhenBothSemicircleCoordinatesAreMissing() {
		assertNull(Position.fromSemicircles(null, null));
	}

	@Test
	void shouldConvertBothSemicircleCoordinatesToDegreesWholeNumbers() {
		Position position = Position.fromSemicircles(536870912, 268435456);

		assertEquals(45.0, position.latitude());
		assertEquals(22.5, position.longitude());
	}

	@Test
	void shouldConvertBothSemicircleCoordinatesToDegrees() {
		Position position = Position.fromSemicircles(596523264, 173147215);

		assertEquals(50.0, position.latitude(), 0.0000025);
		assertEquals(14.513031, position.longitude(), 0.000001);
	}

	@Test
	void shouldKeepLatitudeNullWhenLatitudeIsMissing() {
		Position position = Position.fromSemicircles(null, 173147507);

		assertNull(position.latitude());
		assertEquals(14.513056, position.longitude(), 0.000001);
	}

	@Test
	void shouldKeepLongitudeNullWhenLongitudeIsMissing() {
		Position position = Position.fromSemicircles(596523264, null);

		assertEquals(50.0, position.latitude(), 0.00001);
		assertNull(position.longitude());
	}

	@Test
	void shouldConvertZeroSemicirclesToZeroDegrees() {
		Position position = Position.fromSemicircles(0, 0);

		assertEquals(0.0, position.latitude());
		assertEquals(0.0, position.longitude());
	}
}
