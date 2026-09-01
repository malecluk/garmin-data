package malecluk.garminparser.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.garmin.fit.DateTime;

/**
 * Provides helper methods for formatting FIT and Java date/time values
 * and durations for human-readable console output.
 */
public class DateTimeConverterHelper {
	
	/**
	 * Formats a number of seconds as {@code HH:mm:ss}.
	 *
	 * <p>Fractional seconds are truncated to whole seconds. A {@code null}
	 * value is formatted as {@code 00:00:00}.</p>
	 *
	 * @param totalSeconds number of seconds to format
	 * @return formatted duration in {@code HH:mm:ss} format
	 */
    public static String formatSeconds(Float totalSeconds) {
        if (totalSeconds == null) return "00:00:00";
        int seconds = totalSeconds.intValue();
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
    
    /**
     * Formats a duration as {@code HH:mm:ss}.
     *
     * <p>Sub-second precision is discarded.</p>
     *
     * @param duration duration to format
     * @return formatted duration in {@code HH:mm:ss} format
     */
    public static String formatSeconds(Duration duration) {
    	Float secs = ((Long) duration.toSeconds()).floatValue();
    	return formatSeconds(secs);
    }
    
    /**
     * Formats a Garmin FIT {@link DateTime} as a human-readable date and time.
     *
     * <p>The FIT date is converted to {@link Date} before formatting.
     * A {@code null} value is returned as {@code Unknown date}.</p>
     *
     * @param d Garmin FIT date and time to format
     * @return formatted date and time or {@code Unknown date} if {@code d} is null
     */
    public static String formatDate(DateTime d) {
    	// Conversion of Garmin DateTime to standard java.util.Date
    	Date javaDate = null;
    	if (d != null) {
    		javaDate = d.getDate();
    	}
    	return formatDate(javaDate);
	}
    
    /**
     * Formats a {@link Date} as a human-readable date and time.
     *
     * <p>The output format is {@code dd.MM.yyyy HH:mm:ss} and uses the system
     * default time zone.</p>
     *
     * @param d date to format
     * @return formatted date and time or {@code Unknown date} if {@code d} is null
     */
    public static String formatDate(Date d) {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    	String formattedTime = "Unknown date";
        if (d != null) {
        	            
            // Formatting date and time to readable format
            formattedTime = sdf.format(d);
        }
        return formattedTime;
    }
    
    /**
     * Formats an {@link Instant} as a human-readable date and time.
     *
     * <p>The output format is {@code dd.MM.yyyy HH:mm:ss} and the instant is
     * converted using the system default time zone.</p>
     *
     * @param instant instant to format
     * @return formatted date and time or {@code Unknown date} if {@code instant} is null
     */
    public static String formatDate(Instant instant) {
    	DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.systemDefault());
        if (instant == null) {
            return "Unknown date";
        }

        return FORMATTER.format(instant);
    }

}
