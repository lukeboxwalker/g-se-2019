package de.techfak.gse.lwalkenhorst.radioplayer;

public class NoConnectionException extends Exception {

    static final long serialVersionUID = 42L;

    public NoConnectionException(final String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "[ERROR] " + super.getMessage();
    }
}
