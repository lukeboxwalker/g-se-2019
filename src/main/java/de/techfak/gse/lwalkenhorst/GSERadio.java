package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.apiwrapper.VLCJApiWrapper;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;

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
        musicPlayer.play(playlist);
    }

    private void exit() {
        apiWrapper.release();
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
