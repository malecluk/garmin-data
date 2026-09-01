package malecluk.garminparser.fileparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.garmin.fit.File;
import com.garmin.fit.FileEncoder;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.Fit;
import com.garmin.fit.FitRuntimeException;
import com.garmin.fit.Manufacturer;

import malecluk.garminparser.fileparser.dto.ParsedFitFileMessagesDTO;
import malecluk.garminparser.model.SourceFileHash;

@ExtendWith(MockitoExtension.class)
class FitFileParserTest {

	@TempDir
	Path tempDir;

	@Mock
	private SourceFileHasher sourceFileHasher;

	private FitFileParser fitFileParser;

	@BeforeEach
	void setUp() {
		fitFileParser = new FitFileParser(sourceFileHasher);
	}

	@Test
	void parseFile_validFitFile_returnsParsedMessagesWithSourceFileInfo() throws IOException {
		Path fitFile = createValidFitFile();
		SourceFileHash sourceFileHash = new SourceFileHash("test-source-file-hash");

		when(sourceFileHasher.calculate(fitFile)).thenReturn(sourceFileHash);

		ParsedFitFileMessagesDTO result = fitFileParser.parseFile(fitFile);

		assertNotNull(result);
		assertEquals(fitFile, result.getSourceFilePath());
		assertEquals(sourceFileHash, result.getSourceFileHash());

		assertEquals(1, result.getFileIdMesgList().size());
		assertEquals(File.ACTIVITY, result.getFileIdMesgList().get(0).getType());

		verify(sourceFileHasher).calculate(fitFile);
	}

	@Test
	void parseFile_invalidFitFile_returnsNull() throws IOException {
		Path invalidFitFile = tempDir.resolve("invalid.fit");

		java.nio.file.Files.writeString(invalidFitFile, "This is not a FIT file.");

		ParsedFitFileMessagesDTO result = fitFileParser.parseFile(invalidFitFile);

		assertNull(result);
	}

	private Path createValidFitFile() {
		Path fitFile = tempDir.resolve("activity.fit");

		FileIdMesg fileIdMesg = new FileIdMesg();
		fileIdMesg.setType(File.ACTIVITY);
		fileIdMesg.setManufacturer(Manufacturer.GARMIN);
		fileIdMesg.setProduct(1);
		fileIdMesg.setSerialNumber(123456789L);

		FileEncoder encoder = null;

		try {
			encoder = new FileEncoder(fitFile.toFile(), Fit.ProtocolVersion.V2_0);
			encoder.write(fileIdMesg);
			encoder.close();
		} catch (FitRuntimeException e) {
			if (encoder != null) {
				try {
					encoder.close();
				} catch (FitRuntimeException ignored) {
					// Preserve the original exception.
				}
			}

			throw e;
		}

		return fitFile;
	}
}