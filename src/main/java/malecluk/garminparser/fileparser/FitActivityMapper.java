package malecluk.garminparser.fileparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.garmin.fit.FileIdMesg;
import com.garmin.fit.MesgNum;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.SubSport;
import com.garmin.fit.TimeInZoneMesg;

import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;
import malecluk.garminparser.mappers.ActivityElevationDataMapper;
import malecluk.garminparser.mappers.ActivityMapper;
import malecluk.garminparser.mappers.HeartRateZonesMapper;
import malecluk.garminparser.mappers.OutdoorMovingMapper;
import malecluk.garminparser.mappers.WalkingMapper;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.FloorClimbing;
import malecluk.garminparser.model.activities.Hiking;
import malecluk.garminparser.model.activities.Meditation;
import malecluk.garminparser.model.activities.Rucking;
import malecluk.garminparser.model.activities.Running;
import malecluk.garminparser.model.activities.TrainingActivity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.activities.Yoga;

/**
 * Maps parsed Garmin FIT messages to the application's activity domain model.
 *
 * <p>The mapper determines the activity type from the FIT session sport and
 * delegates sport-specific mapping to the appropriate mapper components.
 * Common session and file metadata are mapped for every supported activity,
 * while additional data such as outdoor movement metrics, elevation data,
 * walking-specific metrics, and heart rate zones are mapped where applicable.</p>
 *
 * <p>Some FIT sports are further distinguished by their sub-sport. For example,
 * hiking with the {@code RUCKING} sub-sport is mapped to {@link Rucking},
 * while training with the {@code YOGA} sub-sport is mapped to {@link Yoga}.</p>
 *
 * <p>If the parsed FIT messages do not pass validation, {@link #map(ParsedFitFileMessagesDTO)}
 * returns {@code null}. Unsupported sports are mapped to a basic {@link Activity}
 * containing only the common activity and file metadata.</p>
 */
@Component
public class FitActivityMapper {
	
	private static final Logger log = LogManager.getLogger(FitActivityMapper.class);
	
	private final ActivityMapper baseActivityMapper;
	private final OutdoorMovingMapper outdoorMovingMapper;
	private final WalkingMapper walkingMapper;
	private final ActivityElevationDataMapper elevationMapper;
	private final HeartRateZonesMapper heartRateZonesMapper;
	private final ParsedMessagesDTOValidator parsedMessagesDTOValidator;
	
	
	public FitActivityMapper(
			ActivityMapper baseActivityMapper, 
			OutdoorMovingMapper outdoorMovingMapper, 
			WalkingMapper walkingMapper,
			ActivityElevationDataMapper elevationMapper,
			HeartRateZonesMapper heartRateZonesMapper,
			ParsedMessagesDTOValidator parsedMessagesDTOValidator) {
		
		this.baseActivityMapper = baseActivityMapper;
		this.outdoorMovingMapper = outdoorMovingMapper;
		this.walkingMapper = walkingMapper;
		this.elevationMapper = elevationMapper;
		this.heartRateZonesMapper = heartRateZonesMapper;
		this.parsedMessagesDTOValidator = parsedMessagesDTOValidator;
	}

	/**
	 * Maps parsed Garmin FIT messages to an application activity.
	 *
	 * <p>The activity type is determined from the sport specified in the first
	 * FIT session message. Sport-specific mapping is then performed according
	 * to the session sport and, where applicable, sub-sport.</p>
	 *
	 * <p>The parsed messages are validated before mapping starts. If validation
	 * fails, {@code null} is returned. If the sport is not explicitly supported,
	 * a basic {@link Activity} containing only common data is returned.</p>
	 *
	 * @param message parsed FIT file messages to map
	 * @return mapped activity, or {@code null} if the parsed messages are invalid
	 */
	public Activity map (ParsedFitFileMessagesDTO message) {
		log.debug("Starting mapping.");
		
		if (!parsedMessagesDTOValidator.isValid(message)) {
			log.error("Error during activity parsing, returning null.");
			return null;
		}
		
		FileIdMesg firstFileIdMesg = message.getFileIdMesgList().getFirst();
		SessionMesg firstSessionMesg = message.getSessionMesgList().getFirst();
		TimeInZoneMesg sessionTimeInZoneMesg = findSessionTimeInZone(message);
		
		log.debug("Session sport: {}", firstSessionMesg.getSport().toString());
		
		return switch (firstSessionMesg.getSport()) {
		    case FLOOR_CLIMBING -> mapFloorClimbing(firstFileIdMesg, firstSessionMesg, sessionTimeInZoneMesg);
		    case HIKING -> mapHiking(firstFileIdMesg, firstSessionMesg, sessionTimeInZoneMesg);
		    case MEDITATION -> mapMeditation(firstFileIdMesg, firstSessionMesg, sessionTimeInZoneMesg);
		    case RUNNING -> mapRunning(firstFileIdMesg, firstSessionMesg, sessionTimeInZoneMesg);
		    case TRAINING -> mapTraining(firstFileIdMesg, firstSessionMesg, sessionTimeInZoneMesg);
		    case WALKING -> mapWalking(firstFileIdMesg, firstSessionMesg, sessionTimeInZoneMesg);
		    
		    default -> mapUnknown(firstFileIdMesg, firstSessionMesg);
		};
	}
	
