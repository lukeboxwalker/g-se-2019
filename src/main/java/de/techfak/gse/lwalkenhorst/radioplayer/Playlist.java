package de.techfak.gse.lwalkenhorst.radioplayer;

import java.util.*;

/**
 * Represents a Playlist (list of songs).
 * The playlist contains a song list that could be played.
 */
public class Playlist {

    private List<Song> playList;

    /**
     * Creates a new Playlist from given songs.
     *
     * @param songs the playlist consist of.
     */
    public Playlist(List<Song> songs) {
        this.playList = Collections.synchronizedList(songs);
    }

    public void shuffle() {
        Collections.shuffle(playList);
    }

    public List<Song> getSongs() {
        return playList;
    }
}
