package malecluk.garminparser;

import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import malecluk.garminparser.fileparser.FitActivityMapper;
import malecluk.garminparser.fileparser.FitFileParser;
import malecluk.garminparser.fileparser.FitFileRenamer;
import malecluk.garminparser.fileparser.FitFileScanner;
import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;
import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.processing.ActivityProcessor;
import malecluk.garminparser.processing.results.ActivityProcessingResult;
import malecluk.garminparser.processing.results.ActivityResultConsolePrinter;
import malecluk.garminparser.utils.ActivityConsolePrinter;
import malecluk.garminparser.utils.ParsedFitFileMessagesDTOPrinter;

/**
 * Orchestrates the processing of Garmin FIT files.
 *
 * <p>The runner scans the configured directory for FIT files, parses each file,
 * maps the parsed messages to an application domain activity, optionally renames
 * the source file, and passes the activity to all configured analyzers.</p>
 *
 * <p>After all files have been processed, the results produced by the analyzers
 * are printed to the console.</p>
 */
@Component
public class FitApplicationRunner implements CommandLineRunner {
	
	private final FitFileParser fitFileParser; // reads messages from one file
	private final FitFileScanner fileScanner; // finds files on disk
	private final FitActivityMapper activityMapper; // maps messages to application domain model
	private final ActivityProcessor activityProcessor;
	private final ActivityResultConsolePrinter resultPrinter;
	
	@Autowired
    private ParsedFitFileMessagesDTOPrinter dtoPrinter;
	
	@Autowired
	private ActivityConsolePrinter consolePrinter;
	
	@Autowired
	private FitFileRenamer fitFileRenamer;
	
	public FitApplicationRunner(
			FitFileParser fitFileParser, 
			FitFileScanner fileScanner, 
			FitActivityMapper activityMapper,
			ActivityProcessor activityProcessor,
			ActivityResultConsolePrinter resultPrinter) {
		
        this.fitFileParser = fitFileParser;
        this.fileScanner = fileScanner;
        this.activityMapper = activityMapper;
        this.activityProcessor = activityProcessor;
        this.resultPrinter = resultPrinter;
    }

	/**
	 * Processes all FIT files found in the configured input directory.
	 *
	 * <p>Each FIT file is parsed into a {@link ParsedFitFileMessagesDTO}, mapped to
	 * an {@link Activity}, optionally renamed according to the activity data, and
	 * passed to the configured activity analyzers. After all files have been
	 * processed, the aggregated analyzer results are printed.</p>
	 *
	 * @param args command-line arguments passed to the Spring Boot application
	 * @throws Exception if processing of a FIT file fails
	 */
    @Override
    public void run(String... args) throws Exception {

    	// read files from directory stored in config in application.yml file
		List<Path> files = fileScanner.findFitFiles();
		
		// TODO remove - for debug only, filtering list of files for just one file
		files = files.stream()
				//.filter(p -> p.getFileName().toString().startsWith("24375337180")) // FLOOR_CLIMBING - for debug only
				//.filter(p -> p.getFileName().toString().startsWith("24378906759")) // MEDITATION - for debug only
				//.filter(p -> p.getFileName().toString().startsWith("24375316476")) // HIKING - RUCKING - for debug only
				//.filter(p -> p.getFileName().toString().startsWith("24235915590")) // RUNNING - for debug only
				.filter(p -> p.getFileName().toString().startsWith("24433414568")) // WALKING - for debug only
				//.filter(p -> p.getFileName().toString().startsWith("24257393733")) // TRAINING - YOGA - for debug only
				//
				//.filter(p -> p.getFileName().toString().startsWith("24384540935") || p.getFileName().toString().startsWith("24394793751")) // for debug only
				.toList();		
		
		// parsing activities
		for (Path file : files) {
			ParsedFitFileMessagesDTO parsedMesgs = fitFileParser.parseFile(file);
			dtoPrinter.print(parsedMesgs);
			
			Activity act = activityMapper.map(parsedMesgs);
			act.setFilePath(file);
			
			System.out.println();
			System.out.println("NEW WAY ACTIVITY:");
			consolePrinter.consolePrint(act);
			System.out.println("------------------------------------------");
			System.out.println();

			// ----------------------------------------------
			
			// new way of analysis
			if (act != null) {
				// rename source file
				fitFileRenamer.rename(act);
				
				activityProcessor.process(act);
			}
		}
		
		ActivityProcessingResult result = activityProcessor.getResults();
		
		resultPrinter.print(result);
		
		System.out.println();
		System.out.println("Files read: " + files.size());
		
		System.out.println();
		
    }
}
