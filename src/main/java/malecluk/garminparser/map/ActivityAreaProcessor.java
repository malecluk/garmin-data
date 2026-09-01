
package malecluk.garminparser.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

import malecluk.garminparser.model.activities.Activity;
import malecluk.garminparser.model.activities.OutdoorMovingActivity;
import malecluk.garminparser.model.component.ActivityTrack;
import malecluk.garminparser.model.value.Position;

/**
 * Processes activity tracks and combines their buffered geographic areas.
 *
 * <p>
 * Each activity is processed independently. Its track is converted into
 * continuous segments, assigned a suitable UTM projection, buffered by
 * {@code TrackBufferBuilder}, and transformed back to WGS84.
 * </p>
 *
 * <p>
 * The resulting activity areas are then merged into a single WGS84 geometry.
 * Activities without usable track segments are skipped.
 * </p>
 */
@Component
public class ActivityAreaProcessor {
	
	private static final Logger log = LogManager.getLogger(ActivityAreaProcessor.class);

	private final TrackGeometryBuilder trackGeometryBuilder;
	private final CoordinateTransformer coordinateTransformer;
	private final TrackBufferBuilder trackBufferBuilder;
	private final ActivityAreaMerger activityAreaMerger;

	public ActivityAreaProcessor(TrackGeometryBuilder trackGeometryBuilder, CoordinateTransformer coordinateTransformer,
			TrackBufferBuilder trackBufferBuilder, ActivityAreaMerger activityAreaMerger) {

		this.trackGeometryBuilder = Objects.requireNonNull(trackGeometryBuilder,
				"trackGeometryBuilder must not be null");
		this.coordinateTransformer = Objects.requireNonNull(coordinateTransformer,
				"coordinateTransformer must not be null");
		this.trackBufferBuilder = Objects.requireNonNull(trackBufferBuilder, "trackBufferBuilder must not be null");
		this.activityAreaMerger = Objects.requireNonNull(activityAreaMerger, "activityAreaMerger must not be null");
	}

	/**
	 * Processes the supplied activity tracks and merges their buffered areas.
	 *
	 * @param tracks activity tracks to process
	 * @return the merged buffered area in WGS84, or an empty geometry if no usable
	 *         track segments are found
	 * @throws NullPointerException if the collection or any track is null
	 */
	public Geometry process(Collection<ActivityTrack> tracks) {
		Objects.requireNonNull(tracks, "tracks must not be null");
		
		log.debug("Tracks to proceed: " + tracks.size());

		List<Geometry> activityAreas = new ArrayList<>();

		int debugCounter = 0;
		for (ActivityTrack track : tracks) {
			log.debug("Processing track " + ++debugCounter + " / " + tracks.size());
			Objects.requireNonNull(track, "tracks must not contain null elements");

			List<LineString> segments = trackGeometryBuilder.build(track);

			if (segments.isEmpty()) {
				continue;
			}

			Position referencePosition = getReferencePosition(segments.get(0));
			CoordinateReferenceSystem projection = coordinateTransformer.determineProjection(referencePosition);

			Geometry activityArea = trackBufferBuilder.build(segments, projection);
			activityAreas.add(activityArea);
		}

		return activityAreaMerger.merge(activityAreas);
	}
	
	/**
	 * Processes outdoor activity tracks and merges their buffered geographic areas.
	 *
	 * <p>
	 * Only {@link OutdoorMovingActivity} instances with an available track are
	 * included. Activities without a track are skipped.
	 * </p>
	 *
	 * @param activities activities to process
	 * @return the merged buffered area in WGS84, or an empty geometry if no usable
	 *         tracks are available
	 * @throws NullPointerException if the collection or any activity is null
	 */
	public Geometry processActivities(Collection<? extends Activity> activities) {
		Objects.requireNonNull(activities, "activities must not be null");

		List<ActivityTrack> tracks = new ArrayList<>();

		for (Activity activity : activities) {
			Objects.requireNonNull(activity, "activities must not contain null elements");
			
			if (activity instanceof OutdoorMovingActivity outdoorMoving && outdoorMoving.getTrack() != null) {
				log.debug("Reading tracks for activity " + activity.getFilePath());
				tracks.add(outdoorMoving.getTrack());
			}
		}

		return process(tracks);
	}

	private Position getReferencePosition(LineString firstSegment) {
		Coordinate coordinate = firstSegment.getCoordinateN(0);

		return new Position(coordinate.getY(), coordinate.getX());
	}
}
