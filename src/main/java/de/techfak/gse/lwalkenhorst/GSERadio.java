package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
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

    /**
     * Starts the program with given arguments.
     * Can start with given directory to read mp3 files from.
     * Can start with -g, --gui to start gui mode
     *
     * @param args the parameters the program is started with.
     */
    public static void main(final String... args) {
        final String systemPath = System.getProperty("user.dir");
        final String directory;
        final boolean guiMode;
        if (args.length >= 1 && GUI_ARGS.contains(args[0])) {
            directory = args.length >= 2 ? args[1] : systemPath;
            guiMode = true;
        } else {
            directory = args.length >= 1 ? args[0] : systemPath;
            guiMode = false;
        }
        try {
            final PlaylistFactory factory = new PlaylistFactory(directory);
            final Playlist playlist = factory.newPlaylist();
            playlist.shuffle();

            final MusicPlayer musicPlayer = new MusicPlayer(playlist);
            musicPlayer.play();

            if (guiMode) {
                GuiApplication.start(musicPlayer, "-a");
            } else {
                final Terminal terminal = new Terminal(musicPlayer);
                terminal.listenForInstructions();
            }
        } catch (NoMusicFileFoundException e) {
            System.err.println(e.getMessage());
            System.exit(e.getExitCode());
        } finally {
            CleanUpDemon.getInstance().cleanup();
        }
    }
}
