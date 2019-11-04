package de.techfak.gse.lwalkenhorst.exceptions;

public class NoMusicFileFoundException extends ExitCodeException {

    static final long serialVersionUID = 42L;
    private static final int EXIT_CODE = 100;

    public NoMusicFileFoundException(String message) {
        super(message, EXIT_CODE);
    }
}
