package malecluk.garminparser.fileparser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.garmin.fit.File;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.SessionMesg;

import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;

class ParsedMessagesDTOValidatorTest {

    private ParsedMessagesDTOValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ParsedMessagesDTOValidator();
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
    void isValid_returnsTrue_whenMultipleSessionMessagesArePresent() {
        ParsedFitFileMessagesDTO dto = createValidDto();
        dto.getSessionMesgList().add(new SessionMesg());

        assertTrue(validator.isValid(dto));
    }

    @Test
    void isValid_usesFirstFileIdMessage_whenMultipleFileIdMessagesArePresent() {
        ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();

        dto.getFileIdMesgList().add(createFileIdMesg(File.ACTIVITY));
        dto.getFileIdMesgList().add(createFileIdMesg(File.WEIGHT));
        dto.getSessionMesgList().add(new SessionMesg());

        assertTrue(validator.isValid(dto));
    }

    private ParsedFitFileMessagesDTO createValidDto() {
        ParsedFitFileMessagesDTO dto = new ParsedFitFileMessagesDTO();

        dto.getFileIdMesgList().add(createFileIdMesg(File.ACTIVITY));
        dto.getSessionMesgList().add(new SessionMesg());

        return dto;
    }

    private FileIdMesg createFileIdMesg(File type) {
        FileIdMesg fileIdMesg = new FileIdMesg();
        fileIdMesg.setType(type);
        return fileIdMesg;
    }
}