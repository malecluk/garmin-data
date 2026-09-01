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
import malecluk.garminparser.mappers.ActivityLapsMapper;
import malecluk.garminparser.mappers.ActivityMapper;
import malecluk.garminparser.mappers.ActivitySetsMapper;
import malecluk.garminparser.mappers.HeartRateZonesMapper;
import malecluk.garminparser.mappers.TrackPointMapper;
import malecluk.garminparser.mappers.sportspecific.OutdoorMovingMapper;
import malecluk.garminparser.mappers.sportspecific.RuckingMapper;
import malecluk.garminparser.mappers.sportspecific.WalkingMapper;
import malecluk.garminparser.model.ActivityId;
import malecluk.garminparser.model.ActivityIds;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Breathing;
import malecluk.garminparser.model.activities.FloorClimbing;
import malecluk.garminparser.model.activities.Hiking;
import malecluk.garminparser.model.activities.Meditation;
import malecluk.garminparser.model.activities.OutdoorMovingActivity;
import malecluk.garminparser.model.activities.Rucking;
import malecluk.garminparser.model.activities.Running;
import malecluk.garminparser.model.activities.TrainingActivity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.activities.Yoga;

/**
 * Maps parsed Garmin FIT messages to the application's activity domain model.
 *
 * <p>
 * The mapper determines the activity type from the FIT session sport and, where
 * applicable, its sub-sport, and delegates sport-specific mapping to the
 * appropriate mapper components. Common session and file metadata are mapped
 * for every activity, while additional data such as outdoor movement metrics,
 * elevation data, walking-specific metrics, heart rate zones, laps, and sets
 * are mapped where applicable.
 * </p>
 *
 * <p>
 * Some FIT sports are further distinguished by their sub-sport. For example,
 * hiking with the {@code RUCKING} sub-sport is mapped to {@link Rucking}, while
 * training with the {@code YOGA} sub-sport is mapped to {@link Yoga} and
 * training with the {@code BREATHING} sub-sport is mapped to {@link Breathing}.
 * </p>
 *
 * <p>
 * If the parsed FIT messages do not pass validation,
 * {@link #map(ParsedFitFileMessagesDTO)} returns {@code null}. Unsupported
 * sports are mapped to a basic {@link Activity} containing only the common
 * activity and file metadata.
 * </p>
 */
@Component
public class FitActivityMapper {

	private static final Logger log = LogManager.getLogger(FitActivityMapper.class);

	private final ActivityMapper baseActivityMapper;
	private final OutdoorMovingMapper outdoorMovingMapper;
	private final WalkingMapper walkingMapper;
	private final ActivityElevationDataMapper elevationMapper;
	private final HeartRateZonesMapper heartRateZonesMapper;
	private final ActivityLapsMapper activityLapsMapper;
	private final ActivitySetsMapper activitySetsMapper;
	private final RuckingMapper ruckingMapper;
	private final ParsedMessagesDTOValidator parsedMessagesDTOValidator;
	private final TrackPointMapper trackPointMapper;

	public FitActivityMapper(ActivityMapper baseActivityMapper, OutdoorMovingMapper outdoorMovingMapper,
			WalkingMapper walkingMapper, ActivityElevationDataMapper elevationMapper,
			HeartRateZonesMapper heartRateZonesMapper, ActivityLapsMapper activityLapsMapper,
			ActivitySetsMapper activitySetsMapper, RuckingMapper ruckingMapper, TrackPointMapper trackPointMapper, ParsedMessagesDTOValidator parsedMessagesDTOValidator) {

		this.baseActivityMapper = baseActivityMapper;
		this.outdoorMovingMapper = outdoorMovingMapper;
		this.walkingMapper = walkingMapper;
		this.elevationMapper = elevationMapper;
		this.heartRateZonesMapper = heartRateZonesMapper;
		this.activityLapsMapper = activityLapsMapper;
		this.activitySetsMapper = activitySetsMapper;
		this.ruckingMapper = ruckingMapper;
		this.trackPointMapper = trackPointMapper;
		this.parsedMessagesDTOValidator = parsedMessagesDTOValidator;
	}

