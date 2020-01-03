package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.WebClient;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamPlayerFactory {

    private static final String PORT_RANGE = "(6553[0-5]|655[0-2][0-9]|65[0-4][0-9]"
        + "{2}|6[0-4][0-9]{3}|[0-9][0-9]{4}|[0-9]{4}|[0-9]{3}|[0-9]{2}|[0-9])";
    private static final String ADDRESS = "(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])"
        + "\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])|localhost)";
    private static final String PROTOCOL = "((rtp)|(http))://";
    private static final String SPLITTER = ":";

    public RadioModel newStreamPlayer(String serverAddress, String port) throws NoConnectionException, NoValidUrlException {
        final String url = "rtp://" + toAddress(serverAddress) + ":" + port + "/";
        if (isValidURL(url)) {
            final PlayOption playOption = new PlayOption();
            playOption.setOption(url);
            playOption.setFunction(null);

            try {
                final WebClient client = new WebClient(serverAddress, Integer.parseInt(port));
                return new StreamPlayer(playOption, client);
            } catch (IOException | InterruptedException e) {
                throw new NoConnectionException("could not connect to given url " + serverAddress + ":" + port);
            }
        } else {
            throw new NoValidUrlException("could not parse address: " + serverAddress + " and port: " + port);
        }
    }

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
        Pattern pattern = Pattern.compile(PROTOCOL + ADDRESS + ":" + PORT_RANGE + "\\/*");
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

