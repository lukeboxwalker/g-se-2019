package de.techfak.gse.lwalkenhorst;

import java.io.File;

public final class GSERadio {

    private GSERadio() {
    }

    public static void main(final String... args) {
        System.out.println("Hello lwalkenhorst!");
        try {
            MusicReader musicReader = new MusicReader(args.length == 1 ? args[0] : null);
            File[] musicFiles = musicReader.getMusicFiles();
        } catch (NoMusicFileFoundException exception) {
            System.err.println(exception.getMessage());
        }
    }

}
