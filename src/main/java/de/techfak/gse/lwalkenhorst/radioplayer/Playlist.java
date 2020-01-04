package de.techfak.gse.lwalkenhorst.radioplayer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * Represents a Playlist (list of songs).
 * The playlist contains a song list that could be played.
 */
public class Playlist {

    private static final int LIST_BOUNDARY = 100;
    private List<Song> songList = new ArrayList<>();

    public void shuffle() {
        Collections.shuffle(songList);
    }

    @JsonIgnore
    public List<Song> getSongs() {
        return songList;
    }

    /**
     * Song list limited by 100.
     * Used in json parsing to limit json object.
     *
     * @return shorted song list
     */
    public List<Song> getSongList() {
        if (songList.size() > LIST_BOUNDARY) {
            return songList.subList(0, LIST_BOUNDARY);
        } else {
            return songList;
        }
    }

    public void setSongList(List<Song> songs) {
        this.songList = Collections.synchronizedList(songs);
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
