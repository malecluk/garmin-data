package malecluk.garminparser.fileparser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.garmin.fit.File;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.Sport;

import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;

class ParsedMessagesDTOValidatorTest {

	private ParsedMessagesDTOValidator validator;

	@BeforeEach
	void setUp() {
		validator = new ParsedMessagesDTOValidator();
	}

	@Test
	void isValid_nullDto_returnsFalse() {
		assertFalse(validator.isValid(null));
	}

	@Test
	void isValid_returnsFalse_whenSessionSportIsMissing() {
		ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();
		dto.getFileIdMesgList().add(createFileIdMesg(File.ACTIVITY));
		dto.getSessionMesgList().add(new SessionMesg());
		assertFalse(validator.isValid(dto));
	}

	@Test
	void isValid_returnsFalse_whenFileIdMesgIsMissing() {
		ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();
		dto.getSessionMesgList().add(new SessionMesg());

		assertFalse(validator.isValid(dto));
	}

	@Test
	void isValid_returnsFalse_whenSessionMesgIsMissing() {
		ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();
		dto.getFileIdMesgList().add(createFileIdMesg(File.ACTIVITY));

		assertFalse(validator.isValid(dto));
	}

	@Test
	void isValid_returnsFalse_whenFileIsNotActivity() {
		ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();
		dto.getFileIdMesgList().add(createFileIdMesg(File.WEIGHT));
		dto.getSessionMesgList().add(new SessionMesg());

		assertFalse(validator.isValid(dto));
	}

	@Test
	void isValid_returnsTrue_forValidActivityDto() {
		ParsedFitFileMessagesDTO dto = createValidDto();

		assertTrue(validator.isValid(dto));
	}

	@Test
	void isValid_returnsFalse_whenMultipleSessionMessagesArePresent() {
	    ParsedFitFileMessagesDTO dto = createValidDto();

	    SessionMesg secondSessionMesg = new SessionMesg();
	    secondSessionMesg.setSport(Sport.RUNNING);
	    dto.getSessionMesgList().add(secondSessionMesg);

	    assertFalse(validator.isValid(dto));
	}

	@Test
	void isValid_usesFirstFileIdMessage_whenMultipleFileIdMessagesArePresent() {
		ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();

		dto.getFileIdMesgList().add(createFileIdMesg(File.ACTIVITY));
		dto.getFileIdMesgList().add(createFileIdMesg(File.WEIGHT));

		SessionMesg sessionMesg = new SessionMesg();
		sessionMesg.setSport(Sport.RUNNING);
		dto.getSessionMesgList().add(sessionMesg);

		assertTrue(validator.isValid(dto));
	}

	private ParsedFitFileMessagesDTO createValidDto() {
		ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();

		dto.getFileIdMesgList().add(createFileIdMesg(File.ACTIVITY));
		SessionMesg sessionMesg = new SessionMesg();
		sessionMesg.setSport(Sport.RUNNING);
		dto.getSessionMesgList().add(sessionMesg);

		return dto;
	}

	private FileIdMesg createFileIdMesg(File type) {
		FileIdMesg fileIdMesg = new FileIdMesg();
		fileIdMesg.setType(type);
		return fileIdMesg;
	}
}