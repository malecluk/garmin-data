package malecluk.garminparser.fileparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import malecluk.garminparser.model.SourceFileHash;

class SourceFileHasherTest {

	private final SourceFileHasher sourceFileHasher = new SourceFileHasher();

	@TempDir
	Path tempDir;

	@Test
	void calculate_sameFile_returnsSameHash() throws IOException {
		Path file = tempDir.resolve("activity.fit");
		Files.writeString(file, "test FIT file content");

		SourceFileHash firstHash = sourceFileHasher.calculate(file);
		SourceFileHash secondHash = sourceFileHasher.calculate(file);

		assertEquals(firstHash, secondHash);
	}

	@Test
	void calculate_differentFiles_returnsDifferentHashes() throws IOException {
		Path firstFile = tempDir.resolve("first.fit");
		Path secondFile = tempDir.resolve("second.fit");

		Files.writeString(firstFile, "first FIT file content");
		Files.writeString(secondFile, "second FIT file content");

		SourceFileHash firstHash = sourceFileHasher.calculate(firstFile);
		SourceFileHash secondHash = sourceFileHasher.calculate(secondFile);

		assertNotEquals(firstHash, secondHash);
	}

	@Test
	void calculate_nonExistingFile_throwsIOException() {
		Path nonExistingFile = tempDir.resolve("non-existing.fit");

		assertThrows(IOException.class, () -> sourceFileHasher.calculate(nonExistingFile));
	}

	@Test
	void calculate_knownContent_returnsExpectedSha256() throws IOException {
		Path file = tempDir.resolve("activity.fit");
		Files.writeString(file, "abc");

		SourceFileHash result = sourceFileHasher.calculate(file);

		assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", result.value());
	}
}
