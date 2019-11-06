package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * The file has not the correct music file format.
 */
public class NoMusicFileFormatException extends Exception {

    static final long serialVersionUID = 42L;

    public NoMusicFileFormatException(String message) {
        super(message);
    }
}
