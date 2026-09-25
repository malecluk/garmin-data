package malecluk.garminparser;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application configuration loaded from the {@code fit} configuration prefix.
 */
@ConfigurationProperties(prefix = "fit")
public record FitConfiguration(
		MesgDtoPrinter mesgDtoPrinter,
		FileRenamer fileRenamer,
        Parser parser,
        FilesScanner filesScanner
        //Export export
) {

	/**
	 * Configuration controlling diagnostic printing of parsed FIT messages.
	 */
	public record MesgDtoPrinter(
			boolean disableAllPrints,
			boolean printActivityMesg,
            boolean printDeviceInfoMesg,
            boolean printDeviceSettingsMesg,
            boolean printHrZoneMesg,
            boolean printLapMesg,
            boolean printSessionMesg,
            boolean printSportMesg,
            boolean printTimeInZoneMesg,
            boolean printTimestampCorrelationMesg,
            boolean printUserProfileMesg,
            boolean printZonesTargetMesg
    ) {}
	
	/**
	 * Configuration controlling whether processed FIT files are renamed.
	 */
	public record FileRenamer(
			boolean enabled
	) {}
	
	/**
	 * Configuration controlling FIT decoding behavior.
	 */
	public record Parser(
            boolean strict,
            boolean unknownFields
    ) {}
	
	/**
	 * Configuration of the directory scanned for FIT files.
	 */
	public record FilesScanner(
			String directoryPath
	) {}
	
}