	/**
	 * Maps parsed Garmin FIT messages to an application activity.
	 *
	 * <p>
	 * The activity type is determined from the sport specified in the first FIT
	 * session message. Sport-specific mapping is then performed according to the
	 * session sport and, where applicable, sub-sport.
	 * </p>
	 *
	 * <p>
	 * The parsed messages are validated before mapping starts. If validation fails,
	 * {@code null} is returned. If the sport is not explicitly supported, a basic
	 * {@link Activity} containing only common data is returned.
	 * </p>
	 *
	 * @param message parsed FIT file messages to map
	 * @return mapped activity, or {@code null} if the parsed messages are invalid
	 */
	public Activity map(ParsedFitFileMessagesDTO message) {
		log.debug("Starting mapping.");

		if (!parsedMessagesDTOValidator.isValid(message)) {
			log.error("Error during activity parsing, returning null.");
			return null;
		}

		FileIdMesg firstFileIdMesg = message.getFileIdMesgList().getFirst();
		SessionMesg firstSessionMesg = message.getSessionMesgList().getFirst();
		ActivityIds activityIds = createActivityIds(firstFileIdMesg, firstSessionMesg, message);

		log.debug("Session sport: {}", firstSessionMesg.getSport().toString());

		Activity activity = switch (firstSessionMesg.getSport()) {
		case FLOOR_CLIMBING -> mapFloorClimbing(firstSessionMesg, activityIds);

		case HIKING -> mapHiking(firstSessionMesg, activityIds);

		case MEDITATION -> mapMeditation(activityIds);

		case RUNNING -> mapRunning(activityIds);

		case TRAINING -> mapTraining(firstSessionMesg, activityIds);

		case WALKING -> mapWalking(firstSessionMesg, activityIds);

		default -> mapUnknown(firstSessionMesg, activityIds);
		};

		mapCommonData(activity, firstFileIdMesg, firstSessionMesg, message);

		if (activity instanceof OutdoorMovingActivity outdoorMoving) {
			outdoorMovingMapper.setOutdoorMovingSessionParams(outdoorMoving, firstSessionMesg);
			outdoorMoving.setTrack(trackPointMapper.map(message.getRecordMesgList()));
		}

		return activity;
	}

	private void mapCommonData(Activity activity, FileIdMesg fileIdMesg, SessionMesg sessionMesg,
			ParsedFitFileMessagesDTO message) {

		baseActivityMapper.setBaseSessionParams(activity, sessionMesg);
		baseActivityMapper.setBaseFileIdParams(activity, fileIdMesg);

		TimeInZoneMesg sessionTimeInZoneMesg = findSessionTimeInZone(message);
		if (sessionTimeInZoneMesg != null) {
			heartRateZonesMapper.map(activity, sessionTimeInZoneMesg);
		}
		
		elevationMapper.setActivityElevationData(activity, sessionMesg);

		activityLapsMapper.map(activity, message.getLapMesgList());
		activitySetsMapper.map(activity, message.getSetMesgList());
	}

	/**
	 * Creates floor climbing activity and maps a floor climbing specific data.
	 *
	 * @param sessionMesg FIT session message containing activity data
	 * @param activityIds identifiers associated with the activity
	 * @return mapped floor climbing activity
	 */
	private FloorClimbing mapFloorClimbing(SessionMesg sessionMesg, ActivityIds activityIds) {
		log.debug("Processing Floor Climbing.");

		FloorClimbing activity = new FloorClimbing(activityIds);

		return activity;
	}

	/**
	 * Creates running activity and maps a running specific data.
	 *
	 * @param activityIds identifiers associated with the activity
	 * @return mapped running activity
	 */
	private Running mapRunning(ActivityIds activityIds) {
		log.debug("Processing Running.");

		Running activity = new Running(activityIds);

		return activity;
	}

	/**
	 * Creates walking activity and maps a walking specific data.
	 *
	 * @param sessionMesg FIT session message containing activity data
	 * @param activityIds identifiers associated with the activity
	 * @return mapped walking activity
	 */
	private Walking mapWalking(SessionMesg sessionMesg, ActivityIds activityIds) {

		log.debug("Processing Walking.");

		Walking activity = new Walking(activityIds);

		walkingMapper.setWalkingSessionParams(activity, sessionMesg);

		return activity;
	}

	/**
	 * Creates standard hiking or rucking activity and maps a their specific data.
	 *
	 * <p>
	 * A session with the FIT {@code RUCKING} sub-sport is mapped to
	 * {@link Rucking}. All other hiking sessions are mapped to {@link Hiking}.
	 * </p>
	 *
	 * @param sessionMesg FIT session message containing activity and sub-sport data
	 * @param activityIds identifiers associated with the activity
	 * @return mapped rucking or hiking activity
	 */
	private Activity mapHiking(SessionMesg sessionMesg, ActivityIds activityIds) {
		
		if (sessionMesg.getSubSport() == SubSport.RUCKING) {
			return mapRucking(sessionMesg, activityIds);
		}

		return mapStandardHiking(activityIds);
	}

	/**
	 * Creates rucking activity and maps a rucking specific data.
	 *
	 * @param sessionMesg FIT session message containing activity data
	 * @param activityIds identifiers associated with the activity
	 * @return mapped rucking activity
	 */
	private Rucking mapRucking(SessionMesg sessionMesg, ActivityIds activityIds) {

		log.debug("Processing Rucking.");

		Rucking activity = new Rucking(activityIds);
		ruckingMapper.setRuckingSessionParams(activity, sessionMesg);
		
		return activity;
	}

