package malecluk.garminparser.fileparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import malecluk.garminparser.FitConfiguration;
import malecluk.garminparser.model.Sport;
import malecluk.garminparser.model.SubSport;
import malecluk.garminparser.model.activities.Activity;

class FitFileRenamerTest {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
			.withZone(ZoneId.systemDefault());

	private static final Instant START_TIME = Instant.parse("2026-09-15T19:12:51Z");

	@TempDir
	Path tempDir;

	private FitFileRenamer renamer;

	@BeforeEach
	void setUp() {
		FitConfiguration config = mock(FitConfiguration.class);
		FitConfiguration.FileRenamer fileRenamerConfig = mock(FitConfiguration.FileRenamer.class);

		when(config.fileRenamer()).thenReturn(fileRenamerConfig);
		when(fileRenamerConfig.enabled()).thenReturn(true);

		renamer = new FitFileRenamer(config);
	}

	@Test
	void rename_disabled_doesNothing() throws IOException {
		FitConfiguration config = mock(FitConfiguration.class);
		FitConfiguration.FileRenamer fileRenamerConfig = mock(FitConfiguration.FileRenamer.class);

		when(config.fileRenamer()).thenReturn(fileRenamerConfig);
		when(fileRenamerConfig.enabled()).thenReturn(false);

		renamer = new FitFileRenamer(config);

		Path originalPath = createFile("24375337180.fit");
		Activity activity = createActivity(originalPath);

		renamer.rename(activity);

		assertTrue(Files.exists(originalPath));
		assertEquals(originalPath, activity.getFilePath());
	}

	@Test
	void rename_withoutFilePath_doesNothing() throws IOException {
		Activity activity = createActivity(null);

		renamer.rename(activity);

		assertEquals(null, activity.getFilePath());
	}

	@Test
	void rename_withInvalidFileName_doesNothing() throws IOException {
		Path originalPath = createFile("1234567890.fit");
		Activity activity = createActivity(originalPath);

		renamer.rename(activity);

		assertTrue(Files.exists(originalPath));
		assertEquals(originalPath, activity.getFilePath());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"24375337180.fit",
			"24375337180.FIT",
			"24375337180.Fit",
			"24375337180.fIt",
			"24375337180_ACTIVITY.fit",
			"24375337180_ACtiVITY.FIT",
			"24375337180_activity.FiT"
	})
	void rename_acceptsFitExtensionAndActivitySuffixInAnyCase(String fileName) throws IOException {
		Path originalPath = createFile(fileName);
		Activity activity = createActivity(originalPath);

		String expectedFileName = expectedFileName("24375337180");
		Path expectedPath = tempDir.resolve(expectedFileName);

		renamer.rename(activity);

		assertFalse(Files.exists(originalPath));
		assertTrue(Files.exists(expectedPath));
		assertEquals(expectedPath, activity.getFilePath());
	}

	@Test
	void rename_validFile_renamesFileAndUpdatesActivityPath() throws IOException {
		Path originalPath = createFile("24375337180.fit");
		Activity activity = createActivity(originalPath);

		String expectedFileName = expectedFileName("24375337180");

		renamer.rename(activity);

		Path expectedPath = tempDir.resolve(expectedFileName);

		assertFalse(Files.exists(originalPath));
		assertTrue(Files.exists(expectedPath));
		assertEquals(expectedPath, activity.getFilePath());
	}

	@Test
	void rename_activityFile_renamesFileAndUpdatesActivityPath() throws IOException {
		Path originalPath = createFile("24375337180_ACTIVITY.fit");
		Activity activity = createActivity(originalPath);

		String expectedFileName = expectedFileName("24375337180");

		renamer.rename(activity);

		Path expectedPath = tempDir.resolve(expectedFileName);

		assertFalse(Files.exists(originalPath));
		assertTrue(Files.exists(expectedPath));
		assertEquals(expectedPath, activity.getFilePath());
	}

	@Test
	void rename_whenTargetAlreadyExists_doesNothing() throws IOException {
		Path originalPath = createFile("24375337180.fit");
		Activity activity = createActivity(originalPath);

		String expectedFileName = expectedFileName("24375337180");
		Path targetPath = tempDir.resolve(expectedFileName);

		Files.createFile(targetPath);

		renamer.rename(activity);

		assertTrue(Files.exists(originalPath));
		assertTrue(Files.exists(targetPath));
		assertEquals(originalPath, activity.getFilePath());
	}

	@Test
	void rename_withTooManyDigits_doesNothing() throws IOException {
		Path originalPath = createFile("123456789012.fit");
		Activity activity = createActivity(originalPath);

		renamer.rename(activity);

		assertTrue(Files.exists(originalPath));
		assertEquals(originalPath, activity.getFilePath());
	}

	@Test
	void rename_withInvalidSuffix_doesNothing() throws IOException {
		Path originalPath = createFile("12345678901_OTHER.fit");
		Activity activity = createActivity(originalPath);

		renamer.rename(activity);

		assertTrue(Files.exists(originalPath));
		assertEquals(originalPath, activity.getFilePath());
	}

	private Activity createActivity(Path filePath) {
		Activity activity = new Activity();

		activity.setFilePath(filePath);
		activity.setStartTime(START_TIME);
		activity.setSport(Sport.FLOOR_CLIMBING);
		activity.setSubSport(SubSport.GENERIC);

		return activity;
	}

	private Path createFile(String fileName) throws IOException {
		Path path = tempDir.resolve(fileName);
		Files.createFile(path);
		return path;
	}

	private String expectedFileName(String number) {
		String formattedTime = DATE_FORMAT.format(START_TIME);

		return number + "_" + formattedTime + "_" + Sport.FLOOR_CLIMBING + "-" + SubSport.GENERIC + ".fit";
	}
}