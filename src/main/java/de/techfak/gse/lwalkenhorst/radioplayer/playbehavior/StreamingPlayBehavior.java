package de.techfak.gse.lwalkenhorst.radioplayer.playbehavior;

import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * StreamingPlayBehavior when playing as a stream.
 */
public class StreamingPlayBehavior implements PlayBehavior {

    private final String destination;
    private final String port;

    public StreamingPlayBehavior(final String destination, final String port) {
        this.destination = destination;
        this.port = port;
    }

    @Override
    public Runnable play(final MediaPlayer mediaPlayer, final Song song) {
        return () -> mediaPlayer.media().play(song.getFilePath(),
            ":sout=#rtp{dst=" + destination + ",port=" + port + ",mux=ts}");
    }
}
