package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.server.ClientSocket;

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
    void setWebClient(ClientSocket client);

    void updateFromServer(String update);

    void disconnect();
}
