package de.techfak.gse.lwalkenhorst.radioplayer.playlist;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

import java.util.*;

/**
 * Represents a Playlist (list of songs).
 * The playlist contains a song list that could be played.
 */
public class Playlist {

    private List<Song> playList;

    /**
     * Creates a new Playlist from given directory.
     * Instantiate the playlist by reading the directories content with a {@link MusicReader}
     * and getting its content by {@link MusicReader#getSongs()}.
     *
     * @param directoryName the directory the playlist is constructed of.
     */
    public Playlist(String directoryName) {
        try {
            MusicReader musicReader = new MusicReader(directoryName);
            this.playList = Collections.synchronizedList(musicReader.getSongs());
        } catch (ExitCodeException e) {
            e.exit();
        }
    }

    public void shuffle() {
        Collections.shuffle(playList);
    }

    public List<Song> getSongs() {
        return playList;
    }
}
