package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.apiwrapper.MediaLoader;
import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;

import java.util.*;

public class Playlist {

    private LinkedList<Song> playList;

    public Playlist(String directoryName, MediaLoader mediaLoader) {
        try {
            this.playList = new MusicReader(directoryName, mediaLoader).getSongs();
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
