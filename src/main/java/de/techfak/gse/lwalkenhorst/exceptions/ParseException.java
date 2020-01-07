package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * ParseException if something went wrong during parsing.
 */
public class ParseException extends GSERadioException {

    static final long serialVersionUID = 42L;

    public ParseException(final String message) {
        super(message);
    }
}
