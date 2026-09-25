package malecluk.garminparser.utils;

import org.springframework.stereotype.Component;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.FloorClimbing;
import malecluk.garminparser.model.activities.Hiking;
import malecluk.garminparser.model.activities.Meditation;
import malecluk.garminparser.model.activities.OutdoorMovingActivity;
import malecluk.garminparser.model.activities.Rucking;
import malecluk.garminparser.model.activities.Running;
import malecluk.garminparser.model.activities.TrainingActivity;
import malecluk.garminparser.model.activities.Walking;
import malecluk.garminparser.model.activities.Yoga;
import malecluk.garminparser.model.component.ActivityDeviceInfo;
import malecluk.garminparser.model.component.ActivityElevationData;
import malecluk.garminparser.model.component.HeartRateZone;
import malecluk.garminparser.model.component.HeartRateZones;

@Component
public class ActivityConsolePrinter {

	/**
	 * Method will print all fields of Activity into console
	 * @param a Activity to print
	 */
	public void consolePrint(Activity a) {
		System.out.println(toConsoleString(a));
	}
	
	/**
	 * Method will return console safe String (with new lines) with list of all parameters of activity
	 * @param act Activity to print
	 * @return String for console output
	 */
	public String toConsoleString(Activity act) {
		StringBuilder sb = new StringBuilder();
		
		// add basic activity
		sb.append(activityTCS(act));
		
		// add heart rates
		if (act.getHeartRateZones() != null) {
			sb.append(activityHeartRateZonesTCS(act.getHeartRateZones()));
		}
		
		// add OutdoorMovingActivity
		if (act instanceof OutdoorMovingActivity outdoorMoving) {
			sb.append(outdoorMovingActivityTCS(outdoorMoving));
		}
		
		// add walking
		if (act instanceof Walking walking) {
			sb.append(walkingTCS(walking));
		}
		
		// add running
		if (act instanceof Running running) {
			sb.append(runningTCS(running));
		}
		
		// add meditation
		if (act instanceof Meditation meditation) {
			sb.append(meditationTCS(meditation));
		}
		
		// add floor climbing
		if (act instanceof FloorClimbing flClimbing) {
			sb.append(floorClimbingTCS(flClimbing));
		}
		
		if (act instanceof Hiking hiking) {
			sb.append(hikingTCS(hiking));
		}
		
		// add rucking
		if (act instanceof Rucking rucking) {
			sb.append(ruckingTCS(rucking));
		}
		
		// add trainingActivity
		if (act instanceof TrainingActivity trAct) {
			sb.append(trainingActivityTCS(trAct));
		}
		
		// add yoga
		if (act instanceof Yoga y) {
			sb.append(yogaTCS(y));
		}
		
		return sb.toString();
	}
	
	/**
	 * Helper method to return console safe (with new lines) String with all values from Activity class.
	 * Should be called from another methods
	 * TCS stands for ToConsoleString
	 * @param a Activity
	 * @return String safe for console output for Activity
	 */
	private String activityTCS(Activity a) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("BaseActivity results:\n");
		
		sb.append("sport:              ").append(a.getSport() != null ? a.getSport().toString() : "null").append("\n");
		sb.append("subSport:           ").append(a.getSubSport() != null ? a.getSubSport().toString() : "null").append("\n");
		sb.append("sportProfileName:   ").append(a.getSportProfileName() != null ? a.getSportProfileName().toString() : "null").append("\n");
		sb.append("startTime:          ").append(DateTimeConverterHelper.formatDate(a.getStartTime())).append("\n");
		sb.append("timestamp:          ").append(DateTimeConverterHelper.formatDate(a.getTimestamp())).append("\n");
		sb.append("fileCreationTime:   ").append(DateTimeConverterHelper.formatDate(a.getFileCreationTime())).append("\n");
		
		String formattedTotalElapsedTime = DateTimeConverterHelper.formatSeconds(a.getTotalElapsedTime());
		sb.append("totalElapsedTime:   ").append(a.getTotalElapsedTime().toSeconds()).append(" / ").append(formattedTotalElapsedTime).append("\n");
		
		String formattedTotalTimerTime = DateTimeConverterHelper.formatSeconds(a.getTotalTimerTime());
		sb.append("totalTimerTime:     ").append(a.getTotalTimerTime().toSeconds()).append(" / ").append(formattedTotalTimerTime).append("\n");
		
		if (a.getElevationData() != null) {
			sb.append(activityElevationDataTCS(a.getElevationData()));
		}
		else {
			sb.append("elevationData:      null").append("\n");
		}
		
		if (a.getDeviceInfo() != null) {
			sb.append(activityDeviceInfoTCS(a.getDeviceInfo()));
		}
		else {
			sb.append("deviceInfo:         null").append("\n");
		}
		
		
		
