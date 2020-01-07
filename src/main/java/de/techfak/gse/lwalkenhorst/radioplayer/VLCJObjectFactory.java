package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.closeup.ObjectCloseupManager;
import de.techfak.gse.lwalkenhorst.closeup.Closeup;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * VLCJFactory to create vlcj-lib objects.
 * Registers object cleanups to CleanUpDemon
 */
public class VLCJObjectFactory implements AutoCloseable {

    private List<Closeup> closeups = new ArrayList<>();

    public VLCJObjectFactory() {
        ObjectCloseupManager.getInstance().register(this, () -> closeups.forEach(Closeup::close));
    }

    /**
     * Creating an new MediaPlayerFactory.
     * Registers the factory release to cleaners.
     *
     * @return new MediaPlayerFactory object
     */
    public MediaPlayerFactory newMediaPlayerFactory() {
        final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        closeups.add(0, mediaPlayerFactory::release);
        return mediaPlayerFactory;
    }

    /**
     * Creating an new MediaPlayer.
     * Registers the mediaPlayer stop and release to cleaners.
     *
     * @param factory to create new mediaPlayer
     * @return new MediaPlayer object
     */
    public MediaPlayer newMediaPlayer(final MediaPlayerFactory factory) {
        final MediaPlayer mediaPlayer = factory.mediaPlayers().newMediaPlayer();
        closeups.add(0, () -> {
            mediaPlayer.controls().stop();
            mediaPlayer.release();
        });
        return mediaPlayer;
    }

    @Override
    public void close() {
        ObjectCloseupManager.getInstance().closeObject(this);
    }
}
