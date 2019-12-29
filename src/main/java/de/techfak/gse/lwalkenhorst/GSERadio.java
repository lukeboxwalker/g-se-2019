package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.argumentparser.ArgumentParser;
import de.techfak.gse.lwalkenhorst.argumentparser.ICommandLine;
import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import de.techfak.gse.lwalkenhorst.radioplayer.*;
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
    private static final String LOCALHOST = "127.0.0.1";

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

                final PlayOption playOption = new PlayOption();
                playOption.setOption(":sout=#rtp{dst=" + LOCALHOST + ",port=" + port + ",mux=ts}");
                final MusicPlayer radio = load(directory, playOption);

                WebServer server = new WebServer(Integer.parseInt(port), radio);
                GuiApplication.start(radio, "-a");

            } else if (commandLine.hasOption(argumentParser.getGuiOption())) {
                final MusicPlayer radio = load(directory);
                GuiApplication.start(radio, "-a");
            } else if (commandLine.hasOption(argumentParser.getClientOption()))  {

                final PlayOption playOption = new PlayOption();
                playOption.setOption("rtp://127.0.0.1:9000/");
                playOption.setFunction(null);

                final MusicPlayer radio = new MusicPlayer(playOption);
                final WebClient client = new WebClient();

                GuiApplication.start(radio, "-a");


                System.out.println("Client startup");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
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

    public static MusicPlayer load(String directory, IPlayAble playAble) throws ExitCodeException {
        final PlaylistFactory factory = new PlaylistFactory(directory);
        final Playlist playlist = factory.newPlaylist();
        playlist.shuffle();

        final MusicPlayer musicPlayer = new MusicPlayer(playAble);
        musicPlayer.setPlaylist(playlist);
        return musicPlayer;
    }

    public static MusicPlayer load(String directory) throws ExitCodeException {
        return load(directory, new PlayOption());
    }
}
