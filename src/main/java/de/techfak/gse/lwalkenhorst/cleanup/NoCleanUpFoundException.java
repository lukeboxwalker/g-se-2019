package de.techfak.gse.lwalkenhorst.cleanup;

/**
 * If an expected cleanup wasn't registered.
 */
public class NoCleanUpFoundException extends Exception {

    static final long serialVersionUID = 42L;

    public NoCleanUpFoundException(String message) {
        super(message);
    }
}
