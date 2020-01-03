package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.argumentparser.ArgumentParser;
import de.techfak.gse.lwalkenhorst.argumentparser.ICommandLine;
import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import de.techfak.gse.lwalkenhorst.radioplayer.*;
import de.techfak.gse.lwalkenhorst.radioview.ClientApplication;
import de.techfak.gse.lwalkenhorst.radioview.GuiApplication;
import de.techfak.gse.lwalkenhorst.radioview.Terminal;

import org.apache.commons.cli.ParseException;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.io.IOException;

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

            if (commandLine.hasOption(argumentParser.getServerOption())) { //Server
                String port = commandLine.getParsedOptionArg(argumentParser.getServerOption());

                //Starting server on given port
                WebServer server = new WebServer(Integer.parseInt(port));
                server.start(createPlaylist(directory));

                //Using terminal to interact with music player
                GuiApplication.start(server.getRadio(), "-a");

            } else if (commandLine.hasOption(argumentParser.getGuiOption())) { //Local GUI
                //Starting in gui mode to interact with music player
                final MusicPlayer radio = load(directory);
                GuiApplication.start(radio, "-a");
            } else if (commandLine.hasOption(argumentParser.getClientOption()))  { //Client

                ClientApplication.main(args);

            } else { //Local Terminal
                //Using terminal to interact with music player
                final MusicPlayer radio = load(directory);
                final Terminal terminal = new Terminal(radio);
                terminal.listenForInstructions();
            }
        } catch (ExitCodeException e) {
            //Handling exceptions that will exit the program with specified exit code.
            System.err.println(e.getMessage());
            System.exit(e.getExitCode());
        } catch (ParseException e) {
            //Printing help message when argument parsing failed because of wrong arguments.
            argumentParser.printHelp();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CleanUpDemon.getInstance().cleanup();
        }
    }

    public static Playlist createPlaylist(String directory) throws ExitCodeException {
        final PlaylistFactory factory = new PlaylistFactory(directory);
        final Playlist playlist = factory.newPlaylist();
        playlist.shuffle();
        return playlist;
    }

    public static MusicPlayer load(Playlist playlist, IPlayAble playAble) {
        final MusicPlayer musicPlayer = new MusicPlayer(playAble);
        musicPlayer.setPlaylist(playlist);
        return musicPlayer;
    }

    public static MusicPlayer load(String directory) throws ExitCodeException {
        return load(createPlaylist(directory), new PlayOption());
    }
}
