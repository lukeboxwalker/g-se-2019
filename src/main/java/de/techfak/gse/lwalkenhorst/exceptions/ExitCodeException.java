package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * An Exception that is supposed to exit the program.
 * The Exception has an explicit exit code
 */
public abstract class ExitCodeException extends GSERadioException {

    public static final long serialVersionUID = 42L;
    private final int exitCode;

    public ExitCodeException(final String message, final int errorCode) {
        super(message);
        this.exitCode = errorCode;
    }

    public ExitCodeException(final String message, final int errorCode, Throwable cause) {
        super(message, cause);
        this.exitCode = errorCode;
    }

    public int getExitCode() {
        return exitCode;
    }

}
