package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private static final int BUFFER_WAIT = 200;

    private final RadioPlayer radio;

    private final AtomicBoolean running;

    /**
     * Creates a new cli (Terminal) object.
     * @param radio the musicPlayer
     */
    public Terminal(final RadioPlayer radio) {
        this.radio = radio;
        this.running = new AtomicBoolean(false);
        radio.start();
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
     * Listener can be killed with {@link #kill()}
     */
    private void startListener() {
        while (running.get()) {
            try {
                final String cmd = requestInput();
                if (cmd != null) {
                    switch (cmd) {
                        case SONG:
                            printSongInfo(radio.getSong());
                            break;
                        case PLAYLIST:
                            printPlaylistInfo(radio.getPlaylist());
                            break;
                        case EXIT:
                            this.kill();
                            break;
                        default:
                            printCMDUsage();
                            break;
                    }
                }
            } catch (IOException e) {
                this.kill();
            }
        }
    }

    /**
     * Reading console input.
     * Using BufferedReader to read system input in console
     *
     * @return the String input or null when listener was killed or interrupted.
     */
    private String requestInput() throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
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
    private void printSongInfo(final Song song) {
        final String header = "#####################[ Current song ]#####################";
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
    private void printPlaylistInfo(final Playlist playlist) {
        final String header = "###################[ Current playlist ]###################";
        final StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(header);
        if (playlist == null) {
            messageBuilder.append("\nEmpty\n");
        } else {
            radio.getPlaylist().getSongs().forEach((song) -> {
                messageBuilder.append(BREAK);
                messageBuilder.append(song.toString());
            });
        }
        messageBuilder.append(BREAK).append(ENDING);
        System.out.println(messageBuilder.toString());
    }

    /**
     * Prints a help message.
     */
    private void printCMDUsage() {
        final String message = "#########################[ Help ]#########################"
            + "\n<song> to list the information of the current playing song.\n"
            + "<playlist> to list the current playlist that is playing.\n"
            + "<exit> to exit the application.\n" + ENDING;
        System.out.println(message);
    }
}
