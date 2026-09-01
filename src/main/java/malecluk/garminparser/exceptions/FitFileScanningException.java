package malecluk.garminparser.exceptions;

/**
 * Own exception for reporting errors during scanning directory for existing .fit files.
 */
public class FitFileScanningException extends RuntimeException {

	private static final long serialVersionUID = -4845925468346061746L;

	public FitFileScanningException(String message) {
        super(message);
    }

    public FitFileScanningException(String message, Throwable cause) {
        super(message, cause);
    }
}
