package de.techfak.gse.lwalkenhorst;

import java.io.File;
import java.util.*;

public class Playlist {

    private LinkedList<File> playList;

    public Playlist(List<File> fileList) {
        this.playList = new LinkedList<>(fileList);
        this.shuffle();
    }

    public LinkedList<File> getQueue() {
        return playList;
    }

    public void shuffle() {
        Collections.shuffle(playList);
    }
}
