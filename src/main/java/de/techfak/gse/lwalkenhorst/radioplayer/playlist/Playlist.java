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
    private int currentIndex;

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

    /**
     * Performs an action on the next song in the playlist.
     * Checks if there is a next song for the consumer to accept.
     * Passes the next song to the consumer {@link #next()}.
     * Setting the current song to null if there is no next song anymore,
     * to prepare a possible upcoming iteration.
     *
     * @param consumer to accept the song.
     */
    public void forNextSong(final Consumer<Song> consumer) {
        synchronized (this) {
            if (hasNext()) {
                consumer.accept(next());
            } else {
                this.current = null;
            }
        }
    }

    /**
     * Checks for a next song.
     * Returning true if the current song is empty (starting a new iteration),
     * or if there is a song after the current one,
     * or the playlist repeats (start form the beginning)
     *
     * @return if there is a next song in the current iteration.
     */
    private boolean hasNext() {
        if (playList.isEmpty()) {
            return false;
        } else {
            return current == null || (currentIndex + 1) < playList.size() || repeat;
        }
    }

    /**
     * Getting the next song form the playlist.
     *
     * @return the next song.
     */
    private Song next() {
        if (current == null) {
            this.current = playList.peek();
        } else {
            currentIndex += 1;
            if (currentIndex < playList.size()) {
                this.current = playList.get(currentIndex);
            } else if (repeat) {
                this.current = playList.peek();
            }
        }
        return current;
    }

    /**
     * Getting the current song from the playlist.
     * Is synchronized to enable thread safe operations form other methods.
     *
     * @return the current song.
     */
    public Song getCurrent() {
        synchronized (this) {
            return current;
        }
    }

    /**
     * Adding a song to the playlist.
     * Is synchronized to enable thread safe operations form other methods.
     *
     * @param song that should be added to the playlist.
     */
    public void appendSong(final Song song) {
        synchronized (this) {
            playList.add(song);
        }
    }

    /**
     * Shuffles the playlist.
     * Is synchronized to enable thread safe operations form other methods.
     */
    public void shuffle() {
        synchronized (this) {
            Collections.shuffle(playList);
        }
    }

    /**
     * Performs a given operation on each song that will be seen by {@link #next()}.
     * Splits the playlist in two sublist with respect to the current song location.
     * Iterates through both sublist with given consumer.
     *
     * @param consumer to pass it to foreach call.
     */
    public void forEachSong(final Consumer<Song> consumer) {
        synchronized (this) {
            if (current != null) {
                playList.subList(currentIndex, playList.size()).forEach(consumer);
                if (currentIndex > 0 && repeat) {
                    playList.subList(0, currentIndex).forEach(consumer);
                }
            }
        }
    }

    public void setRepeating(final boolean repeat) {
        this.repeat = repeat;
    }
}
