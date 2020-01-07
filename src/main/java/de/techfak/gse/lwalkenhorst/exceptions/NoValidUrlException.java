package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * NoValidUrlException if url is not valid.
 */
public class NoValidUrlException extends GSERadioException {

    static final long serialVersionUID = 42L;

    public NoValidUrlException(final String message) {
        super(message);
    }
}
