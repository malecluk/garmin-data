package malecluk.garminparser.mappers.sportspecific;

import org.springframework.stereotype.Component;

import com.garmin.fit.SessionMesg;
import malecluk.garminparser.model.activities.Rucking;

/**
 * Maps rucking-specific data from a FIT session message to a {@link Rucking} activity.
 */
@Component
public class RuckingMapper {
	
	/**
	 * Maps rucking-specific session parameters.
	 *
	 * @param r rucking activity to update
	 * @param m FIT session message containing rucking-specific data
	 */
	public void setRuckingSessionParams(Rucking r, SessionMesg m) {
		r.setPackWeightTenthsKg(getPackWeightTenthsKg(m)); // TODO re-write to getter if / when available and remove helper method
	}

	/**
	 * Reads the rucking pack weight from Garmin FIT field 220.
	 *
	 * <p>
	 * The Garmin FIT SDK currently does not expose a named accessor for this field,
	 * so the field has to be accessed by its numeric identifier.
	 * </p>
	 *
	 * <p>
	 * The value is expected to be stored in tenths of a kilogram. For example, a
	 * value of {@code 30} represents a pack weight of 3.0 kg.
	 * </p>
	 *
	 * @param sessionMesg FIT session message containing the rucking data
	 * @return pack weight in tenths of a kilogram, or {@code null} if the field is
	 *         missing or has no value
	 */
	private Integer getPackWeightTenthsKg(SessionMesg sessionMesg) {

		if (sessionMesg.getField(220) == null || sessionMesg.getField(220).getValue() == null) {
			return null;
		}

		return (Integer) sessionMesg.getField(220).getValue();
	}
}
