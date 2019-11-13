package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;

import java.util.*;

/**
 * Represents a Playlist (list of songs).
 * The playlist contains a song queue that could be played.
 */
public class Playlist {

    private LinkedList<Song> playList;

    /**
     * Creates a new Playlist from given directory.
     * Instantiate the playlist by reading the directories content with a {@link MusicReader}
     * and getting its content by {@link MusicReader#getSongs()}.
     *
     * @param directoryName the directory the playlist is constructed of.
     */
    public Playlist(String directoryName) {
        try {
            this.playList = new MusicReader(directoryName).getSongs();
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
