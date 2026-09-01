package malecluk.garminparser.fileparser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import malecluk.garminparser.model.SourceFileHash;

/**
 * Calculates a cryptographic hash of a source FIT file.
 *
 * <p>The hash is calculated from the complete contents of the file using
 * the SHA-256 algorithm. It can therefore be used to recognize the exact
 * source file from which an activity was imported.</p>
 */
@Component
public class SourceFileHasher {

    private static final int BUFFER_SIZE = 8192;

    /**
     * Calculates the SHA-256 hash of the specified file.
     *
     * @param filePath path to the source file
     * @return the SHA-256 hash of the complete file contents
     * @throws IOException if the file cannot be read
     */
    public SourceFileHash calculate(Path filePath) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            try (InputStream inputStream = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }

            return new SourceFileHash(toHex(digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is not available.", e);
        }
    }

    /**
     * Converts the specified byte array to a lowercase hexadecimal string.
     *
     * @param bytes bytes to convert
     * @return lowercase hexadecimal representation of the bytes
     */
    private String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);

        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}
