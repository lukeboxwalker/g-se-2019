package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * It was no music file found.
 */
public class NoMusicFileFoundException extends ExitCodeException {

    static final long serialVersionUID = 42L;
    private static final int EXIT_CODE = 100;

    public NoMusicFileFoundException(final String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return EXIT_CODE;
    }
}
