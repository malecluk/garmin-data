package malecluk.garminparser.fileparser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.garmin.fit.Decode;
import com.garmin.fit.MesgBroadcaster;

import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;
import malecluk.garminparser.fileparser.listeners.MyMesgListener;

/**
 * Parses Garmin FIT files and collects their messages into a DTO.
 *
 * <p>The parser first verifies the integrity of the FIT file and then decodes
 * the file using the Garmin FIT SDK. Decoded messages are forwarded to
 * {@link MyMesgListener}, which collects them into a {@link ParsedFitFileMessagesDTO}.</p>
 *
 * <p>The file is opened twice because the integrity check consumes the input
 * stream. A new stream is therefore required for the actual decoding.</p>
 */
@Component
public class FitFileParser {
	
	private static final Logger log = LogManager.getLogger(FitFileParser.class);
    
	/**
     * Parses a Garmin FIT file.
     *
     * <p>The file is first checked for FIT file integrity. If the file is not
     * a valid FIT file, parsing is aborted and {@code null} is returned.</p>
     *
     * <p>For a valid file, all decoded FIT messages are collected by
     * {@link MyMesgListener} and returned as a {@link ParsedFitFileMessagesDTO}.</p>
     *
     * @param filePath path to the FIT file to parse
     * @return parsed FIT messages, or {@code null} if the file is not a valid FIT file
     * @throws IOException if the file cannot be opened or read
     */
    public ParsedFitFileMessagesDTO parseFile(Path filePath) throws IOException {
    	
    	log.info("Processing file: {}", filePath);
    	
    	if (!isValidFitFile(filePath)) {
            log.error("File is not a valid .FIT file: {}", filePath);
            return null;
        }
    	
    	log.debug("File is valid .FIT file.");
    	
		try (InputStream decodeInput = Files.newInputStream(filePath)) {
			Decode decode = new Decode();
			MesgBroadcaster mesgBroadcaster = new MesgBroadcaster();
			
			MyMesgListener mesgListener = new MyMesgListener();
			mesgBroadcaster.addListener(mesgListener);
			
			decode.read(decodeInput, mesgBroadcaster, mesgBroadcaster);
			
			ParsedFitFileMessagesDTO parsedMesgs = mesgListener.getMessages();
			
			return parsedMesgs;
		}
    }
    
    /**
     * Checks whether the specified file is a valid Garmin FIT file.
     *
     * <p>The input stream is opened and closed within this method because
     * {@link Decode#checkFileIntegrity(InputStream)} consumes the stream.
     * The caller must therefore open a new stream when the file is decoded.</p>
     *
     * @param filePath path to the file to validate
     * @return {@code true} if the file passes the Garmin FIT integrity check; {@code false} otherwise
     * @throws IOException if the file cannot be opened or read
     */
    private boolean isValidFitFile(Path filePath) throws IOException {

        try (InputStream input = Files.newInputStream(filePath)) {
            Decode decode = new Decode();
            return decode.checkFileIntegrity(input);
        }
    }
}
