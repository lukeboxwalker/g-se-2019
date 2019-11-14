package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.musicplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
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
                                printCMDUsage();
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
        final String line = "##########################################################";
        String message = line
            + "\nCurrent playing song:\n"
            + getMetaDataString("Title: ", song.getTitle())
            + getMetaDataString("Artist: ", song.getArtist())
            + getMetaDataString("Album: ", song.getAlbum())
            + getMetaDataString("Genre: ", song.getGenre())
            + getMetaDataString("Duration: ", song.getDuration())
            + line;
        System.out.println(message);
    }

    private String getMetaDataString(String tag, String metadata) {
        return metadata == null ? "" : tag + metadata + "\n";
    }

    private String getMetaDataString(String tag, long milliseconds) {
        long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds - TimeUnit.MINUTES.toMillis(min));
        return milliseconds <= 0 ? "" : tag + min + ":" + sec + " min\n";
    }

    public void printPlaylistInfo(Playlist playlist) {
        System.out.println(PLAYLIST);
    }

    public void printCMDUsage() {
        final String line = "##########################################################";
        String message = line + "\nCommand usage: \n"
            + "<song> to list the information of the current playing song.\n"
            + "<playlist> to list the current playlist that is playing.\n"
            + "<exit> to exit the application.\n" + line;
        System.out.println(message);
    }
}
