package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.server.WebClient;

/**
 * StreamPlayer that uses WebClient.
 * WebClient is needed for sending requests
 * to server
 */
public interface StreamPlayer extends RadioPlayer {

    /**
     * Setting client that will be used.
     * @param client to sent request
     */
    void useWebClient(WebClient client);
}
