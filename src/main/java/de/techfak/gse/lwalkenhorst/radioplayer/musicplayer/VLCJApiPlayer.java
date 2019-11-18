package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.radioplayer.playlist.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.function.Consumer;

/**
 * Centralize the vlcj library usage.
 * Acts as a wrapper for the MediaPlayerFactory and MediaPlayer given by vlcj.
 */
public abstract class VLCJApiPlayer {
    private static final int POSITION_SKIP = 1;

    private MediaPlayerFactory mediaPlayerFactory;
    private MediaPlayer mediaPlayer;

    /**
     * Initialize the VLCJApiPlayer.
     * Register its cleanup to {@link CleanUpDemon}
     * used by vlcj library when the application terminates.
     * Responsible for playing music with vlcj library.
     */
    public VLCJApiPlayer() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
        CleanUpDemon.register(this, () -> {
            mediaPlayer.controls().stop();
            mediaPlayerFactory.release();
            mediaPlayer.release();
        });
    }

    public abstract void play(Playlist playlist);

    public void playSong(Song song) {
        mediaPlayer.submit(() -> mediaPlayer.media().play(song.getAbsolutePath()));
    }

    public void skipSong() {
        mediaPlayer.controls().skipPosition(POSITION_SKIP);
    }

    /**
     * Adds a song-finish event listener.
     * Adds a MediaPlayerEvent to the media player to listen for finished songs.
     * Performs actions described in consumer.
     *
     * @param consumer to represent the content of the overridden finished method.
     */
    protected void onSongFinish(Consumer<MediaPlayer> consumer) {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                consumer.accept(mediaPlayer);
            }
        });
    }
}
