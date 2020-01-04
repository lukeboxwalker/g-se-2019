package de.techfak.gse.lwalkenhorst.argumentparser;

public class ParseException extends Exception {

    static final long serialVersionUID = 42L;

    public ParseException(final String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "[ERROR] " + super.getMessage();
    }
}
