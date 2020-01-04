package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;

public class NoConnectionException extends ExitCodeException {

    static final long serialVersionUID = 42L;
    private static final int EXIT_CODE = 101;

    public NoConnectionException(final String message) {
        super(message, EXIT_CODE);
    }

    public NoConnectionException(final String message, Throwable cause) {
        super(message, EXIT_CODE, cause);
    }
}
