package malecluk.garminparser.fileparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import malecluk.garminparser.FitConfiguration;
import malecluk.garminparser.exceptions.FitFileScanningException;

/**
 * File system scanner for finding .fit files.
 */
@Component
public class FitFileScanner {
	
	private static final Logger log = LogManager.getLogger(FitFileScanner.class);
	
	private final FitConfiguration config;

	public FitFileScanner(FitConfiguration config) {
        this.config = config;
    }
	
	/**
	 * Finds .fit files in the configured directory.
	 * @return list of found .fit files;
	 *         empty list if the directory was successfully scanned but no .fit files were found;
	 * @throws FitFileScanningException if the directory cannot be scanned
	 */
	public List<Path> findFitFiles() {
		String directoryPath = config.filesScanner().directoryPath();
		
		if (directoryPath == null || directoryPath.isBlank()) {
	        log.error("Directory with .fit files is not configured! Needed fit:files-scanner:directory-path:");
	        throw new FitFileScanningException("Directory with .fit files is not configured!");
	    }
		
		final Path directory;

	    try {
	        directory = Paths.get(directoryPath);
	    } catch (InvalidPathException e) {
	        log.error("Invalid directory path configured: '{}'", directoryPath, e);
	        throw new FitFileScanningException("Invalid directory path configured: " + directoryPath, e);
	    }
		
		log.debug("Searching .fit files in: '{}'", directory);
		
		if (!Files.exists(directory)) {
	        log.error("Directory with .fit files does not exist: '{}'", directory);
	        throw new FitFileScanningException("Directory with .fit files does not exist: " + directory);
	    }
		
		if (!Files.isDirectory(directory)) {
	        log.error("Configured path is not a directory: '{}'", directory);
	        throw new FitFileScanningException("Configured path is not a directory: " + directory);
	    }
		
		if (!Files.isReadable(directory)) {
	        log.error("Directory with .fit files is not readable: '{}'", directory);
	        throw new FitFileScanningException("Directory with .fit files is not readable: " +  directory);
	    }
		
		try (Stream<Path> stream = Files.list(directory)) {
			List<Path> files = stream
            		.filter(Files::isRegularFile) // Just files, ignore folders
            		.filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".fit")) // Just .fit files
                    .toList();
			log.debug("Found {} file(s) to process", files.size());
			
			return files;

        } catch (IOException e) {
        	log.error("Failed to read directory '{}': {}", directory, e.getMessage(), e);
        	throw new FitFileScanningException("Failed to read directory: " + directory, e);
        } catch (SecurityException e) {
            log.error("Access to directory '{}' was denied: {}", directory, e.getMessage(), e);
            throw new FitFileScanningException("Access to directory was denied: " + directory, e);
        } 
	}
}
