package de.techfak.gse.lwalkenhorst.argumentparser;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;

/**
 * OverlappingOptionException when arguments overlap.
 * Should be thrown if to arguments overlap and could not
 * start the program together.
 */
public class OverlappingOptionException extends ExitCodeException {

    static final long serialVersionUID = 42L;
    private static final int EXIT_CODE = 103;

    public OverlappingOptionException(final String message) {
        super(message, EXIT_CODE);
    }
}
