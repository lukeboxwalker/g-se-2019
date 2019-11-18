package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.radioplayer.playlist.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.musicplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.song.MetaData;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The console listener to perform commands.
 */
public class Terminal {
    private static final String ENDING = "##########################################################";
    private static final String BREAK = "\n";

    private static final String SONG = "song";
    private static final String PLAYLIST = "playlist";
    private static final String EXIT = "exit";

    private static final String SHUFFLE = "shuffle";
    private static final String SKIP = "skip";

    private static final int EXIT_CODE = 1;
    private static final int BUFFER_WAIT = 200;

    private final RadioModel radio;

    private final AtomicBoolean running;

    public Terminal(RadioModel radio) {
        this.radio = radio;
        this.running = new AtomicBoolean(false);
    }

    /**
     * Start listening for console input.
     * Setting running to true and starting listener thread {@link #startListener()}
     */
    public void listenForInstructions() {
        if (!running.get()) {
            running.set(true);
            startListener();
        }
    }

    /**
     * Start listens to listen for incoming commands.
     * Using {@link #requestInput()} to read next input line.
     * Listener thread can be killed with {@link #kill()}
     */
    private void startListener() {
        new Thread(() -> {
            while (running.get()) {
                try {
                    String cmd = requestInput();
                    if (cmd != null) {
                        switch (cmd) {
                            case SONG:
                                printSongInfo(radio.getSong());
                                break;
                            case PLAYLIST:
                                printPlaylistInfo(radio.getPlaylist());
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
        }).start();
    }

    /**
     * Reading console input.
     * Using BufferedReader to read system input in console
     *
     * @return the String input or null when listener was killed or interrupted.
     */
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

    /**
     * Prints the information given by a song.
     * Formats the metadata in a readable way to be printed.
     * Only prints existed metadata from the song.
     *
     * @param song that will be printed
     */
    private void printSongInfo(Song song) {
        String header = "#####################[ Current song ]#####################";
        String message;
        if (song == null) {
            message = header + "\nNothing\n" + ENDING;
        } else {
            message = header + BREAK + song.toString() + BREAK + ENDING;
        }
        System.out.println(message);
    }

    /**
     * Prints the information given by a playlist.
     * Lists all song that will be played from the radio.
     * Prints the song in a compact form.
     *
     * @param playlist that will be printed
     */
    private void printPlaylistInfo(Playlist playlist) {
        String header = "###################[ Current playlist ]###################";
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(header);
        if (playlist == null) {
            messageBuilder.append("\nEmpty\n");
        } else {
            playlist.forEachSong((song) -> {
                messageBuilder.append(BREAK);
                messageBuilder.append(song.toString());
            });
        }
        messageBuilder.append(BREAK);
        messageBuilder.append(ENDING);
        System.out.println(messageBuilder.toString());
    }

    /**
     * Prints a help message.
     */
    private void printCMDUsage() {
        String message = "#########################[ Help ]#########################"
            + "\n<song> to list the information of the current playing song.\n"
            + "<playlist> to list the current playlist that is playing.\n"
            + "<exit> to exit the application.\n" + ENDING;
        System.out.println(message);
    }

    private String getMetaDataString(String tag, String metadata, boolean lineBreak) {
        return metadata == null ? "" : tag + metadata + (lineBreak ? BREAK : "");
    }

    private String getMetaDataString(String tag, long milliseconds, boolean lineBreak) {
        long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds - TimeUnit.MINUTES.toMillis(min));
        return milliseconds <= 0 ? "" : tag + min + ":" + sec + " min" + (lineBreak ? BREAK : "");
    }
}
