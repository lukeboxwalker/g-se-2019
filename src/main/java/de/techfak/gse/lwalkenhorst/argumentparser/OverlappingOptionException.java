package de.techfak.gse.lwalkenhorst.argumentparser;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;

public class OverlappingOptionException extends ExitCodeException {

    static final long serialVersionUID = 42L;
    private static final int EXIT_CODE = 103;

    public OverlappingOptionException(final String message) {
        super(message, EXIT_CODE);
    }
}
