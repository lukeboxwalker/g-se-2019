package de.techfak.gse.lwalkenhorst.radioplayer.playbehavior;

import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * StreamListeningPlayBehavior to listen to a stream.
 */
public class StreamListeningPlayBehavior implements PlayBehavior {

    private final String rtpUrl;

    public StreamListeningPlayBehavior(final String address, final String port) {
        this.rtpUrl = "rtp://" + address + ":" + port + "/";
    }

    @Override
    public Runnable play(MediaPlayer mediaPlayer, Song song) {
        return () -> mediaPlayer.media().play(rtpUrl);
    }
}
