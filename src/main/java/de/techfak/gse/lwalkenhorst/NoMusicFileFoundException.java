package de.techfak.gse.lwalkenhorst;

public class NoMusicFileFoundException extends Exception {

    static final long serialVersionUID = 42L;

    public NoMusicFileFoundException(String message) {
        super(message);
    }
}
