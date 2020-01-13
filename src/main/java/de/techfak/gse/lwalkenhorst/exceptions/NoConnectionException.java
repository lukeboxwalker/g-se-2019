package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * NoConnectionException if no connection could be found.
 * Has exit code to that the program can use is to exit
 */
public class NoConnectionException extends ExitCodeException {

    static final long serialVersionUID = 42L;
    private static final int EXIT_CODE = 101;

    public NoConnectionException(final String message) {
        super(message);
    }

    public NoConnectionException(final String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getExitCode() {
        return EXIT_CODE;
    }
}
