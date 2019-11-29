package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.cleanup.Cleaner;
import de.techfak.gse.lwalkenhorst.cleanup.NoCleanUpFoundException;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * VLCJFactory to create vlcj-lib objects.
 * Registers object cleanups to CleanUpDemon
 */
public class VLCJFactory implements AutoCloseable {

    private List<Cleaner> cleaners = new ArrayList<>();

    public VLCJFactory() {
        CleanUpDemon.getInstance().register(this, () -> cleaners.forEach(Cleaner::clean));
    }

    /**
     * Creating an new MediaPlayerFactory.
     * Registers the factory release to cleaners.
     *
     * @return new MediaPlayerFactory object
     */
    public MediaPlayerFactory newMediaPlayerFactory() {
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        cleaners.add(0, mediaPlayerFactory::release);
        return mediaPlayerFactory;
    }

    /**
     * Creating an new MediaPlayer.
     * Registers the mediaPlayer stop and release to cleaners.
     *
     * @param factory to create new mediaPlayer
     * @return new MediaPlayer object
     */
    public MediaPlayer newMediaPlayer(MediaPlayerFactory factory) {
        MediaPlayer mediaPlayer = factory.mediaPlayers().newMediaPlayer();
        cleaners.add(0, () -> {
            mediaPlayer.controls().stop();
            mediaPlayer.release();
        });
        return mediaPlayer;
    }

    @Override
    public void close() throws NoCleanUpFoundException {
        CleanUpDemon.getInstance().cleanup(this);
    }
}
