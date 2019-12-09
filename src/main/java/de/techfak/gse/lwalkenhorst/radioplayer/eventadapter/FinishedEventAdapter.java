package de.techfak.gse.lwalkenhorst.radioplayer.eventadapter;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.function.Consumer;

/**
 * Specific Event when song finishes.
 */
public final class FinishedEventAdapter extends MediaPlayerEventAdapter {

    private Consumer<MediaPlayer> consumer;

    public FinishedEventAdapter(final Consumer<MediaPlayer> consumer) {
        super();
        this.consumer = consumer;
    }

    @Override
    public void finished(final MediaPlayer mediaPlayer) {
        consumer.accept(mediaPlayer);
    }

}
