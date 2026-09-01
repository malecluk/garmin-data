package malecluk.garminparser.model.value;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts numeric configuration values to {@link Distance} values.
 *
 * <p>
 * The source value is interpreted as a distance in meters.
 * </p>
 */
@Component
@ConfigurationPropertiesBinding
public class NumberToDistanceConverter implements Converter<Number, Distance> {

	/**
	 * Converts a numeric value representing meters to a {@link Distance}.
	 *
	 * @param source numeric distance in meters, or {@code null}
	 * @return distance represented by the source value, or {@code null} if the
	 *         source is {@code null}
	 */
	@Override
	public Distance convert(Number source) {
		if (source == null) {
			return null;
		}

		return new Distance(source.doubleValue());
	}
}
