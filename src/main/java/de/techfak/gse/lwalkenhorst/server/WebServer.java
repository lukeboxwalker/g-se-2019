package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.closeup.ObjectCloseupManager;
import de.techfak.gse.lwalkenhorst.exceptions.NoConnectionException;
import de.techfak.gse.lwalkenhorst.exceptions.StreamFailedException;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.*;
import de.techfak.gse.lwalkenhorst.radioplayer.playbehavior.StreamingPlayBehavior;
import fi.iki.elonen.NanoWSD;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;

/**
 * WebServer to setup Rest Server.
 */
public class WebServer extends NanoWSD implements PropertyChangeListener {

    private static final String LOCALHOST = "127.0.0.1";

    private final ServerResponseFactory factory;
    private final List<ServerSocket> sockets = new ArrayList<>();
    private final MusicPlayer musicPlayer;

    /**
     * Creating a new WebServer.
     *
     * @param port        to listen to
     * @param musicPlayer to stream music
     */
    public WebServer(final int port, final MusicPlayer musicPlayer) throws NoConnectionException {
        super(port);
        this.musicPlayer = musicPlayer;
        this.musicPlayer.addPropertyChangeListener(this);
        this.factory = new ServerResponseFactory(musicPlayer);
        try {
            this.start(0, false);
            ObjectCloseupManager.getInstance().register(this, () -> {
                sockets.forEach(socket -> {
                    try {
                        socket.close(WebSocketFrame.CloseCode.GoingAway, "closed", false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                stop();
            });
        } catch (IOException e) {
            throw new NoConnectionException("could not setup server socket on: " + getHostname() + ":" + port, e);
        }
    }

    public void streamMusic(final int streamPort) throws StreamFailedException {
        this.musicPlayer.setPlayBehavior(new StreamingPlayBehavior(LOCALHOST, streamPort));
    }

    @Override
    protected WebSocket openWebSocket(final IHTTPSession ihttpSession) {
        return new ServerSocket(ihttpSession, sockets);
    }

    @Override
    public Response serve(final IHTTPSession session) {
        try {
            //In case of a websocket request just use super
            if (isWebsocketRequested(session)) {
                return super.serve(session);
            } else if (session.getMethod() == Method.GET) { //Case of normal http requests
                switch (session.getUri()) {
                    case "/current-song":
                        return factory.newSongResponse();
                    case "/current-time":
                        return factory.newTimeResponse();
                    case "/playlist":
                        return factory.newPlaylistResponse();
                    case "/votes":
                        return factory.newVotesResponse(session);
                    case "/cover":
                        return factory.newCoverResponse(session);
                    default:
                }
            } else if (session.getMethod() == Method.POST) {
                if (session.getUri().startsWith("/song")) {
                    String[] uri = session.getUri().split("/");
                    final int uriLength = 4;
                    final int uuidIndex = 2;
                    if (uri.length == uriLength) {
                        musicPlayer.vote(uri[uuidIndex]);
                    }
                    return factory.newPlainResponse("");
                }
            }
        } catch (SerialisationException e) {
            e.printStackTrace();
        }
        return factory.newPlainResponse("GSE Radio");
    }

    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {
        try {
            final String propertyName = propertyChangeEvent.getPropertyName();
            for (ServerSocket socket : sockets) {
                socket.send(propertyName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
