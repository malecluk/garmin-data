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
	void findFitFiles_returnsFitFilesFromMultipleDirectories() throws Exception {
		Path firstDirectory = Files.createDirectory(tempDirectory.resolve("2029-09-fit"));
		Path secondDirectory = Files.createDirectory(tempDirectory.resolve("2029-10-fit"));

		Path fitFile1 = Files.createFile(firstDirectory.resolve("activity1.fit"));
		Path fitFile2 = Files.createFile(firstDirectory.resolve("activity2.fit"));
		Path fitFile3 = Files.createFile(secondDirectory.resolve("activity3.fit"));

		FitFileScanner scanner = createScanner(List.of(firstDirectory.toString(), secondDirectory.toString()));

		List<Path> result = scanner.findFitFiles();

		assertEquals(3, result.size());
		assertTrue(result.contains(fitFile1));
		assertTrue(result.contains(fitFile2));
		assertTrue(result.contains(fitFile3));
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
	void findFitFiles_returnsFilesFromNonEmptyDirectories_whenAnotherDirectoryIsEmpty() throws Exception {
		Path firstDirectory = Files.createDirectory(tempDirectory.resolve("2029-09-fit"));
		Path secondDirectory = Files.createDirectory(tempDirectory.resolve("2029-10-fit"));

		Path fitFile = Files.createFile(firstDirectory.resolve("activity.fit"));

		FitFileScanner scanner = createScanner(List.of(firstDirectory.toString(), secondDirectory.toString()));

		List<Path> result = scanner.findFitFiles();

		assertEquals(1, result.size());
		assertTrue(result.contains(fitFile));
	}

	@Test
	void findFitFiles_throwsException_whenDirectoryDoesNotExist() {
		Path nonExistingDirectory = tempDirectory.resolve("does-not-exist");

		FitFileScanner scanner = createScanner(nonExistingDirectory);

		FitFileScanningException exception = assertThrows(FitFileScanningException.class, scanner::findFitFiles);

		assertTrue(exception.getMessage().contains("does not exist"));
	}

	@Test
	void findFitFiles_throwsException_whenOneOfMultipleDirectoriesDoesNotExist() throws Exception {
		Path existingDirectory = Files.createDirectory(tempDirectory.resolve("2029-09-fit"));
		Files.createFile(existingDirectory.resolve("activity.fit"));

		Path nonExistingDirectory = tempDirectory.resolve("2029-10-fit");

		FitFileScanner scanner = createScanner(List.of(existingDirectory.toString(), nonExistingDirectory.toString()));

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
	void findFitFiles_throwsException_whenOneOfMultiplePathsIsFile() throws Exception {
		Path directory = Files.createDirectory(tempDirectory.resolve("2029-09-fit"));
		Files.createFile(directory.resolve("activity.fit"));

		Path file = Files.createFile(tempDirectory.resolve("2029-10-fit"));

		FitFileScanner scanner = createScanner(List.of(directory.toString(), file.toString()));

		FitFileScanningException exception = assertThrows(FitFileScanningException.class, scanner::findFitFiles);

		assertTrue(exception.getMessage().contains("not a directory"));
	}

	@Test
	void filesScanner_throwsException_whenDirectoryPathsIsNull() {
		assertThrows(NullPointerException.class, () -> new FitConfiguration.FilesScanner(null));
	}

	@Test
	void findFitFiles_throwsException_whenDirectoryPathIsBlank() {
		FitFileScanner scanner = createScanner("   ");

		FitFileScanningException exception = assertThrows(FitFileScanningException.class, scanner::findFitFiles);

		assertEquals("Invalid directory path configured:    ", exception.getMessage());
	}

	@Test
	void findFitFiles_throwsException_whenDirectoryPathsIsEmpty() {
		FitFileScanner scanner = createScanner(List.of());

		FitFileScanningException exception = assertThrows(FitFileScanningException.class, scanner::findFitFiles);

		assertEquals("Directory with .fit files is not configured!", exception.getMessage());
	}

	private FitFileScanner createScanner(Path directory) {
		return createScanner(List.of(directory.toString()));
	}

	private FitFileScanner createScanner(String directoryPath) {
		return createScanner(List.of(directoryPath));
	}

	private FitFileScanner createScanner(List<String> directoryPaths) {
		FitConfiguration.FilesScanner filesScanner = new FitConfiguration.FilesScanner(directoryPaths);

		FitConfiguration config = new FitConfiguration(null, null, null, filesScanner);

		return new FitFileScanner(config);
	}
}