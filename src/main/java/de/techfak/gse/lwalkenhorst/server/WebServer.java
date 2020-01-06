package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.jsonparser.JSONParser;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.*;

import fi.iki.elonen.NanoHTTPD;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;


/**
 * WebServer to setup Rest Server.
 */
public class WebServer extends NanoHTTPD {

    private static final String LOCALHOST = "127.0.0.1";
    private static final String MIME_TYPE_JSON = "application/json";
    private static final String MIME_TYPE_JPEG = "image/jpeg";
    private static final String ID = "id";

    private final JSONParser parser;
    private MusicPlayer musicPlayer;

    /**
     * Creating a new WebServer.
     *
     * @param port        to listen to
     * @param musicPlayer to stream music
     */
    public WebServer(final String port, MusicPlayer musicPlayer) {
        super(Integer.parseInt(port));
        this.parser = new JSONParser();
        this.musicPlayer = musicPlayer;
    }

    public void setMusicStream(final String port) {
        this.musicPlayer.setPlayBehavior(getPlayBehavior(port));
    }

    /**
     * Creates new PlayBehavior for server usage.
     * Using given port to setup stream play behavior.
     * If port is null or empty using normal behavior
     *
     * @param port the streaming port
     * @return new PlayBehavior
     */
    private IPlayBehavior getPlayBehavior(String port) {
        IPlayBehavior playBehavior = new NormalPlayBehavior();
        if (port != null && !port.isEmpty()) {
            playBehavior = new IPlayBehavior() {
                @Override
                public Runnable play(MediaPlayer mediaPlayer, Song song) {
                    return () -> mediaPlayer.media().play(song.getFilePath(),
                        ":sout=#rtp{dst=" + LOCALHOST + ",port=" + port + ",mux=ts}");
                }
            };
        }
        return playBehavior;
    }

    /**
     * Starting the server.
     *
     * @throws NoConnectionException if server could not start socket.
     */
    public void startTSPSocket() throws NoConnectionException {
        try {
            this.start(SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            throw new NoConnectionException("could not setup server socket on: "
                + LOCALHOST + ":" + getListeningPort(), e);
        }
        CleanUpDemon.getInstance().register(this, this::stop);
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            if (session.getUri().isEmpty()) {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found");
            }
            switch (session.getUri()) {
                case "/current-song":
                    return newFixedLengthResponse(Response.Status.OK,
                        MIME_TYPE_JSON, parser.toJSON(musicPlayer.getSong()));
                case "/playlist":
                    return newFixedLengthResponse(Response.Status.OK,
                        MIME_TYPE_JSON, parser.toJSON(musicPlayer.getPlaylist()));
                case "/votes":
                    if (session.getParameters().size() == 1) {
                        String songUUID = session.getParameters()
                            .getOrDefault(ID, Collections.singletonList("")).get(0);
                        Integer integer = musicPlayer.getVotingManager().getVotes(songUUID);
                        return newFixedLengthResponse(Response.Status.OK, MIME_TYPE_JSON, parser.toJSON(integer));
                    }
                    break;
                case "/cover":
                    if (session.getParameters().size() == 1) {
                        String songUUID = session.getParameters()
                            .getOrDefault(ID, Collections.singletonList("")).get(0);
                        Song song = musicPlayer.getPlaylist().getSongs().stream()
                            .filter(song1 -> song1.getUuid().equals(songUUID)).findFirst().get();
                        try {
                            String url = URI.create(song.getArtWorkURL()).getPath();
                            if (url == null || url.isEmpty()) {
                                return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_TYPE_JSON, "Not found");
                            }
                            File file = new File(url);
                            FileInputStream stream = new FileInputStream(file);
                            return newFixedLengthResponse(Response.Status.OK, MIME_TYPE_JPEG, stream, -1);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_TYPE_JSON, "Not found");
                        }
                    }
                    break;
                default:
            }
        } catch (SerialisationException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "GSE Radio");
    }
}
