package de.techfak.gse.lwalkenhorst.radioplayer;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * PlayBehavior to play a song in different ways.
 */
public interface IPlayBehavior {

    /**
     * Specific play method.
     *
     * @param mediaPlayer to play song
     * @param song to play
     * @return new Runnable to submit to vlcj
     */
    Runnable play(MediaPlayer mediaPlayer, Song song);
}
