package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.radioplayer.musicplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.playlist.Playlist;
import de.techfak.gse.lwalkenhorst.radioview.GuiApplication;
import de.techfak.gse.lwalkenhorst.radioview.Terminal;

/**
 * The GSERadio program is a music radio.
 *
 * @author Lukas Walkenhorst
 */
public final class GSERadio {

    private GSERadio() {
    }

    private void start(final String directoryPath) {
        MusicPlayer musicPlayer = new MusicPlayer();

        Playlist playlist = new Playlist(directoryPath);
        playlist.shuffle();

        musicPlayer.loadPlaylist(playlist);
        musicPlayer.play();

        Terminal.start(musicPlayer);
        GuiApplication.start(musicPlayer);
    }

    /**
     * Starts the program with given arguments.
     *
     * @param args the parameters the program is started with,
     *             e.g. the directory name
     */
    public static void main(final String... args) {
        GSERadio radio = new GSERadio();
        radio.start(args.length >= 1 ? args[0] : System.getProperty("user.dir"));
    }
}