	/**
	 * Creates standard hiking activity and maps a standard hiking specific data.
	 * 
	 * @param activityIds identifiers associated with the activity
	 * @return mapped hiking activity
	 */
	private Hiking mapStandardHiking(ActivityIds activityIds) {

		log.debug("Processing Hiking.");

		Hiking activity = new Hiking(activityIds);

		return activity;
	}

	/**
	 * Creates meditation activity and maps a meditation specific data.
	 *
	 * @param activityIds identifiers associated with the activity
	 * @return mapped meditation activity
	 */
	private Meditation mapMeditation(ActivityIds activityIds) {

		log.debug("Processing Meditation.");

		Meditation activity = new Meditation(activityIds);

		return activity;
	}

	/**
	 * 
	 * Creates standard training, yoga or breathing activity and maps a their
	 * specific data.
	 * 
	 *
	 * <p>
	 * A session with the FIT {@code YOGA} sub-sport is mapped to {@link Yoga}. A
	 * session with the FIT {@code BREATHING} sub-sport is mapped to
	 * {@link Breathing}. All other training sessions are mapped to
	 * {@link TrainingActivity}.
	 * </p>
	 *
	 * @param sessionMesg FIT session message containing activity and sub-sport
	 * @param activityIds identifiers associated with the activity
	 * @return mapped yoga, breathing or training activity
	 */
	private Activity mapTraining(SessionMesg sessionMesg, ActivityIds activityIds) {

		if (sessionMesg.getSubSport() == SubSport.YOGA) {
			return mapYoga(activityIds);
		} else if (sessionMesg.getSubSport() == SubSport.BREATHING) {
			return mapBreathing(activityIds);
		}

		return mapStandardTraining(activityIds);
	}

	/**
	 * Creates yoga activity and maps a yoga specific data.
	 *
	 * @param activityIds identifiers associated with the activity
	 * @return mapped yoga activity
	 */
	private Yoga mapYoga(ActivityIds activityIds) {

		log.debug("Processing Yoga.");

		Yoga activity = new Yoga(activityIds);

		return activity;
	}

	/**
	 * Creates breathing activity and maps a breathing specific data.
	 * 
	 * @param activityIds identifiers associated with the activity
	 * @return mapped breathing activity
	 */
	private Breathing mapBreathing(ActivityIds activityIds) {

		log.debug("Processing Breathing.");

		Breathing activity = new Breathing(activityIds);

		return activity;
	}

	/**
	 * Creates standard training activity and maps a standard training specific
	 * data.
	 *
	 * @param activityIds identifiers associated with the activity
	 * @return mapped training activity
	 */
	private TrainingActivity mapStandardTraining(ActivityIds activityIds) {

		log.debug("Processing Training.");

		TrainingActivity activity = new TrainingActivity(activityIds);

		return activity;
	}

	/**
	 * Creates a basic activity for a FIT sport that is not specifically supported.
	 *
	 * <p>
	 * Only common session and file metadata are mapped because no sport-specific
	 * mapping is available. It is possible that another details such as laps, heart
	 * rates, sets etc. will be mapped by specific mappers.
	 * </p>
	 *
	 * @param sessionMesg FIT session message containing activity data
	 * @param activityIds identifiers associated with the activity
	 * @return basic activity containing the common mapped parameters
	 */
	private Activity mapUnknown(SessionMesg sessionMesg, ActivityIds activityIds) {

		log.warn("Unknown sport ({}), returning only basic Activity.", sessionMesg.getSport());

		Activity activity = new Activity(activityIds);

		return activity;
	}

	/**
	 * Finds the session-level time-in-zone message associated with the activity.
	 *
	 * <p>
	 * The FIT file may contain multiple time-in-zone messages for different
	 * reference message types. Only the message referencing {@link MesgNum#SESSION}
	 * is relevant here.
	 * </p>
	 *
	 * @param message parsed FIT file messages
	 * @return session-level time-in-zone message, or {@code null} if none is
	 *         present
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
	 * Creates activity identifiers from FIT file and session metadata.
	 *
	 * <p>
	 * The activity identifier is composed of the FIT device serial number and the
	 * session start timestamp. The source file hash is included in the resulting
	 * {@link ActivityIds} as an identifier of the original FIT file.
	 * </p>
	 *
	 * @param fileIdMesg  FIT file ID message containing the device serial number
	 * @param sessionMesg FIT session message containing the activity start time
	 * @param message     parsed FIT file messages containing the source file hash
	 * @return activity identifiers derived from the FIT file metadata
	 */
	private ActivityIds createActivityIds(FileIdMesg fileIdMesg, SessionMesg sessionMesg,
			ParsedFitFileMessagesDTO message) {

		String activityIdValue = fileIdMesg.getSerialNumber() + "-" + sessionMesg.getStartTime().getTimestamp();

		ActivityId activityId = new ActivityId(activityIdValue);

		return new ActivityIds(activityId, message.getSourceFileHash());
	}
}
