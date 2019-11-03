package de.techfak.gse.lwalkenhorst.exceptions;

public class NoMusicFileFormatException extends Exception {

    static final long serialVersionUID = 42L;

    public NoMusicFileFormatException(String message) {
        super(message);
    }
}
