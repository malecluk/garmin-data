package malecluk.garminparser.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.garmin.fit.Field;
import com.garmin.fit.Mesg;

import malecluk.garminparser.FitConfiguration;
import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;

/**
 * Helper class just for printing content of ParsedFitFileMessagesDTO into console 
 */
@Component
public class ParsedFitFileMessagesDTOPrinter {
	
	private final FitConfiguration config;

	public ParsedFitFileMessagesDTOPrinter(FitConfiguration config) {
		this.config = config;
	}

	public void print(ParsedFitFileMessagesDTO dto) {
		
		if (config.mesgDtoPrinter().disableAllPrints()) {
			return;
		}
		
		System.out.println("===================================================");
		System.out.println("ParsedFitFileMessagesDTO content:");
		System.out.println();
		
		// -----------------------------------------------------------------------------
		
		//
		printMessages(
	            "ActivityMesg",
	            "print-activity-mesg",
	            dto.getActivityMesgList(),
	            config.mesgDtoPrinter().printActivityMesg());
		
		printMessages(
	            "DeviceInfoMesg",
	            "print-device-info-mesg",
	            dto.getDeviceInfoMesgList(),
	            config.mesgDtoPrinter().printDeviceInfoMesg());
		
		printMessages(
	            "DeviceSettingsMesg",
	            "print-device-settings-mesg",
	            dto.getDeviceSettingsMesgList(),
	            config.mesgDtoPrinter().printDeviceSettingsMesg());
		
		printMessages(
				"HrZoneMesg", 
				"print-hr-zone-mesg", 
				dto.getHrZoneMesgList(),
				config.mesgDtoPrinter().printHrZoneMesg());
		
		printMessages(
	            "LapMesg",
	            "print-lap-mesg",
	            dto.getLapMesgList(),
	            config.mesgDtoPrinter().printLapMesg());
		
		printMessages(
	            "SessionMesg",
	            "print-session-mesg",
	            dto.getSessionMesgList(),
	            config.mesgDtoPrinter().printSessionMesg());
		
		printMessages(
	            "SportMesg",
	            "print-sport-mesg",
	            dto.getSportMesgList(),
	            config.mesgDtoPrinter().printSportMesg());
		
		printMessages(
	            "TimeInZoneMesg",
	            "print-time-in-zone-mesg",
	            dto.getTimeInZoneMesgList(),
	            config.mesgDtoPrinter().printTimeInZoneMesg());
		
		printMessages(
	            "TimestampCorrelationMesg",
	            "print-timestamp-correlation-mesg",
	            dto.getTimestampCorrelationMesgList(),
	            config.mesgDtoPrinter().printTimestampCorrelationMesg());
		
		printMessages(
	            "UserProfileMesg",
	            "print-user-profile-mesg",
	            dto.getUserProfileMesgList(),
	            config.mesgDtoPrinter().printUserProfileMesg());
		
		printMessages(
	            "ZonesTargetMesg",
	            "print-zones-target-mesg",
	            dto.getZonesTargetMesgList(),
	            config.mesgDtoPrinter().printZonesTargetMesg());
		
		System.out.println("===================================================");
		
	}
	
	private <T extends Mesg> void printMessages(
	        String messageName,
	        String configName,
	        List<T> messages,
	        boolean enabled) {

	    System.out.println(messageName + ":");

	    if (!enabled) {
	        System.out.println("print disabled by mesg-dto-printer:" + configName);
	        System.out.println();
	        return;
	    }

	    if (messages == null) {
	        System.out.println(messageName + " list is null");
	        System.out.println();
	        return;
	    }

	    if (messages.isEmpty()) {
	        System.out.println(messageName + " list is empty");
	        System.out.println();
	        return;
	    }

	    T firstMsg = messages.getFirst();

// tady zvážit napřed najít fieldy ze všech zpráv a až pak něco vypisovat
/*
Set<Integer> fieldNumbers = new LinkedHashSet<>();

for (T message : messages) {
    for (Field field : message.getFields()) {
        fieldNumbers.add(field.getNum());
    }
}
*/
	    for (Field field : firstMsg.getFields()) {
	        System.out.print(field.getName() + "(" + field.getNum() + "){"  + field.getUnits()  + "} / vs=" + field.getNumValues() + ": ");
	        for (T message : messages) {
	            Field messageField = message.getField(field.getNum());
	            if (messageField != null) {
	            	if (messageField.getNumValues() <= 1) {
	            		System.out.print(messageField.getValue());
	            	}
	            	else {
	            		System.out.print("[");
	            		for (int i = 0; i < messageField.getNumValues(); i++) {
	            			System.out.print(field.getValue(i));

	            		    if (i < field.getNumValues() - 1) {
	            		        System.out.print(", ");
	            		    }
						}
	            		System.out.print("]");
	            	}
	            }
	            else {
	            	System.out.println("null");
	            }
	            System.out.print(" ");
	        }
	        System.out.println();
	    }
	    System.out.println();
	}
}
