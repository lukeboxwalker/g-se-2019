package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;

import java.io.File;
import java.util.List;

public final class GSERadio {

    private GSERadio() {
    }

    private void start(final String... args) {
        try {
            MusicReader musicReader = new MusicReader(args.length == 1 ? args[0] : null);
            List<File> fileList = musicReader.getMusicFiles();
            Playlist playlist = new Playlist(fileList);
        } catch (NoMusicFileFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(final String... args) {
        //System.out.println("Hello lwalkenhorst!");
        System.out.println("Started MediaPlayer");
        GSERadio radio = new GSERadio();
        radio.start(args);
    }
}
