package de.techfak.gse.lwalkenhorst.radioplayer;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

public interface IPlayAble {
    Runnable play(MediaPlayer mediaPlayer, Song song);
}
