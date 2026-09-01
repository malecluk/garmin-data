package malecluk.garminparser.fileparser;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.garmin.fit.FileIdMesg;
import com.garmin.fit.SessionMesg;

import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;

/**
 * Validates the minimum FIT message set required to create an activity.
 *
 * <p>A valid DTO must contain a {@code FileIdMesg} identifying an Activity
 * file and at least one {@code SessionMesg}. If multiple file ID or session
 * messages are present, only the first one is used by the mapper.</p>
 */
@Component
public class ParsedMessagesDTOValidator {
	private static final Logger log = LogManager.getLogger(ParsedMessagesDTOValidator.class);
	
	/**
	 * Checks whether the parsed FIT messages contain the minimum data required
	 * to create an activity.
	 *
	 * @param mesgsDto parsed FIT messages
	 * @return {@code true} if the DTO represents an activity file with the
	 *         required messages; {@code false} otherwise
	 */
	public boolean isValid(ParsedFitFileMessagesDTO mesgsDto) {
		
		// check if we have FileIdMesg in DTO so we can check if we have activity
		List<FileIdMesg> fileIdMesgList = mesgsDto.getFileIdMesgList();
		
		if (fileIdMesgList == null || fileIdMesgList.isEmpty()) {
			log.error("There is no FileIdMesg, cannot create Activity.");
			return false;
		}
		
		log.debug("FileIdMesg check ok");
		if (fileIdMesgList.size() > 1) {
			log.warn("DTO contains more than one FileIdMesg in list {}, using first message from that list!", fileIdMesgList.size());
		}
		
		// we have FileIdMesg, we can check more
		FileIdMesg firstFileIdMesg = fileIdMesgList.getFirst();
		if (firstFileIdMesg.getType() != com.garmin.fit.File.ACTIVITY) {
			log.error("FIT file is not an Activity file (type: {}).", firstFileIdMesg.getType());
	        return false;
		}
		
		List<SessionMesg> sessionMesgList = mesgsDto.getSessionMesgList();
		if (sessionMesgList == null || sessionMesgList.isEmpty()) {
			log.error("There is no SessionMesg, cannot create Activity.");
			return false;
		}
		
		log.debug("SessionMesg check ok");
		if (sessionMesgList.size() > 1) {
			log.warn("DTO contains more than one SessionMesg in list {}, using first message from that list!", sessionMesgList.size());
		}
		
		return true;
	}
}
