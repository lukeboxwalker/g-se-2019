package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * File could not be loaded.
 */
public class FileNotLoadableException extends GSERadioException {

    static final long serialVersionUID = 42L;

    public FileNotLoadableException(final String message) {
        super(message);
    }

    public FileNotLoadableException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
