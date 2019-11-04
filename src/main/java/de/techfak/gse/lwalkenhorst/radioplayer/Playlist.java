package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;

import java.util.*;

public class Playlist {

    private LinkedList<Song> playList;

    public Playlist(MusicReader reader) {
        try {
            this.playList = new LinkedList<>(reader.getSongs());
            this.shuffle();
        } catch (ExitCodeException e) {
            e.exit();
        }
    }

    public LinkedList<Song> getQueue() {
        return playList;
    }

    public void shuffle() {
        Collections.shuffle(playList);
    }
}
