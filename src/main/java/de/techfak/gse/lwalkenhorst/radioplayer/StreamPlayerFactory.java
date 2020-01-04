package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.server.NoConnectionException;
import de.techfak.gse.lwalkenhorst.server.NoValidUrlException;
import de.techfak.gse.lwalkenhorst.server.WebClient;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory to create ne StreamPlayer objects.
 */
public class StreamPlayerFactory {

    private static final String PORT_RANGE = ""
        + "(6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[0-9][0-9]{4}|[0-9]{4}|[0-9]{3}|[0-9]{2}|[0-9])";
    private static final String ADDRESS = ""
        + "(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])"
        + "|localhost)";
    private static final String PROTOCOL = "((rtp)|(http))://";
    private static final String SPLITTER = ":";

    /**
     * Creates a new StreamPlayer.
     * Checking WebClient connection to init streamPlayer.
     * Inits new PlayBehavior to listen to vlcj stream.
     * Parses given serverAddress and port to validate it.
     *
     * @param serverAddress of the server
     * @param port of the server
     * @return new RadioModel (StreamPlayer) to play music from
     * @throws NoConnectionException if could not connect to server
     * @throws NoValidUrlException if given url is invalid
     */
    public RadioModel newStreamPlayer(String serverAddress,
                                      String port) throws NoConnectionException, NoValidUrlException {
        final String url = "rtp://" + toAddress(serverAddress) + SPLITTER + port + "/";
        if (isValidURL(url)) {
            final WebClient client = new WebClient(serverAddress, Integer.parseInt(port));
            StreamPlayer streamPlayer = new StreamPlayer(client);
            streamPlayer.setPlayBehavior(new IPlayBehavior() {
                @Override
                public Runnable play(MediaPlayer mediaPlayer, Song song) {
                    return () -> mediaPlayer.media().play(url);
                }
            });
            return streamPlayer;
        } else {
            throw new NoValidUrlException("could not parse address: " + serverAddress + " and port: " + port);
        }
    }

    /**
     * Creates a new StreamPlayer.
     * Parses url to validate it.
     * Splits url in serverAddress and port to call {@link #newStreamPlayer(String, String)}
     *
     * @param url the server url
     * @return new RadioModel (StreamPlayer) to play music from
     * @throws NoConnectionException if could not connect to server
     * @throws NoValidUrlException if given url is invalid
     */
    public RadioModel newStreamPlayer(String url) throws NoConnectionException, NoValidUrlException {
        if (isValidURL(url)) {
            final String port = extractPort(url);
            final String serverAddress = extractServerAddress(url);
            return newStreamPlayer(serverAddress, port);
        } else {
            throw new NoValidUrlException("could not parse url: " + url);
        }
    }

    private boolean isValidURL(String url) {
        Pattern pattern = Pattern.compile(PROTOCOL + ADDRESS + SPLITTER + PORT_RANGE + "\\/*");
        return pattern.matcher(url).matches();
    }

    private String extractPort(String url) {
        String[] splitURL = url.split(SPLITTER);
        Matcher matcher = Pattern.compile(PORT_RANGE).matcher(splitURL[2]);
        boolean matches = matcher.find();
        return matches ? matcher.group() : "";
    }

    private String extractServerAddress(String url) {
        String[] splitURL = url.split(SPLITTER);
        return toAddress(splitURL[1].substring(2));
    }

    private String toAddress(String address) {
        if (address.equals("localhost")) {
            return "127.0.0.1";
        } else {
            return address;
        }
    }
}

