package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.jsonparser.JSONParser;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.*;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.Collections;

public class WebServer extends NanoHTTPD {

    private static final String LOCALHOST = "127.0.0.1";
    private static final String MIME_TYPE = "application/json";

    private final JSONParser parser;
    private MusicPlayer musicPlayer;

    public WebServer(final String port) {
        super(Integer.parseInt(port));
        this.parser = new JSONParser();
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public IPlayAble getPlayAble(String port) {
        final PlayOption playOption = new PlayOption();
        if (port != null && !port.isEmpty()) {
            playOption.setOption(":sout=#rtp{dst=" + LOCALHOST + ",port=" + port + ",mux=ts}");
        }
        return playOption;
    }

    public void startTSPSocket() throws NoConnectionException {
        try {
            this.start(SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            throw new NoConnectionException("could not setup server socket on: " + LOCALHOST + ":" + getListeningPort(), e);
        }
        CleanUpDemon.getInstance().register(this, this::stop);
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            switch (session.getUri()) {
                case "/current-song":
                    return newFixedLengthResponse(Response.Status.OK, MIME_TYPE, parser.toJSON(musicPlayer.getSong()));
                case "/playlist":
                    return newFixedLengthResponse(Response.Status.OK, MIME_TYPE, parser.toJSON(musicPlayer.getPlaylist()));
                case "/votes":
                    if (session.getParameters().size() == 1) {
                        String songUUID = session.getParameters().getOrDefault("id", Collections.singletonList("")).get(0);
                        Integer integer = musicPlayer.getVotingManager().getVotes(songUUID);
                        return newFixedLengthResponse(Response.Status.OK, MIME_TYPE, parser.toJSON(integer));
                    }
            }
        } catch (SerialisationException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "GSE Radio");
    }

    public RadioModel getRadio() {
        return musicPlayer;
    }
}
