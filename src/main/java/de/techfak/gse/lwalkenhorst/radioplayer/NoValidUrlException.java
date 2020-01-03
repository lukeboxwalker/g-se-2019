package de.techfak.gse.lwalkenhorst.radioplayer;

public class NoValidUrlException extends Exception {

    static final long serialVersionUID = 42L;

    public NoValidUrlException(final String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "[ERROR] " + super.getMessage();
    }
}
