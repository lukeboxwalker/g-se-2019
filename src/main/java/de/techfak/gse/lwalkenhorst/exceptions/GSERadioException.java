package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * Base Exception thrown by GSERadio Application.
 */
public abstract class GSERadioException extends Exception {

    public static final long serialVersionUID = 42L;

    public GSERadioException(String message) {
        super(message);
    }

    public GSERadioException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "[ERROR] " + super.getMessage();
    }
}
