package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * File could not be loaded.
 */
public class FileNotLoadableException extends Exception {

    static final long serialVersionUID = 42L;

    public FileNotLoadableException(final String message) {
        super(message);
    }

    public FileNotLoadableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "[ERROR] " + super.getMessage();
    }
}
