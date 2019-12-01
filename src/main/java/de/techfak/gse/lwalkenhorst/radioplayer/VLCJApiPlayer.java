package de.techfak.gse.lwalkenhorst.radioplayer;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
        VLCJFactory factory = new VLCJFactory();
        MediaPlayerFactory mediaPlayerFactory = factory.newMediaPlayerFactory();
        this.mediaPlayer = factory.newMediaPlayer(mediaPlayerFactory);
    }

    public abstract void play();

    public abstract void loadPlaylist(Playlist playlist);

    public void playSong(Song song) {
        mediaPlayer.submit(() -> mediaPlayer.media().play(song.getAbsolutePath()));
    }

    public void skipSong(Skip skip) {
        mediaPlayer.controls().skipPosition(skip.positionSkip);
    }

    public void pauseSong() {
        mediaPlayer.controls().pause();
    }

    public void resumeSong() {
        mediaPlayer.controls().play();
    }

    /**
     * Adds a song-finish event listener.
     * Adds a MediaPlayerEvent to the media player to listen for finished songs.
     * Performs actions described in consumer.
     *
     * @param songFinished to represent the content of the overridden finished method.
     * @param timeChanged  to represent the content when song moved on playing.
     */
    protected void onEventCall(Consumer<MediaPlayer> songFinished, BiConsumer<MediaPlayer, Float> timeChanged) {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newTime) {
                timeChanged.accept(mediaPlayer, newTime);
            }

            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                songFinished.accept(mediaPlayer);
            }
        });
    }

    /**
     * Specifies a song skip move with the corresponding position move.
     */
    public enum Skip {
        FORWARD(1), BACKWARD(-1);

        private int positionSkip;

        Skip(int positionSkip) {
            this.positionSkip = positionSkip;
        }

    }
}
