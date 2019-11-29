package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.PlaylistFactory;
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
        try {
            MusicPlayer musicPlayer = new MusicPlayer();

            PlaylistFactory factory = new PlaylistFactory(directoryPath);
            Playlist playlist = factory.newPlaylist();
            playlist.shuffle();

            musicPlayer.loadPlaylist(playlist);
            musicPlayer.play();

            Terminal.start(musicPlayer);
            GuiApplication.start(musicPlayer);

        } catch (NoMusicFileFoundException e) {
            System.err.println(e.getMessage());
            System.exit(e.getExitCode());
        }
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
