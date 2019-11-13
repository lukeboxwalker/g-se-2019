package de.techfak.gse.lwalkenhorst.terminalScanner;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class Terminal {

    private static final String SONG = "song";
    private static final String PLAYLIST = "playlist";
    private static final String EXIT = "exit";

    private static final int EXIT_CODE = 1;
    private static final int BUFFER_WAIT = 200;

    private final RadioModel radio;

    private final AtomicBoolean running;

    public Terminal(RadioModel radio) {
        System.out.println("test");
        this.radio = radio;
        this.running = new AtomicBoolean(true);
        this.listenForInstructions();
    }

    private void listenForInstructions() {
        Thread terminalThread = new Thread(() -> {
            while (running.get()) {
                try {
                    String cmd = requestInput();
                    if (cmd != null) {
                        switch (cmd) {
                            case SONG:
                                printSongInfo(radio.getCurrentPlayingSong());
                                break;
                            case PLAYLIST:
                                printPlaylistInfo(radio.getCurrentPlaylist());
                                break;
                            case EXIT:
                                System.exit(EXIT_CODE);
                                break;
                            default:
                                System.out.println("Help msg");
                        }
                    }
                } catch (IOException e) {
                    this.kill();
                }
            }
        });
        terminalThread.start();
    }

    private String requestInput() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        do {
            try {
                while (!bufferedReader.ready() && running.get()) {
                    Thread.sleep(BUFFER_WAIT);
                }
                if (!running.get()) {
                    return null;
                }
                input = bufferedReader.readLine();
            } catch (InterruptedException e) {
                return null;
            }
        } while (input.isEmpty());
        return input;
    }

    public void kill() {
        running.set(false);
    }

    public void printSongInfo(Song song) {
        System.out.println(SONG);
    }

    public void printPlaylistInfo(Playlist playlist) {
        System.out.println(PLAYLIST);
    }
}