		return sb.toString();
	}
	
	/**
	 * Helper method to return console safe (with new lines) String with all values from ActivityDeviceInfo class.
	 * Should be called from another methods
	 * TCS stands for ToConsoleString
	 * @param i ActivityDeviceInfo
	 * @return String safe for console output for ActivityDeviceInfo
	 */
	private String activityDeviceInfoTCS(ActivityDeviceInfo i) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("ActivityDeviceInfo:\n");
		
		sb.append("manufacturer:       ").append(i.getManufacturer()).append("\n");
		sb.append("product:            ").append(i.getProduct()).append("\n");
		sb.append("serialNumber:       ").append(i.getSerialNumber()).append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Helper method to return console safe (with new lines) String with all values from ActivityElevationData class.
	 * Should be called from another methods
	 * TCS stands for ToConsoleString
	 * @param ed ActivityElevationData
	 * @return String safe for console output for ActivityElevationData
	 */
	private String activityElevationDataTCS(ActivityElevationData ed) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("ActivityElevationData:\n");
		
		sb.append("totalAscent:        ").append(ed.getTotalAscent().toMeters()).append(" m").append("\n");
		sb.append("totalDescent:       ").append(ed.getTotalDescent().toMeters()).append(" m").append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Helper method to return console safe (with new lines) String with all values from HeartRateZones class.
	 * Should be called from another methods
	 * TCS stands for ToConsoleString
	 * @param z HeartRateZones
	 * @return String safe for console output for HeartRateZones
	 */
	private String activityHeartRateZonesTCS(HeartRateZones z) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("HeartRateZones results:\n");

	    for (int i = 0; i < z.zonesList().size(); i++) {
	        HeartRateZone zone = z.zonesList().get(i);

	        sb.append(String.format(
	                "%d: min=%-5s max=%-5s time=%s s%n",
	                zone.number(),
	                zone.minBpm(),
	                zone.maxBpm(),
	                DateTimeConverterHelper.formatSeconds(zone.time())
	        ));
	    }

	    return sb.toString();
	}
	
	private String floorClimbingTCS(FloorClimbing fc) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Floor Climbing results:\n");
		sb.append("-- no fields --\n");
		
		return sb.toString();
	}
	
	private String hikingTCS(Hiking h) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Hiking results:\n");
		sb.append("-- no fields --\n");
		
		return sb.toString();
	}

	/**
	 * Helper method to return console safe (with new lines) String with all values from Running class.
	 * Should be called from another methods
	 * TCS stands for ToConsoleString
	 * @param m Meditation
	 * @return String safe for console output for Meditation
	 */
	private String meditationTCS(Meditation m) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Meditation results:\n");
		sb.append("-- no fields --\n");
		
		return sb.toString();
	}
	
	/**
	 * Helper method to return console safe (with new lines) String with all values from OutdoorMovingActivity class.
	 * Should be called from another methods
	 * TCS stands for ToConsoleString
	 * @param a OutdoorMovingActivity
	 * @return String safe for console output for OutdoorMovingActivity
	 */
	private String outdoorMovingActivityTCS(OutdoorMovingActivity a) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Moving results:\n");
		
		sb.append("enhancedAvgSpeed:   " + a.getEnhancedAvgSpeed()).append("\n");
		sb.append("totalDistance:      ").append(a.getTotalDistance().toMeters()).append(" m / ").append(a.getTotalDistance().toKilometers()).append(" km").append("\n");
		
		// Average pace
        Float avgSpeedMps = a.getEnhancedAvgSpeed();
        String formattedPace = convertSpeedToPace(avgSpeedMps);
        sb.append("CMP: Avg. pace:     " + a.getAvgPace() + " / " + formattedPace).append("\n");
		
		
		return sb.toString();
	}
	
	private String ruckingTCS(Rucking r) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Rucking results:\n");
		
		sb.append("packWeightTenthsKg: " + r.getPackWeightTenthsKg());
		if (r.getPackWeightTenthsKg() != null) {
			sb.append(" / ").append((r.getPackWeightTenthsKg() / 10.0f)).append(" kg");
		}
		sb.append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Helper method to return console safe (with new lines) String with all values from Running class.
	 * Should be called from another methods
	 * TCS stands for ToConsoleString
	 * @param r Running
	 * @return String safe for console output for Running
	 */
	private String runningTCS(Running r) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Running results:\n");
		sb.append("-- no fields --\n");
		
		return sb.toString();
	}
	
	private String trainingActivityTCS(TrainingActivity t) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Training results:\n");
		sb.append("-- no fields --\n");
		
		return sb.toString();
	}
	
	/**
	 * Helper method to return console safe (with new lines) String with all values from Walking class.
	 * Should be called from another methods
	 * TCS stands for ToConsoleString
	 * @param w Walking
	 * @return String safe for console output for Walking
	 */
	private String walkingTCS(Walking w) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Walking results:\n");
		
		sb.append("totalStrides:      " + w.getTotalStrides()).append("\n");
		sb.append("CMP: total steps:  " + (w.getTotalStrides()*2)).append("\n"); // Computed value - total steps
		
		return sb.toString();
	}
	
	private String yogaTCS(Yoga y) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Yoga results:\n");
		sb.append("-- no fields --\n");
		
		return sb.toString();
	}
	
	/**
	 * Helper method for transformation of m/s to min/km
	 * @param speedMps speed in meters per second
	 * @return pace in minutes per km
	 */
    private static String convertSpeedToPace(Float speedMps) {
        if (speedMps == null || speedMps <= 0) return "--:-- min/km";
        
        // Převod m/s na km/h
        float speedKmh = speedMps * 3.6f;
        
        // Výpočet minut na kilometr (60 / km/h)
        float paceDecimal = 60 / speedKmh;
        
        int minutes = (int) paceDecimal;
        int seconds = (int) ((paceDecimal - minutes) * 60);
        
        return String.format("%d:%02d min/km", minutes, seconds);
    }
}
