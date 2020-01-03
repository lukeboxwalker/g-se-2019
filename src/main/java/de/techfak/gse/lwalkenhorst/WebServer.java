package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.jsonparser.JSONParser;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.PlayOption;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;

import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.Collections;

public class WebServer extends NanoHTTPD {

    private static final String LOCALHOST = "127.0.0.1";

    private final int port;
    private final JSONParser parser;
    private MusicPlayer musicPlayer;

    public WebServer(final int port) {
        super(port);
        this.port = port;
        this.parser = new JSONParser();
    }

    public void start(Playlist playlist) throws IOException {
        final PlayOption playOption = new PlayOption();
        playOption.setOption(":sout=#rtp{dst=" + LOCALHOST + ",port=" + port + ",mux=ts}");

        musicPlayer = new MusicPlayer(playOption);
        musicPlayer.setPlaylist(playlist);

        this.start(SOCKET_READ_TIMEOUT, false);
        CleanUpDemon.getInstance().register(this, this::stop);
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            switch (session.getUri()) {
                case "/current-song":
                    return newFixedLengthResponse(Response.Status.OK, "application/json", parser.toJSON(musicPlayer.getSong()));
                case "/playlist":
                    return newFixedLengthResponse(Response.Status.OK, "application/json", parser.toJSON(musicPlayer.getPlaylist()));
                case "/votes":
                    if (session.getParameters().size() == 1) {
                        String songUUID = session.getParameters().getOrDefault("id", Collections.singletonList("")).get(0);
                        Integer integer = musicPlayer.getVotingManager().getVotes(songUUID);
                        return newFixedLengthResponse(Response.Status.OK, "application/json", parser.toJSON(integer));
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
