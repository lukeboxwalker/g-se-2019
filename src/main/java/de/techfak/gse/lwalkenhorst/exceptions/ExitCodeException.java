package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * An Exception that is supposed to exit the program.
 * The Exception has an explicit exit code
 */
public abstract class ExitCodeException extends GSERadioException {

    public static final long serialVersionUID = 42L;

    public ExitCodeException(String message) {
        super(message);
    }

    public ExitCodeException(String message, Throwable cause) {
        super(message, cause);
    }


    public abstract int getExitCode();

}
