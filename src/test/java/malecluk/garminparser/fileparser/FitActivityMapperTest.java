package malecluk.garminparser.fileparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.garmin.fit.Field;
import com.garmin.fit.File;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.MesgNum;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.Sport;
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
import malecluk.garminparser.model.activities.OutdoorMovingActivity;
import malecluk.garminparser.model.activities.Rucking;
import malecluk.garminparser.model.activities.Running;
import malecluk.garminparser.model.activities.TrainingActivity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.activities.Yoga;

class FitActivityMapperTest {

	@Mock
	private ActivityMapper baseActivityMapper;

	@Mock
	private OutdoorMovingMapper outdoorMovingMapper;

	@Mock
	private WalkingMapper walkingMapper;

	@Mock
	private ActivityElevationDataMapper elevationMapper;

	@Mock
	private HeartRateZonesMapper heartRateZonesMapper;

	@Mock
	private ParsedMessagesDTOValidator parsedMessagesDTOValidator;

	private FitActivityMapper mapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mapper = new FitActivityMapper(baseActivityMapper, outdoorMovingMapper, walkingMapper, elevationMapper,
				heartRateZonesMapper, parsedMessagesDTOValidator);
	}

	// -------------------------------------------------------------------------
	// Validation
	// -------------------------------------------------------------------------

	@Test
	void map_invalidDto_returnsNull() {
		ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();

		when(parsedMessagesDTOValidator.isValid(dto)).thenReturn(false);

		Activity result = mapper.map(dto);

		assertNull(result);

		verify(parsedMessagesDTOValidator).isValid(dto);

		verifyNoInteractions(baseActivityMapper, outdoorMovingMapper, walkingMapper, elevationMapper,
				heartRateZonesMapper);
	}

	// -------------------------------------------------------------------------
	// FLOOR_CLIMBING
	// -------------------------------------------------------------------------

	@Test
	void map_floorClimbing_returnsFloorClimbing() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.FLOOR_CLIMBING, SubSport.GENERIC);

		Activity result = mapper.map(dto);

		assertEquals(FloorClimbing.class, result.getClass());

		verifyBaseParams(dto, result);
		verifyElevation(dto, result);

		verifyNoInteractions(outdoorMovingMapper, walkingMapper, heartRateZonesMapper);
	}

	@Test
	void map_floorClimbing_withSessionHrZone_mapsHrZone() {
		ParsedFitFileMessagesDTO dto = validDtoWithHrZone(Sport.FLOOR_CLIMBING, SubSport.GENERIC, MesgNum.SESSION);

		Activity result = mapper.map(dto);

		assertEquals(FloorClimbing.class, result.getClass());

		verify(heartRateZonesMapper).map(result, dto.getTimeInZoneMesgList().getFirst());
	}

	// -------------------------------------------------------------------------
	// HIKING
	// -------------------------------------------------------------------------

	@Test
	void map_hiking_returnsHiking() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.HIKING, SubSport.GENERIC);

		Activity result = mapper.map(dto);

		assertEquals(Hiking.class, result.getClass());

		verifyBaseParams(dto, result);
		verifyOutdoorMoving(dto, assertInstanceOf(OutdoorMovingActivity.class, result));
		verifyElevation(dto, result);

		verifyNoInteractions(walkingMapper, heartRateZonesMapper);
	}

	@Test
	void map_hiking_withNullSubSport_returnsHiking() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.HIKING, null);

		Activity result = mapper.map(dto);

		assertEquals(Hiking.class, result.getClass());

		verifyBaseParams(dto, result);
		verifyOutdoorMoving(dto, assertInstanceOf(OutdoorMovingActivity.class, result));
		verifyElevation(dto, result);
	}

	// -------------------------------------------------------------------------
	// RUCKING
	// -------------------------------------------------------------------------

	@Test
	void map_hikingRucking_returnsRuckingWithPackWeight() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.HIKING, SubSport.RUCKING);

		SessionMesg session = dto.getSessionMesgList().getFirst();

		Field field = org.mockito.Mockito.mock(Field.class);

		when(session.getField(220)).thenReturn(field);
		when(field.getValue()).thenReturn(30);

		Activity result = mapper.map(dto);

		assertEquals(Rucking.class, result.getClass());

		Rucking rucking = (Rucking) result;

		assertEquals(Integer.valueOf(30), rucking.getPackWeightTenthsKg());

		verifyBaseParams(dto, result);
		verifyOutdoorMoving(dto, assertInstanceOf(OutdoorMovingActivity.class, result));
		verifyElevation(dto, result);

		verifyNoInteractions(walkingMapper, heartRateZonesMapper);
	}

	@Test
	void map_hikingRucking_withoutPackWeight_returnsNullPackWeight() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.HIKING, SubSport.RUCKING);

		SessionMesg session = dto.getSessionMesgList().getFirst();

		when(session.getField(220)).thenReturn(null);

		Activity result = mapper.map(dto);

		assertEquals(Rucking.class, result.getClass());

		Rucking rucking = (Rucking) result;

		assertNull(rucking.getPackWeightTenthsKg());
	}

	@Test
	void map_hikingRucking_withNullPackWeightValue_returnsNullPackWeight() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.HIKING, SubSport.RUCKING);

		SessionMesg session = dto.getSessionMesgList().getFirst();

		Field field = org.mockito.Mockito.mock(Field.class);

		when(session.getField(220)).thenReturn(field);
		when(field.getValue()).thenReturn(null);

		Activity result = mapper.map(dto);

		assertEquals(Rucking.class, result.getClass());

		Rucking rucking = (Rucking) result;

		assertNull(rucking.getPackWeightTenthsKg());
	}

	// -------------------------------------------------------------------------
	// MEDITATION
	// -------------------------------------------------------------------------

	@Test
	void map_meditation_returnsMeditation() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.MEDITATION, SubSport.GENERIC);

		Activity result = mapper.map(dto);

		assertEquals(Meditation.class, result.getClass());

		verifyBaseParams(dto, result);

		verifyNoInteractions(outdoorMovingMapper, walkingMapper, elevationMapper, heartRateZonesMapper);
	}

	@Test
	void map_meditation_withSessionHrZone_mapsHrZone() {
		ParsedFitFileMessagesDTO dto = validDtoWithHrZone(Sport.MEDITATION, SubSport.GENERIC, MesgNum.SESSION);

		Activity result = mapper.map(dto);

		assertEquals(Meditation.class, result.getClass());

		verify(heartRateZonesMapper).map(result, dto.getTimeInZoneMesgList().getFirst());
	}

	// -------------------------------------------------------------------------
	// RUNNING
	// -------------------------------------------------------------------------

	@Test
	void map_running_returnsRunning() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.RUNNING, SubSport.GENERIC);

		Activity result = mapper.map(dto);

		assertEquals(Running.class, result.getClass());

		verifyBaseParams(dto, result);
		verifyOutdoorMoving(dto, assertInstanceOf(OutdoorMovingActivity.class, result));
		verifyElevation(dto, result);

		verifyNoInteractions(walkingMapper, heartRateZonesMapper);
	}

	// -------------------------------------------------------------------------
	// WALKING
	// -------------------------------------------------------------------------

	@Test
	void map_walking_returnsWalking() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.WALKING, SubSport.GENERIC);

		Activity result = mapper.map(dto);

		assertEquals(Walking.class, result.getClass());

		verifyBaseParams(dto, result);
		verifyOutdoorMoving(dto, assertInstanceOf(OutdoorMovingActivity.class, result));
		verifyWalking(dto, assertInstanceOf(Walking.class, result));
		verifyElevation(dto, result);

		verifyNoInteractions(heartRateZonesMapper);
	}

	@Test
	void map_walking_withSessionHrZone_mapsHrZone() {
		ParsedFitFileMessagesDTO dto = validDtoWithHrZone(Sport.WALKING, SubSport.GENERIC, MesgNum.SESSION);

		Activity result = mapper.map(dto);

		assertEquals(Walking.class, result.getClass());

		verify(heartRateZonesMapper).map(result, dto.getTimeInZoneMesgList().getFirst());
	}

	@Test
	void map_walking_withOnlyLapHrZone_doesNotMapHrZone() {
		ParsedFitFileMessagesDTO dto = validDtoWithHrZone(Sport.WALKING, SubSport.GENERIC, MesgNum.LAP);

		Activity result = mapper.map(dto);

		assertEquals(Walking.class, result.getClass());

		verifyNoInteractions(heartRateZonesMapper);
	}

	@Test
	void map_walking_withSessionAndLapHrZones_usesSessionHrZone() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.WALKING, SubSport.GENERIC);

		TimeInZoneMesg lapZone = mockTimeInZone(MesgNum.LAP);
		TimeInZoneMesg sessionZone = mockTimeInZone(MesgNum.SESSION);

		dto.getTimeInZoneMesgList().add(lapZone);
		dto.getTimeInZoneMesgList().add(sessionZone);

		Activity result = mapper.map(dto);

		verify(heartRateZonesMapper).map(result, sessionZone);
		verify(heartRateZonesMapper, never()).map(result, lapZone);
	}

	// -------------------------------------------------------------------------
	// TRAINING
	// -------------------------------------------------------------------------

	@Test
	void map_training_returnsTrainingActivity() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.TRAINING, SubSport.GENERIC);

		Activity result = mapper.map(dto);

		assertEquals(TrainingActivity.class, result.getClass());

		verifyBaseParams(dto, result);

		verifyNoInteractions(outdoorMovingMapper, walkingMapper, elevationMapper, heartRateZonesMapper);
	}

	@Test
	void map_training_withNullSubSport_returnsTrainingActivity() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.TRAINING, null);

		Activity result = mapper.map(dto);

		assertEquals(TrainingActivity.class, result.getClass());

		verifyBaseParams(dto, result);
	}

	@Test
	void map_trainingYoga_returnsYoga() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.TRAINING, SubSport.YOGA);

		Activity result = mapper.map(dto);

		assertEquals(Yoga.class, result.getClass());

		verifyBaseParams(dto, result);

		verifyNoInteractions(outdoorMovingMapper, walkingMapper, elevationMapper);
	}

	@Test
	void map_trainingYoga_withSessionHrZone_mapsHrZone() {
		ParsedFitFileMessagesDTO dto = validDtoWithHrZone(Sport.TRAINING, SubSport.YOGA, MesgNum.SESSION);

		Activity result = mapper.map(dto);

		assertEquals(Yoga.class, result.getClass());

		verify(heartRateZonesMapper).map(result, dto.getTimeInZoneMesgList().getFirst());
	}

	// -------------------------------------------------------------------------
	// UNKNOWN SPORT
	// -------------------------------------------------------------------------

	@Test
	void map_unknownSport_returnsBasicActivity() {
		ParsedFitFileMessagesDTO dto = validDto(Sport.CYCLING, SubSport.GENERIC);

		Activity result = mapper.map(dto);

		assertEquals(Activity.class, result.getClass());

		verifyBaseParams(dto, result);

		verifyNoInteractions(outdoorMovingMapper, walkingMapper, elevationMapper, heartRateZonesMapper);
	}

	// -------------------------------------------------------------------------
	// Null sport
	// -------------------------------------------------------------------------

	@Test
	void map_nullSport_throwsNullPointerException() {
		ParsedFitFileMessagesDTO dto = validDto(null, SubSport.GENERIC);

		assertThrows(NullPointerException.class, () -> mapper.map(dto));
	}

	// -------------------------------------------------------------------------
	// Test helpers
	// -------------------------------------------------------------------------

	private ParsedFitFileMessagesDTO validDto(Sport sport, SubSport subSport) {

		ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();

		FileIdMesg fileIdMesg = org.mockito.Mockito.mock(FileIdMesg.class);
		SessionMesg sessionMesg = org.mockito.Mockito.mock(SessionMesg.class);

		when(fileIdMesg.getType()).thenReturn(File.ACTIVITY);
		when(sessionMesg.getSport()).thenReturn(sport);
		when(sessionMesg.getSubSport()).thenReturn(subSport);

		dto.getFileIdMesgList().add(fileIdMesg);
		dto.getSessionMesgList().add(sessionMesg);

		when(parsedMessagesDTOValidator.isValid(dto)).thenReturn(true);

		return dto;
	}

	private ParsedFitFileMessagesDTO validDtoWithHrZone(Sport sport, SubSport subSport, Integer referenceMesg) {

		ParsedFitFileMessagesDTO dto = validDto(sport, subSport);

		dto.getTimeInZoneMesgList().add(mockTimeInZone(referenceMesg));

		return dto;
	}

	private TimeInZoneMesg mockTimeInZone(Integer referenceMesg) {
		TimeInZoneMesg mesg = mock(TimeInZoneMesg.class);

		when(mesg.getReferenceMesg()).thenReturn(referenceMesg);

		return mesg;
	}

	private void verifyBaseParams(ParsedFitFileMessagesDTO dto, Activity result) {
		verify(baseActivityMapper).setBaseSessionParams(result, dto.getSessionMesgList().getFirst());
		verify(baseActivityMapper).setBaseFileIdParams(result, dto.getFileIdMesgList().getFirst());
	}

	private void verifyOutdoorMoving(ParsedFitFileMessagesDTO dto, OutdoorMovingActivity result) {
		verify(outdoorMovingMapper).setOutdoorMovingSessionParams(result, dto.getSessionMesgList().getFirst());
	}

	private void verifyWalking(ParsedFitFileMessagesDTO dto, Walking result) {
		verify(walkingMapper).setWalkingSessionParams(result, dto.getSessionMesgList().getFirst());
	}

	private void verifyElevation(ParsedFitFileMessagesDTO dto, Activity result) {
		verify(elevationMapper).setActivityElevationData(result, dto.getSessionMesgList().getFirst());
	}
}
