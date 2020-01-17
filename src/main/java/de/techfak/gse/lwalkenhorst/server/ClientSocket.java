package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.closeup.ObjectCloseupManager;
import de.techfak.gse.lwalkenhorst.exceptions.NoConnectionException;
import de.techfak.gse.lwalkenhorst.jsonparser.JSONParser;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import de.techfak.gse.lwalkenhorst.radioplayer.StreamPlayer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * WebClient that communicates with server by requests.
 */
public class ClientSocket extends WebSocketClient {

    private static final int OK = 200;

    private final JSONParser parser;
    private final HttpClient client;
    private final String baseUri;
    private final StreamPlayer streamPlayer;

    /**
     * Creates a new Webclient.
     * Checks connection to server.
     *
     * @param serverAddress of the server
     * @param port of the server
     * @throws NoConnectionException if could not connect to given server
     */
    public ClientSocket(String serverAddress, int port, StreamPlayer streamPlayer) throws NoConnectionException {
        super(URI.create("ws://"+ serverAddress +":" + port));
        this.client = HttpClient.newHttpClient();
        this.streamPlayer = streamPlayer;
        this.streamPlayer.setWebClient(this);
        this.parser = new JSONParser();
        this.baseUri = "http://" + uri.getAuthority();
        final String message = "could not connect to given url " + baseUri;
        try {
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri)).GET().build();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != OK || !response.body().equals("GSE Radio")) {
                throw new NoConnectionException(message);
            }
        } catch (IOException | InterruptedException e) {
            throw new NoConnectionException(message, e);
        }
        ObjectCloseupManager.getInstance().register(this, this::close);
    }

    /**
     * Requests a Song form connected server.
     * Parses responded json to song object.
     * If parsing or connection fails returns an empty song.
     *
     * @return new song object.
     */
    public Song requestSong() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri + "/current-song")).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Song song = parser.parseJSON(response.body(), Song.class);
            song.setArtWorkURL(URI.create(baseUri + "/cover?id=" + song.getUuid()).toString());
            return song;
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
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri + "/playlist")).GET().build();
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
     * @param uuid to get votes from
     * @return votes of a given song
     */
    public int requestVote(String uuid) {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUri + "/votes?id=" + uuid)).GET().build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Integer.parseInt(response.body());
        } catch (IOException | InterruptedException e) {
            return 0;
        }
    }

    /**
     * Using the uuid of a song to vote for song.
     *
     * @param uuid to vote for
     */
    public void vote(String uuid) {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/vote" + uuid)).POST(HttpRequest.BodyPublishers.ofString(uuid)).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String update) {
        streamPlayer.updateFromServer(update);
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
