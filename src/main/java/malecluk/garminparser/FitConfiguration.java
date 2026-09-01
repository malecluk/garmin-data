package malecluk.garminparser;

import java.util.List;
import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application configuration loaded from the {@code fit} configuration prefix.
 */
@ConfigurationProperties(prefix = "fit")
public record FitConfiguration(
		MesgDtoPrinter mesgDtoPrinter,
		FileRenamer fileRenamer,
        FilesScanner filesScanner
        //Export export
) {
	
	public FitConfiguration {
        if (mesgDtoPrinter == null) {
        	mesgDtoPrinter = MesgDtoPrinter.disabled();
        }
    }

	/**
	 * Configuration controlling diagnostic printing of parsed FIT messages.
	 */
	public record MesgDtoPrinter(
			
			/**
			 * Disables all FIT message printing when enabled.
			 */
			boolean disableAllPrints,
			
			/**
			 * Whether decoded {@code FileIdMesg} messages are printed.
			 */
			boolean printFileIdMesg,
			
			/**
			 * Whether decoded {@code ActivityMesg} messages are printed.
			 */
			boolean printActivityMesg,
			
			/**
			 * Whether decoded {@code DeviceInfoMesg} messages are printed.
			 */
            boolean printDeviceInfoMesg,
            
            /**
             * Whether decoded {@code DeviceSettingsMesg} messages are printed.
             */
            boolean printDeviceSettingsMesg,
            
            /**
             * Whether decoded {@code HrZoneMesg} messages are printed.
             */
            boolean printHrZoneMesg,
            
            /**
             * Whether decoded {@code LapMesg} messages are printed.
             */
            boolean printLapMesg,
            
            /**
             * Whether decoded {@code SessionMesg} messages are printed.
             */
            boolean printSessionMesg,
            
            /**
             * Whether decoded {@code SportMesg} messages are printed.
             */
            boolean printSportMesg,
            
            /**
             * Whether decoded {@code TimeInZoneMesg} messages are printed.
             */
            boolean printTimeInZoneMesg,
            
            /**
             * Whether decoded {@code TimestampCorrelationMesg} messages are printed.
             */
            boolean printTimestampCorrelationMesg,
            
            /**
             * Whether decoded {@code UserProfileMesg} messages are printed.
             */
            boolean printUserProfileMesg,
            
            /**
             * Whether decoded {@code ZonesTargetMesg} messages are printed.
             */
            boolean printZonesTargetMesg,
            
            /**
             * Whether decoded {@code SetMesg} messages are printed.
             */
            boolean printSetMesg
    ) {
		
		public static MesgDtoPrinter disabled() {
	        return new MesgDtoPrinter(
	                true,
	                false,
	                false,
	                false,
	                false,
	                false,
	                false,
	                false,
	                false,
	                false,
	                false,
	                false,
	                false,
	                false
	        );
		}
	}
	
	/**
	 * Configuration controlling whether processed FIT files are renamed.
	 */
	public record FileRenamer(
			
			/**
			 * Whether processed FIT files are automatically renamed according to
			 * their activity data.
			 */
			boolean enabled
	) {}
	
	/**
	 * Configuration of the directory scanned for FIT files.
	 */
	public record FilesScanner(
			
			/**
			 * Paths to the directories containing FIT files to process.
			 */
			List<String> directoryPaths,
			
			/**
	         * Optional file-name prefix used to restrict processing to a single
	         * or small amount of FIT file(s) during local debugging.
	         */
			List<String> debugFilePrefixes
	) {
		public FilesScanner {
	        Objects.requireNonNull(directoryPaths, "directoryPaths must not be null");
	        directoryPaths = List.copyOf(directoryPaths);
	        
	        if (debugFilePrefixes == null) {
	            debugFilePrefixes = List.of();
	        } else {
	            debugFilePrefixes = List.copyOf(debugFilePrefixes);
	        }
	    }
	}
	
}
