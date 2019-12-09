package de.techfak.gse.lwalkenhorst.radioplayer;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

/**
 * Centralize the vlcj library usage.
 * Acts as a wrapper for the MediaPlayerFactory and MediaPlayer given by vlcj.
 */
public abstract class VLCJApiPlayer {

    private MediaPlayer mediaPlayer;

    /**
     * Initialize the VLCJApiPlayer.
     * Responsible for playing music with vlcj library.
     */
    public VLCJApiPlayer() {
        final VLCJFactory factory = new VLCJFactory();
        final MediaPlayerFactory mediaPlayerFactory = factory.newMediaPlayerFactory();
        this.mediaPlayer = factory.newMediaPlayer(mediaPlayerFactory);
    }

    public abstract void play();

    public void playSong(final Song song) {
        mediaPlayer.submit(() -> mediaPlayer.media().play(song.getAbsolutePath()));
    }

    public void skipSong(final Skip skip) {
        mediaPlayer.controls().skipPosition(skip.positionSkip);
    }

    public void pauseSong() {
        mediaPlayer.controls().pause();
    }

    public void resumeSong() {
        mediaPlayer.controls().play();
    }

    protected void registerEventListener(final MediaPlayerEventListener eventListener) {
        mediaPlayer.events().addMediaPlayerEventListener(eventListener);
    }

    /**
     * Specifies a song skip move with the corresponding position move.
     */
    public enum Skip {
        FORWARD(1), BACKWARD(-1);

        private int positionSkip;

        Skip(final int positionSkip) {
            this.positionSkip = positionSkip;
        }

    }
}
