package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.argumentparser.ArgumentParser;
import de.techfak.gse.lwalkenhorst.argumentparser.ICommandLine;
import de.techfak.gse.lwalkenhorst.argumentparser.OverlappingOptionException;
import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.PlaylistFactory;
import de.techfak.gse.lwalkenhorst.radioview.GuiApplication;
import de.techfak.gse.lwalkenhorst.radioview.Terminal;
import org.apache.commons.cli.ParseException;

/**
 * The GSERadio program is a music radio.
 *
 * @author Lukas Walkenhorst
 */
public final class GSERadio {

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

        final ArgumentParser argumentParser = new ArgumentParser();
        try {
            final ICommandLine commandLine = argumentParser.parse(args);
            String directory = commandLine.hasArgument() ? commandLine.getArgument() : System.getProperty("user.dir");

            final PlaylistFactory factory = new PlaylistFactory(directory);
            final Playlist playlist = factory.newPlaylist();
            playlist.shuffle();

            final MusicPlayer musicPlayer = new MusicPlayer(playlist);
            musicPlayer.play();

            if (commandLine.hasOption(ArgumentParser.GUI_OPTION)) {
                GuiApplication.start(musicPlayer, "-a");
            } else if (commandLine.hasOption(ArgumentParser.CLIENT_OPTION))  {
                //TODO Client startup
                System.out.println("Client startup");
            } else {
                final Terminal terminal = new Terminal(musicPlayer);
                terminal.listenForInstructions();
            }
        } catch (ExitCodeException e) {
            System.err.println(e.getMessage());
            System.exit(e.getExitCode());
        } catch (ParseException e) {
            argumentParser.printHelp();
        } finally {
            CleanUpDemon.getInstance().cleanup();
        }
    }
}
