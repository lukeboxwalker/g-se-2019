package de.techfak.gse.lwalkenhorst.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parsing URL.
 */
public class UrlParser {

    private static final String PORT_RANGE = ""
        + "(6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[0-9][0-9]{4}|[0-9]{4}|[0-9]{3}|[0-9]{2}|[0-9])";
    private static final String ADDRESS = ""
        + "(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])"
        + "|localhost)";
    private static final String PROTOCOL = "((rtp)|(http))://";
    private static final String SPLITTER = ":";

    public boolean isValidURL(String url) {
        Pattern pattern = Pattern.compile(PROTOCOL + ADDRESS + SPLITTER + PORT_RANGE + "\\/*");
        return pattern.matcher(url).matches();
    }

    public boolean isValidPort(String port) {
        return Pattern.compile(PORT_RANGE).matcher(port).matches();
    }

    public boolean isValidServerAddress(String serverAddress) {
        return Pattern.compile(ADDRESS).matcher(serverAddress).matches();
    }

    /**
     * Extracting port form url.
     * Using Regex Pattern to do so
     * @param url to extract port form
     * @return port
     */
    public String extractPort(String url) {
        String[] splitURL = url.split(SPLITTER);
        Matcher matcher = Pattern.compile(PORT_RANGE).matcher(splitURL[2]);
        boolean matches = matcher.find();
        return matches ? matcher.group() : "";
    }

    /**
     * Extracting server address form url.
     * Using Regex Pattern to do so
     * @param url to extract server address form
     * @return server address
     */
    public String extractServerAddress(String url) {
        String[] splitURL = url.split(SPLITTER);
        return toAddress(splitURL[1].substring(2));
    }

    /**
     * Convert server Address to ip.
     * @param address name
     * @return ip address to connect to
     */
    public String toAddress(String address) {
        if (address.equals("localhost")) {
            return "127.0.0.1";
        } else {
            return address;
        }
    }
}