	/**
	 * Maps parameters common to all activities.
	 *
	 * <p>This includes common session data and FIT file identification data.</p>
	 *
	 * @param activity activity to which the parameters are mapped
	 * @param sessionMesg FIT session message containing activity data
	 * @param fileIdMesg FIT file ID message containing file metadata
	 */
	private void setBaseParams(Activity activity, SessionMesg sessionMesg, FileIdMesg fileIdMesg) {

	    baseActivityMapper.setBaseSessionParams(activity, sessionMesg);
	    baseActivityMapper.setBaseFileIdParams(activity, fileIdMesg);
	}
	
	/**
	 * Maps session heart rate zone data to an activity.
	 *
	 * <p>No mapping is performed when the FIT file does not contain a session-level time-in-zone message.</p>
	 *
	 * @param activity activity to which the heart rate zones are mapped
	 * @param hrZoneMesg FIT time-in-zone message containing heart rate zone data,
	 *        or {@code null} if no session-level message is available
	 */
	private void setHRZones(Activity activity, TimeInZoneMesg hrZoneMesg) {
		if (hrZoneMesg != null) {
			heartRateZonesMapper.map(activity, hrZoneMesg);
		}
	}
	
	/**
	 * Creates and maps a floor climbing activity.
	 *
	 * <p>Common activity data, heart rate zones, and elevation data are mapped
	 * from the corresponding FIT messages.</p>
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zone data, or {@code null} if unavailable
	 * @return mapped floor climbing activity
	 */
	private FloorClimbing mapFloorClimbing(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {
	    log.debug("Processing Floor Climbing.");

	    FloorClimbing activity = new FloorClimbing();

	    setBaseParams(activity, sessionMesg, fileIdMesg);
	    setHRZones(activity, timeInZoneMesg);
	    elevationMapper.setActivityElevationData(activity, sessionMesg);

	    return activity;
	}
	
	/**
	 * Creates and maps a running activity.
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zones
	 * @return mapped running activity
	 */
	private Running mapRunning(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {
	    log.debug("Processing Running.");

	    Running activity = new Running();

	    setBaseParams(activity, sessionMesg, fileIdMesg);
	    setHRZones(activity, timeInZoneMesg);
	    outdoorMovingMapper.setOutdoorMovingSessionParams(activity, sessionMesg);
	    elevationMapper.setActivityElevationData(activity, sessionMesg);

	    return activity;
	}
	
	/**
	 * Creates and maps a walking activity.
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zones
	 * @return mapped walking activity
	 */
	private Walking mapWalking(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {

	    log.debug("Processing Walking.");

	    Walking activity = new Walking();

	    setBaseParams(activity, sessionMesg, fileIdMesg);
	    outdoorMovingMapper.setOutdoorMovingSessionParams(activity, sessionMesg);
	    walkingMapper.setWalkingSessionParams(activity, sessionMesg);
	    elevationMapper.setActivityElevationData(activity, sessionMesg);

	    setHRZones(activity, timeInZoneMesg);

	    return activity;
	}
	
	/**
	 * Maps a hiking session to either a standard hiking or rucking activity.
	 *
	 * <p>A session with the FIT {@code RUCKING} sub-sport is mapped to
	 * {@link Rucking}. All other hiking sessions are mapped to {@link Hiking}.</p>
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity and sub-sport data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zone data, or {@code null} if unavailable
	 * @return mapped rucking or hiking activity
	 */
	private Activity mapHiking(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {

	    if (sessionMesg.getSubSport() == SubSport.RUCKING) {
	        return mapRucking(fileIdMesg, sessionMesg, timeInZoneMesg);
	    }

	    return mapStandardHiking(fileIdMesg, sessionMesg, timeInZoneMesg);
	}
	
	/**
	 * Creates and maps a rucking activity.
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zones
	 * @return mapped rucking activity
	 */
	private Rucking mapRucking(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {

	    log.debug("Processing Rucking.");

	    Rucking activity = new Rucking();

	    setBaseParams(activity, sessionMesg, fileIdMesg);
	    setHRZones(activity, timeInZoneMesg);
	    outdoorMovingMapper.setOutdoorMovingSessionParams(activity, sessionMesg);
	    elevationMapper.setActivityElevationData(activity, sessionMesg);

	    activity.setPackWeightTenthsKg(getPackWeightTenthsKg(sessionMesg)); // TODO re-write to getter if / when available and remove helper method
	    
	    return activity;
	}
	
	/**
	 * Creates and maps a standard hiking activity.
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zones
	 * @return mapped hiking activity
	 */
	private Hiking mapStandardHiking(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {

	    log.debug("Processing Hiking.");

	    Hiking activity = new Hiking();

	    setBaseParams(activity, sessionMesg, fileIdMesg);
	    setHRZones(activity, timeInZoneMesg);
	    outdoorMovingMapper.setOutdoorMovingSessionParams(activity, sessionMesg);
	    elevationMapper.setActivityElevationData(activity, sessionMesg);

	    return activity;
	}
	
	/**
	 * Creates and maps a meditation activity.
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zones
	 * @return mapped meditation activity
	 */
	private Meditation mapMeditation(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {

	    log.debug("Processing Meditation.");

	    Meditation activity = new Meditation();

	    setBaseParams(activity, sessionMesg, fileIdMesg);
		setHRZones(activity, timeInZoneMesg);

	    return activity;
	}
	
	/**
	 * Maps a training session to either a standard training or yoga activity.
	 *
	 * <p>A session with the FIT {@code YOGA} sub-sport is mapped to {@link Yoga}.
	 * All other training sessions are mapped to {@link TrainingActivity}.</p>
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity and sub-sport data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zone data, or {@code null} if unavailable
	 * @return mapped yoga or training activity
	 */
	private Activity mapTraining(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {

	    if (sessionMesg.getSubSport() == SubSport.YOGA) {
	        return mapYoga(fileIdMesg, sessionMesg, timeInZoneMesg);
	    }

	    return mapStandardTraining(fileIdMesg, sessionMesg, timeInZoneMesg);
	}
	
	/**
	 * Creates and maps a yoga activity.
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zones
	 * @return mapped yoga activity
	 */
	private Yoga mapYoga(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {

	    log.debug("Processing Yoga.");

	    Yoga activity = new Yoga();

	    setBaseParams(activity, sessionMesg, fileIdMesg);
	    setHRZones(activity, timeInZoneMesg);

	    return activity;
	}
	
	/**
	 * Creates and maps a standard training activity.
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @param timeInZoneMesg FIT time-in-zone message containing session heart rate zones
	 * @return mapped training activity
	 */
	private TrainingActivity mapStandardTraining(FileIdMesg fileIdMesg, SessionMesg sessionMesg, TimeInZoneMesg timeInZoneMesg) {

	    log.debug("Processing Training.");

	    TrainingActivity activity = new TrainingActivity();

	    setBaseParams(activity, sessionMesg, fileIdMesg);
	    setHRZones(activity, timeInZoneMesg);

	    return activity;
	}
	
	/**
	 * Creates a basic activity for a FIT sport that is not specifically supported.
	 *
	 * <p>Only common session and file metadata are mapped because no
	 * sport-specific mapping is available.</p>
	 *
	 * @param fileIdMesg FIT file ID message containing file metadata
	 * @param sessionMesg FIT session message containing activity data
	 * @return basic activity containing the common mapped parameters
	 */
	private Activity mapUnknown(FileIdMesg fileIdMesg, SessionMesg sessionMesg) {

	    log.warn("Unknown sport ({}), returning only basic Activity.", sessionMesg.getSport());

	    Activity activity = new Activity();

	    setBaseParams(activity, sessionMesg, fileIdMesg);

	    return activity;
	}
	
	/**
	 * Finds the session-level time-in-zone message associated with the activity.
	 *
	 * <p>The FIT file may contain multiple time-in-zone messages for different
	 * reference message types. Only the message referencing {@link MesgNum#SESSION}
	 * is relevant here.</p>
	 *
	 * @param message parsed FIT file messages
	 * @return session-level time-in-zone message, or {@code null} if none is present
	 */
	private TimeInZoneMesg findSessionTimeInZone(ParsedFitFileMessagesDTO message) {

	    for (TimeInZoneMesg mesg : message.getTimeInZoneMesgList()) {
	        if (mesg.getReferenceMesg() == MesgNum.SESSION) {
	            return mesg;
	        }
	    }

	    return null;
	}
	
	/**
	 * Reads the rucking pack weight from Garmin FIT field 220.
	 *
	 * <p>The Garmin FIT SDK currently does not expose a named accessor for this
	 * field, so the field has to be accessed by its numeric identifier.</p>
	 *
	 * <p>The value is expected to be stored in tenths of a kilogram. For example,
	 * a value of {@code 30} represents a pack weight of 3.0 kg.</p>
	 *
	 * @param sessionMesg FIT session message containing the rucking data
	 * @return pack weight in tenths of a kilogram, or {@code null} if the field is missing or has no value
	 */
	private Integer getPackWeightTenthsKg(SessionMesg sessionMesg) {

	    if (sessionMesg.getField(220) == null ||
	        sessionMesg.getField(220).getValue() == null) {
	        return null;
	    }

	    return (Integer) sessionMesg.getField(220).getValue();
	}
}
