package malecluk.garminparser.model.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class NumberToDistanceConverterTest {

	private NumberToDistanceConverter converter = new NumberToDistanceConverter();
	
	@Test
	void convert_withInteger_returnsDistance() {
	    Distance result = converter.convert(500);

	    assertEquals(new Distance(500), result);
	}
	
	@Test
	void convert_withDouble_returnsDistance() {
	    Distance result = converter.convert(500.5);

	    assertEquals(new Distance(500.5), result);
	}
	
	@Test
	void convert_withNull_returnsNull() {
	    assertNull(converter.convert(null));
	}
}
