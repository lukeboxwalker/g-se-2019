package de.techfak.gse.lwalkenhorst.radioplayer;

import java.util.*;

/**
 * Represents a Playlist (list of songs).
 * The playlist contains a song list that could be played.
 */
public class Playlist {

    private List<Song> songList;

    /**
     * Creates a new Playlist from given songs.
     *
     * @param songs the playlist consist of.
     */
    public Playlist(final List<Song> songs) {
        this.songList = Collections.synchronizedList(songs);
    }

    public void shuffle() {
        Collections.shuffle(songList);
    }

    public List<Song> getSongs() {
        return songList;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Iterator<Song> iterator = songList.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next().toString());
            if (iterator.hasNext()) {
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }
}
