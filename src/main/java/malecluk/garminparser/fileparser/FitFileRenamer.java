package malecluk.garminparser.fileparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import malecluk.garminparser.FitConfiguration;
import malecluk.garminparser.model.activities.Activity;


/**
 * Renames FIT files according to the data stored in the corresponding activity. 
 * 
 * <p>Only files with an original name consisting of exactly eleven digits, 
 * optionally followed by {@code _ACTIVITY}, and ending with {@code .fit} are renamed.</p> 
 * 
 * <p>The new file name contains the original file number, activity start time, sport and sub-sport, for example: 
 * {@code 24375337180_2026-09-15_21-12-51_FLOOR_CLIMBING-GENERIC.fit}.</p> 
 * 
 * <p>File renaming can be enabled or disabled through the application configuration.</p> 
 */
@Component
public class FitFileRenamer {
	
	private static final Logger log = LogManager.getLogger(FitFileRenamer.class);
	
	private final FitConfiguration config;
	
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^(\\d{11})(?:_ACTIVITY)?\\.fit$");
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withZone(ZoneId.systemDefault());
	
	public FitFileRenamer(FitConfiguration config) {
        this.config = config;
    }
	
	/** 
	 * Renames the FIT file associated with the specified activity. 
	 * 
	 * <p>If file renaming is disabled in the configuration, this method does 
	 * nothing. The method also does nothing if the activity has no associated ¨
	 * file or if the current file name does not match the expected original 
	 * FIT file name format.</p> 
	 * 
	 * <p>After a successful rename, the activity is updated so that its file 
	 * path points to the newly renamed file.</p> 
	 * 
	 * @param activity activity whose associated FIT file should be renamed 
	 * @throws IOException if the file cannot be moved to the new name 
	 */
	public void rename(Activity activity) throws IOException {
		if (!config.fileRenamer().enabled()) {
			return;
		}
		
		Path filePath = activity.getFilePath();
		
		if (filePath == null) {
	        log.warn("Cannot rename activity file because file path is missing.");
	        return;
	    }
		
		String fileName = filePath.getFileName().toString();

	    Matcher matcher = FILE_NAME_PATTERN.matcher(fileName);

	    if (!matcher.matches()) {
	        return;
	    }
	    
	    String number = matcher.group(1);
	    String formattedTime = DATE_FORMAT.format(activity.getStartTime());
	    
	    String newName = number + "_" + formattedTime + "_"  + activity.getSport() + "-" + activity.getSubSport() + ".fit";
	    
	    Path target = filePath.resolveSibling(newName);
	    
	    if (Files.exists(target)) {
	        log.warn("Cannot rename FIT file '{}' because target '{}' already exists.", fileName, target.getFileName());
	        return;
	    }

	    Files.move(filePath, target);

	    log.info("Renamed FIT file '{}' to '{}'.", fileName, newName);

	    activity.setFilePath(target);
	}

}
