package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;

import java.io.File;
import java.util.List;

public final class GSERadio {

    private static final MusicPlayer musicPlayer = new MusicPlayer();

    private GSERadio() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::exit));
    }

    private void start(final String... args) {
        try {
            MusicReader musicReader = new MusicReader(args.length == 1 ? args[0] : null);
            List<File> fileList = musicReader.getMusicFiles();
            Playlist playlist = new Playlist(fileList);
            musicPlayer.play(playlist);
        } catch (ExitCodeException e) {
            System.err.println(e.getMessage());
            System.exit(e.getExitCode());
        }
    }

    public void exit() {
        musicPlayer.release();
    }

    public static void main(final String... args) {
        //System.out.println("Hello lwalkenhorst!");
        System.out.println("Started MediaPlayer");
        GSERadio radio = new GSERadio();
        radio.start(args);
    }
}
