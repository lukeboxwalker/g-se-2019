package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * StreamFailedException if vlcj could not stream music.
 */
public class StreamFailedException extends ExitCodeException {

    static final long serialVersionUID = 42L;
    private static final int EXIT_CODE = 102;

    public StreamFailedException(String message) {
        super(message, EXIT_CODE);
    }

    public StreamFailedException(String message, Throwable cause) {
        super(message, EXIT_CODE, cause);
    }
}
