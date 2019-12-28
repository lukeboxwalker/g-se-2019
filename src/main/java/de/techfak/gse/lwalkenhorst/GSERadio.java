package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.argumentparser.ArgumentParser;
import de.techfak.gse.lwalkenhorst.argumentparser.ICommandLine;
import de.techfak.gse.lwalkenhorst.argumentparser.IOption;
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

    private static final String USER_DIR =  System.getProperty("user.dir");

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
            final String directory = commandLine.hasArgument() ? commandLine.getArgument() : USER_DIR;

            if (commandLine.hasOption(argumentParser.getServerOption())) {
                String port = commandLine.getParsedOptionArg(argumentParser.getServerOption());
                //TODO Server startup
                System.out.println("Server startup with port: " + port);
            } else if (commandLine.hasOption(argumentParser.getGuiOption())) {
                GuiApplication.start(start(directory), "-a");
            } else if (commandLine.hasOption(argumentParser.getClientOption()))  {
                //TODO Client startup
                System.out.println("Client startup");
            } else {
                final Terminal terminal = new Terminal(start(directory));
                terminal.listenForInstructions();
            }
        } catch (ExitCodeException e) {
            //Handling exceptions that will exit the program with specified exit code.
            System.err.println(e.getMessage());
            System.exit(e.getExitCode());
        } catch (ParseException e) {
            //Printing help message when argument parsing failed because of wrong arguments.
            argumentParser.printHelp();
        } finally {
            CleanUpDemon.getInstance().cleanup();
        }
    }

    public static MusicPlayer start(String directory) throws NoMusicFileFoundException {
        final PlaylistFactory factory = new PlaylistFactory(directory);
        final Playlist playlist = factory.newPlaylist();
        playlist.shuffle();

        final MusicPlayer musicPlayer = new MusicPlayer(playlist);
        musicPlayer.play();
        return musicPlayer;
    }
}
