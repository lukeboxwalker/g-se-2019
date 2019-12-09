package de.techfak.gse.lwalkenhorst.radioplayer.eventadapter;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.function.BiConsumer;

/**
 * Specific Event when song plays.
 * Used when current song time changed
 */
public final class TimeChangedEventAdapter extends MediaPlayerEventAdapter {

    private BiConsumer<MediaPlayer, Float> consumer;

    public TimeChangedEventAdapter(final BiConsumer<MediaPlayer, Float> consumer) {
        super();
        this.consumer = consumer;
    }

    @Override
    public void positionChanged(final MediaPlayer mediaPlayer, final float newTime) {
        consumer.accept(mediaPlayer, newTime);
    }

}
