package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * File could not be loaded.
 */
public class FileNotLoadableException extends Exception {

    static final long serialVersionUID = 42L;

    public FileNotLoadableException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "[ERROR] " + super.getMessage();
    }
}
