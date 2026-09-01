package malecluk.garminparser.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.Breathing;
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
import malecluk.garminparser.model.component.ActivityLap;
import malecluk.garminparser.model.component.ActivityLaps;
import malecluk.garminparser.model.component.ActivitySet;
import malecluk.garminparser.model.component.ActivitySets;
import malecluk.garminparser.model.component.HeartRateZone;
import malecluk.garminparser.model.component.HeartRateZones;

/**
 * Formats {@link Activity} instances and their specialized activity data
 * for console output.
 *
 * <p>The printer includes common activity fields and, when applicable,
 * fields specific to outdoor activities, walking, running, meditation,
 * floor climbing, hiking, rucking, training, and yoga.</p>
 */
@Component
public class ActivityConsolePrinter {

	/**
	 * Prints an activity to the console.
	 * @param activity activity to print
	 */
	public void consolePrint(Activity a) {
		System.out.println(toConsoleString(a));
	}
	
	/**
	 * Returns a string containing the activity data formatted for console output.
	 * @param act activity to format
	 * @return formatted activity data
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
		
		// add hiking
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
		
		// add breathing
		if (act instanceof Breathing b) {
			sb.append(breathingTCS(b));
		}
		
		// add laps
		if (act.getLaps() != null) {
			sb.append(activityLapsTCS(act.getLaps()));
		}
		
		// add sets
		if (act.getSets() != null) {
			sb.append(activitySetsTCS(act.getSets()));
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
		
		sb.append("Activity ID:        ").append(a.getIds().activityId().value()).append("\n");
		sb.append(".FIT file hash:     ").append(a.getIds().sourceFileHash().value()).append("\n");
		sb.append("sport:              ").append(formatValue(a.getSport())).append("\n");
		sb.append("subSport:           ").append(formatValue(a.getSubSport())).append("\n");
		sb.append("sportProfileName:   ").append(formatValue(a.getSportProfileName())).append("\n");
		sb.append("startTime:          ").append(formatInstant(a.getStartTime())).append("\n");
		sb.append("timestamp:          ").append(formatInstant(a.getTimestamp())).append("\n");
		sb.append("fileCreationTime:   ").append(formatInstant(a.getFileCreationTime())).append("\n");
		
		if (a.getTotalElapsedTime() != null) {
			sb.append("totalElapsedTime:   ").append(a.getTotalElapsedTime().toSeconds()).append(" / ").append(formatDuration(a.getTotalElapsedTime())).append("\n");
		}
		else {
			sb.append("totalElapsedTime:   null").append("\n");
		}
		if (a.getTotalTimerTime() != null) {
			sb.append("totalTimerTime:     ").append(a.getTotalTimerTime().toSeconds()).append(" / ").append(formatDuration(a.getTotalTimerTime())).append("\n");
		}
		else {
			sb.append("totalTimerTime:     null").append("\n");
		}
		
		
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
	                formatDuration(zone.time())
	        ));
	    }

	    return sb.toString();
	}
	
	private String activitySetsTCS(ActivitySets sets) {
		StringBuilder sb = new StringBuilder();

		sb.append("---------------------------\n");
		sb.append("ActivitySets results:\n");
		
		if (sets.isEmpty()) {
			sb.append("-- no sets --\n");
			return sb.toString();
		}
		
		List<ActivitySet> setList = sets.setList();
		
		sb.append(String.format("%-22s", "parameter"));

		for (ActivitySet set : setList) {
			sb.append(String.format("%-22s", "set" + set.getNumber()));
		}
		
		sb.append("\n");
		sb.append("-".repeat(22 * (setList.size() + 1))).append("\n");

		appendSetRow(sb, "number", setList, ActivitySet::getNumber);
		appendSetRow(sb, "setTimestamp", setList, set -> formatInstant(set.getSetTimestamp()));
		appendSetRow(sb, "startTime", setList, set -> formatInstant(set.getStartTime()));
		appendSetRow(sb, "duration", setList, set -> formatDuration(set.getDuration()));
		appendSetRow(sb, "setType", setList, ActivitySet::getSetType);
		
		return sb.toString();
	}
	
	private void appendSetRow(StringBuilder sb, String parameter, List<ActivitySet> sets, Function<ActivitySet, Object> valueProvider) {

		sb.append(String.format("%-22s", parameter));

		for (ActivitySet set : sets) {
			sb.append(String.format("%-22s", formatValue(valueProvider.apply(set))));
		}

		sb.append("\n");
	}
	
	private String activityLapsTCS(ActivityLaps laps) {
		StringBuilder sb = new StringBuilder();

		sb.append("---------------------------\n");
		sb.append("ActivityLaps results:\n");

		if (laps.isEmpty()) {
			sb.append("-- no laps --\n");
			return sb.toString();
		}

		List<ActivityLap> lapList = laps.lapsList();

		sb.append(String.format("%-22s", "parameter"));

		for (ActivityLap lap : lapList) {
			sb.append(String.format("%-22s", "lap" + lap.getNumber()));
		}

		sb.append("\n");
		sb.append("-".repeat(22 * (lapList.size() + 1))).append("\n");

		appendLapRow(sb, "number", lapList, ActivityLap::getNumber);
		appendLapRow(sb, "startTime", lapList, lap -> formatInstant(lap.getStartTime()));
		appendLapRow(sb, "endTime", lapList, lap -> formatInstant(lap.getEndTime()));
		appendLapRow(sb, "elapsedTime", lapList, lap -> formatDuration(lap.getElapsedTime()));
		appendLapRow(sb, "timerTime", lapList, lap -> formatDuration(lap.getTimerTime()));
		appendLapRow(sb, "totalDistance", lapList, lap ->
				lap.getTotalDistance() != null
						? lap.getTotalDistance().toMeters() + " m"
						: "null");
		appendLapRow(sb, "averageSpeed", lapList, lap -> lap.getAverageSpeed());
		appendLapRow(sb, "maximumSpeed", lapList, lap -> lap.getMaximumSpeed());
		appendLapRow(sb, "averageHeartRate", lapList, lap -> lap.getAverageHeartRate());
		appendLapRow(sb, "maximumHeartRate", lapList, lap -> lap.getMaximumHeartRate());
		appendLapRow(sb, "averageCadence", lapList, lap -> lap.getAverageCadence());
		appendLapRow(sb, "maximumCadence", lapList, lap -> lap.getMaximumCadence());
		appendLapRow(sb, "totalCalories", lapList, lap -> lap.getTotalCalories());
		appendLapRow(sb, "totalAscent", lapList, lap ->
				lap.getTotalAscent() != null
						? lap.getTotalAscent().toMeters() + " m"
						: "null");
		appendLapRow(sb, "totalDescent", lapList, lap ->
				lap.getTotalDescent() != null
						? lap.getTotalDescent().toMeters() + " m"
						: "null");
		appendLapRow(sb, "minimumAltitude", lapList, lap -> lap.getMinimumAltitude());
		appendLapRow(sb, "maximumAltitude", lapList, lap -> lap.getMaximumAltitude());
		appendLapRow(sb, "averageTemperature", lapList, lap -> lap.getAverageTemperature());
		appendLapRow(sb, "minimumTemperature", lapList, lap -> lap.getMinimumTemperature());
		appendLapRow(sb, "maximumTemperature", lapList, lap -> lap.getMaximumTemperature());
		
		sb.append(String.format("%-22s", "startPosition"));
		
		for (ActivityLap lap : lapList) {
			sb.append(lap.getStartPosition() != null ? String.format("%-21.6f", lap.getStartPosition().latitude()) : "null");
		}
		sb.append("\n");
		
		sb.append(String.format("%-22s", " "));
		
		for (ActivityLap lap : lapList) {
			sb.append(lap.getStartPosition() != null ? String.format("%-21.6f", lap.getStartPosition().longitude()) : "null");
		}
		sb.append("\n");

		return sb.toString();
	}

	private void appendLapRow(StringBuilder sb, String parameter, List<ActivityLap> laps, Function<ActivityLap, Object> valueProvider) {

		sb.append(String.format("%-22s", parameter));

		for (ActivityLap lap : laps) {
			sb.append(String.format("%-22s", formatValue(valueProvider.apply(lap))));
		}

		sb.append("\n");
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
		
		if (a.getTotalDistance() != null) {
			sb.append("totalDistance:      ").append(String.format("%.2f", a.getTotalDistance().toMeters())).append(" m / ").append(String.format("%.5f", a.getTotalDistance().toKilometers())).append(" km").append("\n");
		}
		else {
			sb.append("totalDistance:      null").append("\n");
		}
		
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
		if (w.getTotalStrides() != null) {
			sb.append("CMP: total steps:  " + (w.getTotalStrides()*2)).append("\n"); // Computed value - total steps
		}		
		
		return sb.toString();
	}
	
	private String yogaTCS(Yoga y) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Yoga results:\n");
		sb.append("-- no fields --\n");
		
		return sb.toString();
	}
	
	private String breathingTCS(Breathing b) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---------------------------\n");
		sb.append("Breathing results:\n");
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

	private String formatValue(Object value) {
		return value != null ? value.toString() : "null";
	}

	private String formatInstant(Instant instant) {
		return instant != null ? DateTimeConverterHelper.formatDate(instant) : "null";
	}

	private String formatDuration(Duration duration) {
		return duration != null ? DateTimeConverterHelper.formatSeconds(duration) : "null";
	}
}
