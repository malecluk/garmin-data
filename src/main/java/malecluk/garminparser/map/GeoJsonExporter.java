package malecluk.garminparser.map;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Exports geographic geometries as GeoJSON files.
 */
@Component
public class GeoJsonExporter {

	private final Path outputPath;

	public GeoJsonExporter(@Value("${map.visited-area.output-path:visited-area.geojson}") String outputPath) {

		this.outputPath = Path.of(Objects.requireNonNull(outputPath, "outputPath must not be null"));
	}

	/**
	 * Writes a geometry as a GeoJSON Feature to the configured output file.
	 *
	 * @param geometry geometry to export, expressed in WGS84 coordinates
	 * @throws NullPointerException if the geometry is null
	 * @throws IOException          if the output file cannot be written
	 */
	public void export(Geometry geometry) throws IOException {
		Objects.requireNonNull(geometry, "geometry must not be null");

		GeoJsonWriter writer = new GeoJsonWriter();
		String geometryJson = writer.write(geometry);

		String featureJson = """
				{
				  "type": "Feature",
				  "properties": {},
				  "geometry": %s
				}
				""".formatted(geometryJson);

		Path absolutePath = outputPath.toAbsolutePath();
		Path parent = absolutePath.getParent();

		if (parent != null) {
			Files.createDirectories(parent);
		}

		Files.writeString(absolutePath, featureJson, StandardCharsets.UTF_8);
	}
}