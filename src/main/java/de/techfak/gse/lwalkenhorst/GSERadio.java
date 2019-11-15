package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.radioplayer.musicplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioview.Terminal;

/**
 * The GSERadio program is a music radio.
 * Responsible for memory management with the vlcj library
 *
 * @author Lukas Walkenhorst
 */
public final class GSERadio {

    private GSERadio() {
        Runtime.getRuntime().addShutdownHook(CleanUpDemon.CLEANUP_THREAD);
    }

    private void start(final String directoryPath) {
        MusicPlayer musicPlayer = new MusicPlayer();

        Terminal terminal = new Terminal(musicPlayer);
        terminal.listenForInstructions();

        Playlist playlist = new Playlist(directoryPath);
        playlist.shuffle();

        musicPlayer.play(playlist);
    }

    /**
     * Starts the program with given arguments.
     *
     * @param args the parameters the program is started with,
     *             e.g. the directory name
     */
    public static void main(final String... args) {
        GSERadio radio = new GSERadio();
        radio.start(args.length > 1 ? args[0] : System.getProperty("user.dir"));
    }
}
