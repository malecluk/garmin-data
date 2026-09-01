package malecluk.garminparser.model.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

	@Test
	void shouldProvideZeroDistanceConstant() {
		assertEquals(Distance.ZERO, new Distance(0));
	}

	@Test
	void shouldSubtractDistance() {
		Distance distance = new Distance(500);

		assertEquals(new Distance(300), distance.minus(new Distance(200)));
	}

	@Test
	void shouldSubtractDistanceAndProduceNegativeResult() {
		Distance distance = new Distance(200);

		assertEquals(new Distance(-100), distance.minus(new Distance(300)));
	}

	@Test
	void shouldSubtractZeroDistanceWithoutChangingValue() {
		Distance distance = new Distance(500);

		assertEquals(distance, distance.minus(Distance.ZERO));
	}

	@Test
	void shouldBeNegativeForNegativeDistance() {
		assertTrue(new Distance(-1).isNegative());
	}

	@Test
	void shouldNotBeNegativeForZeroDistance() {
		assertFalse(new Distance(0).isNegative());
	}

	@Test
	void shouldNotBeNegativeForPositiveDistance() {
		assertFalse(new Distance(1).isNegative());
	}

	@Test
	void shouldCompareSmallerDistanceAsLessThanLargerDistance() {
		assertTrue(new Distance(100).compareTo(new Distance(200)) < 0);
	}

	@Test
	void shouldCompareEqualDistancesAsEqual() {
		assertEquals(0, new Distance(100).compareTo(new Distance(100)));
	}

	@Test
	void shouldCompareLargerDistanceAsGreaterThanSmallerDistance() {
		assertTrue(new Distance(200).compareTo(new Distance(100)) > 0);
	}

	@Test
	void shouldRejectNullWhenAddingDistance() {
		assertThrows(NullPointerException.class, () -> new Distance(100).add(null));
	}
	
	@Test
	void shouldRejectNullWhenSubstractingDistance() {
		assertThrows(NullPointerException.class, () -> new Distance(100).minus(null));
	}
	
	@Test
	void shouldRejectNullWhenComparingDistance() {
		assertThrows(NullPointerException.class, () -> new Distance(100).compareTo(null));
	}
}
