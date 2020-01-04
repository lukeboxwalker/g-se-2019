package de.techfak.gse.lwalkenhorst.server;


import de.techfak.gse.lwalkenhorst.jsonparser.JSONParser;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * WebClient that communicates with server by requests.
 */
public class WebClient {

    private static final String SPLITTER = ":";
    private static final int OK = 200;

    private final JSONParser parser;
    private final HttpClient client;
    private final String baseUri;

    /**
     * Creates a new Webclient.
     * Checks connection to server.
     *
     * @param serverAddress of the server
     * @param port of the server
     * @throws NoConnectionException if could not connect to given server
     */
    public WebClient(String serverAddress, int port) throws NoConnectionException {
        this.client = HttpClient.newHttpClient();
        this.parser = new JSONParser();
        this.baseUri = "http://" + serverAddress + SPLITTER + port;
        final String message = "could not connect to given url " + serverAddress + SPLITTER + port;
        HttpResponse<String> response;
        try {
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri)).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new NoConnectionException(message, e);
        }
        if (response.statusCode() != OK || !response.body().equals("GSE Radio")) {
            throw new NoConnectionException(message);
        }
    }

    /**
     * Requests a Song form connected server.
     * Parses responded json to song object.
     * If parsing or connection fails returns an empty song.
     *
     * @return new song object.
     */
    public Song requestSong() {
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri + "/current-song")).build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parser.parseJSON(response.body(), Song.class);
        } catch (IOException | InterruptedException e) {
            return new Song();
        } catch (SerialisationException e) {
            e.printStackTrace();
            return new Song();
        }
    }

    /**
     * Requests a Playlist from connected server.
     * Parses responded json to playlist object.
     * If parsing or connection fails returns an empty playlist.
     *
     * @return new Playlist object
     */
    public Playlist requestPlaylist() {
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri + "/playlist")).build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parser.parseJSON(response.body(), Playlist.class);
        } catch (IOException | InterruptedException e) {
            return new Playlist();
        } catch (SerialisationException e) {
            e.printStackTrace();
            return new Playlist();
        }
    }

    /**
     * Requests the votes for a song.
     * Using the uuid of a song to send server request
     * returns 0 if connection fails.
     *
     * @param song to get votes from
     * @return votes of a given song
     */
    public int requestVote(Song song) {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUri + "/votes?id=" + song.getUuid())).build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Integer.parseInt(response.body());
        } catch (IOException | InterruptedException e) {
            return 0;
        }
    }
}
