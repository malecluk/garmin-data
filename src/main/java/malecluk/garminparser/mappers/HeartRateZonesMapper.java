package malecluk.garminparser.mappers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.garmin.fit.MesgNum;
import com.garmin.fit.TimeInZoneMesg;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.component.HeartRateZone;
import malecluk.garminparser.model.component.HeartRateZones;

@Component
public class HeartRateZonesMapper {

	public void map(Activity activity, TimeInZoneMesg mesg) {

		// for now we don't want lap zones
        if (mesg.getReferenceMesg() != MesgNum.SESSION) {
            return;
        }
        
        List<Short> boundaries = List.of(mesg.getHrZoneHighBoundary());
        List<Float> times = List.of(mesg.getTimeInHrZone());
        
        List<HeartRateZone> zones = new ArrayList<>();
        
        for (int i = 0; i < times.size(); i++) {
        	
        	Integer minBpm = i == 0 ? null : Integer.valueOf(boundaries.get(i - 1));
            Integer maxBpm = i < boundaries.size() ? boundaries.get(i) - 1 : null;

            Duration time = Duration.ofMillis((long) (times.get(i) * 1000));
            
            zones.add(new HeartRateZone(i, minBpm, maxBpm, time));
        }
		
        activity.setHeartRateZones(new HeartRateZones(zones));
	}
}
