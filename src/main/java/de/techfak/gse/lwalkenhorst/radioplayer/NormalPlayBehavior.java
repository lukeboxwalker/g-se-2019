package de.techfak.gse.lwalkenhorst.radioplayer;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * NormalPlayBehavior when playing in local mode.
 */
public class NormalPlayBehavior implements IPlayBehavior {

    @Override
    public Runnable play(MediaPlayer mediaPlayer, Song song) {
        return () -> mediaPlayer.media().play(song.getFilePath());
    }
}
