package de.techfak.gse.lwalkenhorst.radioplayer.playbehavior;

import de.techfak.gse.lwalkenhorst.exceptions.StreamFailedException;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * StreamingPlayBehavior when playing as a stream.
 */
public class StreamingPlayBehavior implements PlayBehavior {

    private static final int LOWEST_PORT = 1024;
    private static final int HIGHEST_PORT = 49151;
    private final String destination;
    private final int port;

    /**
     * Creating a Stream PlayBehavior.
     *
     * @param destination address to stream to
     * @param port        to listen to
     * @throws StreamFailedException if port is not valid
     */
    public StreamingPlayBehavior(final String destination, final int port) throws StreamFailedException {
        if (port < LOWEST_PORT || port > HIGHEST_PORT) {
            throw new StreamFailedException("could not start streaming with vlc media player because of port: " + port);
        }
        this.destination = destination;
        this.port = port;
    }

    @Override
    public Runnable play(final MediaPlayer mediaPlayer, final Song song) {
        return () -> mediaPlayer.media().play(song.getFilePath(),
                ":sout=#rtp{dst=" + destination + ",port=" + port + ",mux=ts}");
    }
}
