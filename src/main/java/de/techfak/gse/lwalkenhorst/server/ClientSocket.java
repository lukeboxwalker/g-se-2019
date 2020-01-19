package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.closeup.ObjectCloseupManager;
import de.techfak.gse.lwalkenhorst.jsonparser.JSONParser;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import de.techfak.gse.lwalkenhorst.radioplayer.StreamPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.VLCJMediaPlayer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Timer;
import java.util.TimerTask;

/**
 * WebClient that communicates with server by requests.
 */
public class ClientSocket extends WebSocketClient {

    private static final int OK = 200;

    private final JSONParser parser;
    private final HttpClient client;
    private final String baseUri;
    private final StreamPlayer streamPlayer;
    private boolean closed;
    private boolean disconnected;

    /**
     * Creates a new Webclient.
     * Checks connection to server.
     *
     * @param serverAddress of the server
     * @param port          of the server
     */
    public ClientSocket(String serverAddress, int port, StreamPlayer streamPlayer) {
        super(URI.create("ws://" + serverAddress + ":" + port));
        this.client = HttpClient.newHttpClient();
        this.streamPlayer = streamPlayer;
        this.streamPlayer.setWebClient(this);
        this.parser = new JSONParser();
        this.baseUri = "http://" + uri.getAuthority();
        ObjectCloseupManager.getInstance().register(this, this::close);
    }

    public void disconnect() {
        this.disconnected = true;
        ObjectCloseupManager.getInstance().closeObject(this);
    }

    public boolean canConnect() {
        try {
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri)).GET().build();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return (response.statusCode() == OK && response.body().equals("GSE Radio"));
        } catch (IOException | InterruptedException e) {
            return false;
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
        if (closed) return new Song();
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
        if (closed) return new Playlist();
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
        if (closed) return 0;
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/votes?id=" + uuid)).GET().build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Integer.parseInt(response.body());
        } catch (IOException | InterruptedException | NumberFormatException e) {
            return 0;
        }
    }

    public float requestCurrentTime() {
        if (closed) return 0f;
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUri + "/current-time")).GET().build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Float.parseFloat(response.body());
        } catch (IOException | InterruptedException | NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * Using the uuid of a song to vote for song.
     *
     * @param uuid to vote for
     */
    public void vote(String uuid) {
        if (closed) return;
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/vote")).POST(HttpRequest.BodyPublishers.ofString(uuid)).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        this.closed = false;
        this.disconnected = false;
    }

    @Override
    public void onMessage(String update) {
        streamPlayer.updateFromServer(update);
    }

    @Override
    public void onClose(int closeCode, String s, boolean b) {
        this.closed = true;
        streamPlayer.updateFromServer(VLCJMediaPlayer.SONG_UPDATE);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public Runnable reconnectSchedule(int period) {
        return () -> {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!closed || disconnected) {
                        ObjectCloseupManager.getInstance().closeObject(timer);
                    } else if (canConnect()) {
                        reconnect();
                        ObjectCloseupManager.getInstance().closeObject(timer);
                    }

                }
            };
            ObjectCloseupManager.getInstance().register(timer, timer::cancel);
            timer.schedule(timerTask, 0, period);
        };
    }
}
