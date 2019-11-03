package de.techfak.gse.lwalkenhorst.exceptions;

public class NoMusicFileFoundException extends ErrorCodeException {

    static final long serialVersionUID = 42L;

    public NoMusicFileFoundException(String message) {
        super(message, 101);
    }
}
