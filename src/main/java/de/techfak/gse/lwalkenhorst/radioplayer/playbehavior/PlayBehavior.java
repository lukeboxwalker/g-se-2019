package de.techfak.gse.lwalkenhorst.radioplayer.playbehavior;

import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * PlayBehavior to play a song in different ways.
 */
public interface PlayBehavior {

    /**
     * Specific play method.
     *
     * @param mediaPlayer to play song
     * @param song to play
     * @return new Runnable to submit to vlcj
     */
    Runnable play(MediaPlayer mediaPlayer, Song song);
}
