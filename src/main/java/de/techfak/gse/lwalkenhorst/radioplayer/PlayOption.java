package de.techfak.gse.lwalkenhorst.radioplayer;


import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;

public class PlayOption implements IPlayAble {

    private String[] option;
    private boolean hasOption;
    private Function<Song, String> function;
    private boolean hasFunction;

    public PlayOption() {
        this.option = null;
        this.hasOption = false;
        this.function = (Song::getFilePath);
        this.hasFunction = true;
    }

    public Runnable play(MediaPlayer mediaPlayer, Song song) {
        if (hasFunction && !hasOption) {
            return () -> mediaPlayer.media().play(function.apply(song));
        } else if (!hasFunction && hasOption) {
            return () -> mediaPlayer.media().play(option[0]);
        } else if (hasFunction) {
            System.out.println(song.getFilePath() + " " + Arrays.toString(option));
            return () -> mediaPlayer.media().play(function.apply(song), option);
        } else {
            return () -> {
            };
        }
    }

    public void setOption(String... option) {
        this.option = option;
        this.hasOption = this.option != null && this.option.length > 0;
    }

    public void setFunction(Function<Song, String> function) {
        this.function = function;
        this.hasFunction = this.function != null;
    }
}
