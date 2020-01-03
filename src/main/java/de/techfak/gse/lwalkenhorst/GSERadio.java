package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.argumentparser.ArgumentParser;
import de.techfak.gse.lwalkenhorst.argumentparser.ICommandLine;
import de.techfak.gse.lwalkenhorst.argumentparser.OptionAdapter;
import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import de.techfak.gse.lwalkenhorst.radioplayer.*;
import de.techfak.gse.lwalkenhorst.radioview.ClientApplication;
import de.techfak.gse.lwalkenhorst.radioview.GuiApplication;
import de.techfak.gse.lwalkenhorst.radioview.Terminal;

import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * The GSERadio program is a music radio.
 *
 * @author Lukas Walkenhorst
 */
public final class GSERadio {

    private static final String USER_DIR =  System.getProperty("user.dir");
    private static final String SERVER = "server";
    private static final String GUI = "gui";
    private static final String CLIENT = "client";
    private static final String PORT_RANGE = ""
        + "(6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[0-9][0-9]{4}|[0-9]{4}|[0-9]{3}|[0-9]{2}|[0-9])";


    private GSERadio() {
    }

    private void start(final String... args) {
        final ArgumentParser argumentParser = newArgumentParser();
        try {
            final ICommandLine commandLine = argumentParser.parse(args);
            final String directory = commandLine.hasArgument() ? commandLine.getArgument() : USER_DIR;

            if (commandLine.hasOption(SERVER)) { //Server
                String port = commandLine.getParsedOptionArg(SERVER);

                //Starting server on given port
                WebServer server = new WebServer(Integer.parseInt(port));
                server.start(createPlaylist(directory));

                //Using terminal to interact with music player
                GuiApplication.start(server.getRadio(), "-a");

            } else if (commandLine.hasOption(GUI)) { //Local GUI
                //Starting in gui mode to interact with music player
                final MusicPlayer radio = load(directory);
                GuiApplication.start(radio, "-a");
            } else if (commandLine.hasOption(CLIENT))  { //Client

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

    private ArgumentParser newArgumentParser() {
        OptionAdapter guiOption = new OptionAdapter(GUI, "Starting in gui mode.");
        OptionAdapter clientOption = new OptionAdapter(CLIENT, "Starting in client mode.");

        guiOption.addConflictingOption(clientOption);
        clientOption.addConflictingOption(guiOption);

        String description = "Starting in server mode with given argument --streaming=<port> to specify port.";
        OptionAdapter serverOption = new OptionAdapter(SERVER, true, description);
        serverOption.setParsingPattern(Pattern.compile("--streaming=" + PORT_RANGE));
        serverOption.setExtractingPatternPattern(Pattern.compile(PORT_RANGE));

        return new ArgumentParser(guiOption, clientOption, serverOption);
    }

    private Playlist createPlaylist(String directory) throws ExitCodeException {
        final PlaylistFactory factory = new PlaylistFactory(directory);
        final Playlist playlist = factory.newPlaylist();
        playlist.shuffle();
        return playlist;
    }

    private MusicPlayer load(Playlist playlist, IPlayAble playAble) {
        final MusicPlayer musicPlayer = new MusicPlayer(playAble);
        musicPlayer.setPlaylist(playlist);
        return musicPlayer;
    }

    private MusicPlayer load(String directory) throws ExitCodeException {
        return load(createPlaylist(directory), new PlayOption());
    }

    /**
     * Starts the program with given arguments.
     * Can start with given directory to read mp3 files from.
     * Can start with -g, --gui to start gui mode
     *
     * @param args the parameters the program is started with.
     */
    public static void main(final String... args) {
        GSERadio radio = new GSERadio();
        radio.start(args);
    }


}
