package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.apiwrapper.VLCJApiWrapper;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.terminalScanner.Terminal;

/**
 * The GSERadio program is a music radio.
 * Responsible for memory management with the vlcj library
 *
 * @author Lukas Walkenhorst
 */
public final class GSERadio {

    private VLCJApiWrapper apiWrapper;

    private GSERadio() {
        this.apiWrapper = new VLCJApiWrapper();
        Runtime.getRuntime().addShutdownHook(new Thread(this::exit));
    }

    private void start(final String directoryPath) {
        MusicPlayer musicPlayer = new MusicPlayer(apiWrapper);
        Playlist playlist = new Playlist(directoryPath, apiWrapper);

        playlist.shuffle();
        musicPlayer.play(playlist);

        Terminal terminal = new Terminal(musicPlayer);
        terminal.listenForInstructions();
    }

    private void exit() {
        apiWrapper.release();
    }

    /**
     * Starts the program with given arguments.
     *
     * @param args the parameters the program is started with,
     *             e.g. the directory
     */
    public static void main(final String... args) {
        GSERadio radio = new GSERadio();
        radio.start(args.length > 1 ? args[0] : System.getProperty("user.dir"));
    }
}
