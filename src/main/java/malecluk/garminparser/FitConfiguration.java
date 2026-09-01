package malecluk.garminparser;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fit")
public record FitConfiguration(
		MesgDtoPrinter mesgDtoPrinter,
		FileRenamer fileRenamer,
        Parser parser,
        FilesScanner filesScanner
        //Export export
) {

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
	
	public record FileRenamer(
			boolean enabled
	) {}
	
	public record Parser(
            boolean strict,
            boolean unknownFields
    ) {}
	
	public record FilesScanner(
			String directoryPath
	) {}
	
}
