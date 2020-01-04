package de.techfak.gse.lwalkenhorst.jsonparser;

/**
 * SerialisationException if serialisation fails.
 */
public class SerialisationException extends Exception {

    static final long serialVersionUID = 42L;

    public SerialisationException(final String message) {
        super(message);
    }
}
