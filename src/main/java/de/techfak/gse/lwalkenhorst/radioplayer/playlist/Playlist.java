package de.techfak.gse.lwalkenhorst.radioplayer.playlist;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

import java.util.*;
import java.util.function.Consumer;

/**
 * Represents a Playlist (list of songs).
 * The playlist contains a song queue that could be played.
 */
public class Playlist {

    private LinkedList<Song> playList;
    private Song current;

    private volatile boolean repeat;

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
            this.repeat = false;
        } catch (ExitCodeException e) {
            e.exit();
        }
    }

    public void forNextSong(final Consumer<Song> consumer) {
        synchronized (this) {
            if (hasNext()) {
                consumer.accept(next());
            } else {
                this.current = null;
            }
        }
    }

    private boolean hasNext() {
        if (playList.isEmpty()) {
            return false;
        } else {
            return current == null || (playList.indexOf(current) + 1) < playList.size() || repeat;
        }
    }

    private Song next() {
        synchronized (this) {
            if (current == null) {
                this.current = playList.peek();
            } else {
                int index = playList.indexOf(current) + 1;
                if (index < playList.size()) {
                    this.current = playList.get(index);
                } else if (repeat) {
                    this.current = playList.peek();
                }
            }
            return current;
        }
    }

    public Song getCurrent() {
        synchronized (this) {
            return current;
        }
    }

    public void appendSong(final Song song) {
        synchronized (this) {
            playList.add(song);
        }
    }

    public void shuffle() {
        synchronized (this) {
            Collections.shuffle(playList);
        }
    }

    public void forEachSong(final Consumer<Song> consumer) {
        synchronized (this) {
            if (current != null) {
                int index = playList.indexOf(current);
                playList.subList(playList.indexOf(current), playList.size()).forEach(consumer);
                if (index > 0 && repeat) {
                    playList.subList(0, playList.indexOf(current)).forEach(consumer);
                }
            }
        }
    }

    public void setRepeating(final boolean repeat) {
        this.repeat = repeat;
    }
}
