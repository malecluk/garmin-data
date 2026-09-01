package malecluk.garminparser.model.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DistanceTest {

	@Test
	void shouldConvertMetersToKilometers() {
		Distance distance = new Distance(1000);

		assertEquals(1.0, distance.toKilometers(), 0.000000001);
	}

	@Test
	void shouldConvertHalfKilometer() {
		Distance distance = new Distance(500);

		assertEquals(0.5, distance.toKilometers(), 0.000000001);
	}

	@Test
	void shouldConvertZeroMetersToZeroKilometers() {
		Distance distance = new Distance(0);

		assertEquals(0.0, distance.toKilometers(), 0.000000001);
	}

	@Test
	void shouldPreserveDecimalKilometers() {
		Distance distance = new Distance(5086.46);

		assertEquals(5.08646, distance.toKilometers(), 0.000000001);
	}

	@Test
	void shouldConvertDistanceWithDecimalMeters() {
		Distance distance = new Distance(1234.56);

		assertEquals(1.23456, distance.toKilometers(), 0.000000001);
	}

	@Test
	void shouldPreserveMeters() {
		Distance distance = new Distance(5086.46);

		assertEquals(5086.46, distance.toMeters(), 0.000000001);
	}
}
