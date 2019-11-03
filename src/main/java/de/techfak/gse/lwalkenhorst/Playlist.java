package de.techfak.gse.lwalkenhorst;

import java.util.*;

public class Playlist {

    private LinkedList<Song> playList;

    public Playlist(List<Song> fileList) {
        this.playList = new LinkedList<>(fileList);
        this.shuffle();
    }

    public LinkedList<Song> getQueue() {
        return playList;
    }

    public void shuffle() {
        Collections.shuffle(playList);
    }
}
