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

public final class WebClient {

    private static final String SPLITTER = ":";
    private static final int OK = 200;

    private final JSONParser parser;
    private final HttpClient client;
    private final String baseUri;

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

    public Song requestSong() {
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri + "/current-song")).build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parser.parseJSON(response.body(), Song.class);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error getting resource");
            return new Song();
        } catch (SerialisationException e) {
            e.printStackTrace();
            return new Song();
        }
    }

    public Playlist requestPlaylist() {
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri + "/playlist")).build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parser.parseJSON(response.body(), Playlist.class);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error getting resource");
            return new Playlist();
        } catch (SerialisationException e) {
            e.printStackTrace();
            return new Playlist();
        }
    }

    public int requestVote(Song song) {
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri + "/votes?id=" + song.getUuid())).build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Integer.parseInt(response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error getting resource");
            return 0;
        }
    }
}
