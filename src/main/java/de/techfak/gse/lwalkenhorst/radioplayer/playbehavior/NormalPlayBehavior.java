package de.techfak.gse.lwalkenhorst.radioplayer.playbehavior;

import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * NormalPlayBehavior when playing in local mode.
 */
public class NormalPlayBehavior implements PlayBehavior {

    @Override
    public Runnable play(MediaPlayer mediaPlayer, Song song) {
        return () -> mediaPlayer.media().play(song.getFilePath());
    }
}
