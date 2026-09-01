package malecluk.garminparser.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.garmin.fit.DateTime;

public class DateTimeConverterHelper {
	
	// Helper method for formatting seconds to HH:MM:SS
    public static String formatSeconds(Float totalSeconds) {
        if (totalSeconds == null) return "00:00:00";
        int seconds = totalSeconds.intValue();
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
    
    public static String formatSeconds(Duration duration) {
    	Float secs = ((Long) duration.toSeconds()).floatValue();
    	return formatSeconds(secs);
    }
    
    /**
     * Formating date to string
     * @param d
     * @return
     */
    public static String formatDate(DateTime d) {
    	// Conversion of Garmin DateTime to standard java.util.Date
    	Date javaDate = null;
    	if (d != null) {
    		javaDate = d.getDate();
    	}
    	return formatDate(javaDate);
	}
    
    public static String formatDate(Date d) {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    	String formattedTime = "Unknown date";
        if (d != null) {
        	            
            // Formatting date and time to readable format
            formattedTime = sdf.format(d);
        }
        return formattedTime;
    }
    
    public static String formatDate(Instant instant) {
    	DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.systemDefault());
        if (instant == null) {
            return "Unknown date";
        }

        return FORMATTER.format(instant);
    }

}
