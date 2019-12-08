package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.PlaylistFactory;
import de.techfak.gse.lwalkenhorst.radioview.GuiApplication;
import de.techfak.gse.lwalkenhorst.radioview.Terminal;

import java.util.Arrays;
import java.util.List;

/**
 * The GSERadio program is a music radio.
 *
 * @author Lukas Walkenhorst
 */
public final class GSERadio {

    private static final List<String> GUI_ARGS = Arrays.asList("-g", "--gui");

    private GSERadio() {
    }

    private void start(final String directoryPath, boolean guiMode) {
        try {
            PlaylistFactory factory = new PlaylistFactory(directoryPath);
            Playlist playlist = factory.newPlaylist();
            playlist.shuffle();

            MusicPlayer musicPlayer = new MusicPlayer(playlist);
            musicPlayer.play();

            if (guiMode) {
                GuiApplication.start(musicPlayer, "-a");
            } else {
                Terminal.start(musicPlayer);
            }


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
        final String systemPath = System.getProperty("user.dir");
        if (args.length >= 1 && GUI_ARGS.contains(args[0])) {
            radio.start(args.length >= 2 ? args[1] : systemPath, true);
        } else {
            radio.start(args.length >= 1 ? args[0] : systemPath, false);
        }
    }
}
