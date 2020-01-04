package de.techfak.gse.lwalkenhorst.radioplayer;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Centralize the vlcj library usage.
 * Acts as a wrapper for the MediaPlayerFactory and MediaPlayer given by vlcj.
 */
public abstract class VLCJApiPlayer {

    public static final String VOTE_UPDATE = "voteUpdate";
    public static final String SONG_UPDATE = "songUpdate";
    public static final String TIME_UPDATE = "timeUpdate";

    private MediaPlayer mediaPlayer;
    private IPlayBehavior playBehavior;
    private PropertyChangeSupport support;

    /**
     * Initialize the VLCJApiPlayer.
     * Responsible for playing music with vlcj library.
     *
     * @param playBehavior supplies runnable to submit for playing a song.
     */
    public VLCJApiPlayer(IPlayBehavior playBehavior) {
        final VLCJFactory factory = new VLCJFactory();
        final MediaPlayerFactory mediaPlayerFactory = factory.newMediaPlayerFactory();
        this.mediaPlayer = factory.newMediaPlayer(mediaPlayerFactory);
        this.support = new PropertyChangeSupport(this);
        this.playBehavior = playBehavior;
    }

    public VLCJApiPlayer() {
        this(new NormalPlayBehavior());
    }

    public void setPlayBehavior(IPlayBehavior playBehavior) {
        this.playBehavior = playBehavior;
    }

    public void playSong(final Song song) {
        mediaPlayer.submit(playBehavior.play(mediaPlayer, song));
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

    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
    }

    public void removePropertyChangeListener(final PropertyChangeListener observer) {
        support.removePropertyChangeListener(observer);
    }

    protected PropertyChangeSupport getSupport() {
        return support;
    }
}
