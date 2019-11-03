package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;

import java.io.File;
import java.util.List;

public final class GSERadio {

    private GSERadio() {
    }

    public static void main(final String... args) {
        System.out.println("Hello lwalkenhorst!");
        try {
            MusicReader musicReader = new MusicReader(args.length == 1 ? args[0] : null);
            List<File> musicFiles = musicReader.getMusicFiles();
        } catch (NoMusicFileFoundException exception) {
            System.err.println(exception.getMessage());
        }
    }

}
