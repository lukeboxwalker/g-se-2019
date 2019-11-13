package de.techfak.gse.lwalkenhorst.apiwrapper;

import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;
import java.util.function.Consumer;

/**
 * Centralize the vlcj library usage.
 * Acts as a wrapper for the MediaPlayerFactory and MediaPlayer given by vlcj.
 */
public class VLCJApiWrapper {

    private MediaPlayerFactory mediaPlayerFactory;
    private MediaPlayer mediaPlayer;

    public VLCJApiWrapper() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
    }

    public void playSong(Song song) {
        mediaPlayer.submit(() -> mediaPlayer.media().play(song.getAbsolutePath()));
    }

    /**
     * Adds a song-finish event listener.
     * Adds a MediaPlayerEvent to the media player to listen for finished songs.
     * Performs actions described in consumer.
     *
     * @param consumer to represent the content of the overridden finished method.
     */
    public void onSongFinish(Consumer<MediaPlayer> consumer) {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                consumer.accept(mediaPlayer);
            }
        });
    }

    /**
     * Releasing the Memory used by vlcj.
     * Stops playing music and release the memory, used by
     * the vlcj library (MediaPlayerFactory and MediaPlayer)
     */
    public void release() {
        mediaPlayer.controls().stop();
        mediaPlayerFactory.release();
        mediaPlayer.release();
    }
}
