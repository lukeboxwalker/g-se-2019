package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.jsonparser.JSONParser;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServerResponseFactory {

    private static final NanoHTTPD.Response.Status OK = NanoHTTPD.Response.Status.OK;
    private static final NanoHTTPD.Response.Status NOT_FOUND = NanoHTTPD.Response.Status.NOT_FOUND;
    private static final String JSON = "application/json";
    private static final String JPEG = "image/jpeg";
    private static final String PLAINTEXT = "text/plain";
    private static final String ID = "id";

    private final JSONParser parser;
    private final RadioPlayer radio;

    public ServerResponseFactory(final RadioPlayer radio) {
        this.parser = new JSONParser();
        this.radio = radio;
    }

    public NanoHTTPD.Response newTimeResponse() {
        return NanoHTTPD.newFixedLengthResponse(OK, JSON, String.valueOf(radio.getCurrentPlayTime()));
    }

    public NanoHTTPD.Response newSongResponse() throws SerialisationException {
        final String jsonSong = parser.toJSON(radio.getSong());
        return NanoHTTPD.newFixedLengthResponse(OK, JSON, jsonSong);
    }

    public NanoHTTPD.Response newPlaylistResponse() throws SerialisationException {
        final String jsonPlaylist =  parser.toJSON(radio.getPlaylist());
        return NanoHTTPD.newFixedLengthResponse(OK, JSON, jsonPlaylist);
    }

    public NanoHTTPD.Response newVotesResponse(final NanoHTTPD.IHTTPSession session) {
        if (session.getParameters().size() == 1) {
            final Integer votes = radio.getVotes(getUUID(session));
            return NanoHTTPD.newFixedLengthResponse(OK, JSON, String.valueOf(votes));
        }
        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, JSON, "");
    }

    public NanoHTTPD.Response newCoverResponse(final NanoHTTPD.IHTTPSession session) {
        if (session.getParameters().size() == 1) {
            final String uuid = getUUID(session);
            for (Song song : radio.getPlaylist().getSongs()) {
                if (song.getUuid().equals(uuid)) {
                    try {
                        final String url = URI.create(song.getArtWorkURL()).getPath();
                        if (url == null || url.isEmpty()) {
                            return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, JSON, "");
                        }
                        final File file = new File(url);
                        final FileInputStream stream = new FileInputStream(file);
                        return NanoHTTPD.newFixedLengthResponse(OK, JPEG, stream, -1);
                    } catch (FileNotFoundException e) {
                        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, JSON, "");
                    }
                }
            }
        }
        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, JSON, "");
    }

    private String getUUID(final NanoHTTPD.IHTTPSession session) {
        final Map<String, List<String>> parameters = session.getParameters();
        return parameters.getOrDefault(ID, Collections.singletonList("")).get(0);
    }

    public NanoHTTPD.Response newPlainResponse(String text) {
        return NanoHTTPD.newFixedLengthResponse(OK, PLAINTEXT, text);
    }
}
