package malecluk.garminparser.fileparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import malecluk.garminparser.FitConfiguration;
import malecluk.garminparser.exceptions.FitFileScanningException;

class FitFileScannerTest {

	@TempDir
	Path tempDirectory;

	@Test
	void findFitFiles_returnsFitFilesFromDirectory() throws Exception {
		Path fitFile1 = Files.createFile(tempDirectory.resolve("activity1.fit"));
		Path fitFile2 = Files.createFile(tempDirectory.resolve("activity2.fit"));

		Files.createFile(tempDirectory.resolve("activity.txt"));
		Files.createFile(tempDirectory.resolve("activity.csv"));

		FitFileScanner scanner = createScanner(tempDirectory);

		List<Path> result = scanner.findFitFiles();

		assertEquals(2, result.size());
		assertTrue(result.contains(fitFile1));
		assertTrue(result.contains(fitFile2));
	}

	@Test
	void findFitFiles_ignoresNonFitFiles() throws Exception {
		Files.createFile(tempDirectory.resolve("activity.txt"));
		Files.createFile(tempDirectory.resolve("activity.csv"));
		Files.createFile(tempDirectory.resolve("activity.fit.bak"));

		FitFileScanner scanner = createScanner(tempDirectory);

		List<Path> result = scanner.findFitFiles();

		assertTrue(result.isEmpty());
	}

	@Test
	void findFitFiles_isCaseInsensitiveForFitExtension() throws Exception {
		Path lowerCase = Files.createFile(tempDirectory.resolve("activity-lower.fit"));
	    Path upperCase = Files.createFile(tempDirectory.resolve("activity-upper.FIT"));
	    Path mixedCase = Files.createFile(tempDirectory.resolve("activity-mixed.FiT"));

		FitFileScanner scanner = createScanner(tempDirectory);

		List<Path> result = scanner.findFitFiles();

		assertEquals(3, result.size());
		assertTrue(result.contains(lowerCase));
		assertTrue(result.contains(upperCase));
		assertTrue(result.contains(mixedCase));
	}

	@Test
	void findFitFiles_returnsEmptyList_whenDirectoryIsEmpty() {
		FitFileScanner scanner = createScanner(tempDirectory);

		List<Path> result = scanner.findFitFiles();

		assertTrue(result.isEmpty());
	}

	@Test
	void findFitFiles_throwsException_whenDirectoryDoesNotExist() {
		Path nonExistingDirectory = tempDirectory.resolve("does-not-exist");

		FitFileScanner scanner = createScanner(nonExistingDirectory);

		FitFileScanningException exception = assertThrows(FitFileScanningException.class, scanner::findFitFiles);

		assertTrue(exception.getMessage().contains("does not exist"));
	}

	@Test
	void findFitFiles_throwsException_whenPathIsFile() throws Exception {
		Path file = Files.createFile(tempDirectory.resolve("not-a-directory.fit"));

		FitFileScanner scanner = createScanner(file);

		FitFileScanningException exception = assertThrows(FitFileScanningException.class, scanner::findFitFiles);

		assertTrue(exception.getMessage().contains("not a directory"));
	}

	@Test
	void findFitFiles_throwsException_whenDirectoryPathIsNull() {
		FitFileScanner scanner = createScanner((String) null);

		FitFileScanningException exception = assertThrows(FitFileScanningException.class, scanner::findFitFiles);

		assertEquals("Directory with .fit files is not configured!", exception.getMessage());
	}

	@Test
	void findFitFiles_throwsException_whenDirectoryPathIsBlank() {
		FitFileScanner scanner = createScanner("   ");

		FitFileScanningException exception = assertThrows(FitFileScanningException.class, scanner::findFitFiles);

		assertEquals("Directory with .fit files is not configured!", exception.getMessage());
	}

	private FitFileScanner createScanner(Path directory) {
		String directoryPath = directory == null ? null : directory.toString();

		FitConfiguration.FilesScanner filesScanner = new FitConfiguration.FilesScanner(directoryPath);

		FitConfiguration config = new FitConfiguration(null, null, null, filesScanner);

		return new FitFileScanner(config);
	}

	private FitFileScanner createScanner(String directoryPath) {
		FitConfiguration.FilesScanner filesScanner = new FitConfiguration.FilesScanner(directoryPath);

		FitConfiguration config = new FitConfiguration(null, null, null, filesScanner);

		return new FitFileScanner(config);
	}
}