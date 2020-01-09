package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.argumentparser.*;
import de.techfak.gse.lwalkenhorst.closeup.ObjectCloseupManager;
import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import de.techfak.gse.lwalkenhorst.exceptions.ParseException;
import de.techfak.gse.lwalkenhorst.radioplayer.*;
import de.techfak.gse.lwalkenhorst.radioview.ClientApplication;
import de.techfak.gse.lwalkenhorst.radioview.GuiApplication;
import de.techfak.gse.lwalkenhorst.radioview.Terminal;

import de.techfak.gse.lwalkenhorst.server.WebServer;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The GSERadio program is a music radio.
 *
 * @author Lukas Walkenhorst
 */
public final class GSERadio {

    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String SERVER = "server";
    private static final String GUI = "gui";
    private static final String CLIENT = "client";
    private static final String STREAMING = "streaming";
    private static final String PORT = "port";
    private static final String DEFAULT_PORT = "8080";
    private static final String SEPARATE = "=";
    private static final Pattern NUMBER = Pattern.compile("([0-9]*)");


    private GSERadio() {
    }

    /**
     * Starting the GSERadio application.
     * Parsing Arguments with {@link ArgumentParser} to determine what the application is used for
     * Program can start as Server, as Client or as local music application
     * Arguments:
     * --server [--streaming=<port>] [--port<port>] to specify server mode
     * --client to start in client mode
     * --gui to start with gui otherwise starts in default cli mode
     *
     * @param args the program was started with
     */
    private void start(final String... args) {
        try {
            final CommandLine commandLine = new ArgumentParser().parse(createOptions(), args);
            final String directory = commandLine.hasArgument() ? commandLine.getArgument() : USER_DIR;

            if (commandLine.hasOption(CLIENT)) { //Client
                final StreamMusicPlayer streamPlayer = new StreamMusicPlayer();
                ClientApplication.start(streamPlayer);
            } else {
                final MusicPlayer musicPlayer = load(directory);
                if (commandLine.hasOption(SERVER)) { //Server
                    String streaming = commandLine.getOptionArg(SERVER, STREAMING);
                    String port = commandLine.getOptionArg(SERVER, PORT);

                    try {
                        if (streaming != null && !streaming.isEmpty()) {
                            new WebServer(Integer.parseInt(port), musicPlayer, Integer.parseInt(streaming));
                        } else {
                            new WebServer(Integer.parseInt(port), musicPlayer);
                        }
                    } catch (NumberFormatException e) {
                        throw new ParseException("port argument was expected to be an integer");
                    }
                }
                if (commandLine.hasOption(GUI)) { //Local GUI
                    GuiApplication.start(musicPlayer, "-a");
                } else { //Local Terminal
                    final Terminal terminal = new Terminal(musicPlayer);
                    terminal.listenForInstructions();
                }
            }
        } catch (ExitCodeException e) {
            //Handling exceptions that will exit the program with specified exit code.
            System.err.println(e.getMessage());
            System.exit(e.getExitCode());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } finally {
            ObjectCloseupManager.getInstance().closeReferences();
        }
    }

    /**
     * Creating commandLine options.
     * Building new option arguments to parse arguments with.
     *
     * @return list of defined options
     */
    private List<Option> createOptions() {
        Option guiOption = CommandLineOption.builder().withName(GUI).conflictsOptions(CLIENT, SERVER).build();
        Option clientOption = CommandLineOption.builder().withName(CLIENT).conflictsOptions(GUI, SERVER).build();

        Argument streaming = CommandLineArgument.builder().withName(STREAMING).withValueSeparator(SEPARATE)
                .withPatternMatcher(NUMBER).isRequired(false).build();
        Argument port = CommandLineArgument.builder().withName(PORT).withValueSeparator(SEPARATE)
                .withPatternMatcher(NUMBER).isRequired(true).withDefaultValue(DEFAULT_PORT).build();

        Option serverOption = CommandLineOption.builder().withName(SERVER).withArgument(streaming).withArgument(port)
                .conflictsOptions(CLIENT, GUI).build();

        return Arrays.asList(guiOption, clientOption, serverOption);
    }

    /**
     * Loading MusicPlayer with given music form directory.
     *
     * @param directory with mp3 files
     * @return musicPlayer with loaded playlist
     * @throws ExitCodeException when Playlist is empty or directory contains no mp3 files
     */
    private MusicPlayer load(String directory) throws ExitCodeException {
        final PlaylistFactory factory = new PlaylistFactory(directory);
        final Playlist playlist = factory.newPlaylist(true);
        playlist.shuffle();

        final MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.setPlaylist(playlist);
        return musicPlayer;
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
