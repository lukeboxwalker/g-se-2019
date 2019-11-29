package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.playlist.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.function.Consumer;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer extends VLCJApiPlayer implements RadioModel {

    /**
     * Current song that is playing.
     * Represents the song and the corresponding index in the playlist
     */
    private static final class SongEntry {
        private Song song;
        private int index;
        private SongEntry(Song song, int index) {
            this.song = song;
            this.index = index;
        }
    }

    private PropertyChangeSupport support;
    private Playlist playlist;
    private SongEntry current;
    private boolean repeat;

    /**
     * Creating a new MusicPlayer to play music.
     * Will repeat its playing songs
     */
    public MusicPlayer() {
        super();
        this.support = new PropertyChangeSupport(this);
        this.repeat = true;
    }

    private void playNextSong() {
        synchronized (this) {
            if (playlist != null) {
                List<Song> songs = playlist.getSongs();
                if (playlist != null && !songs.isEmpty()) {
                    SongEntry oldCurrent = current;
                    if (current == null) {
                        current = new SongEntry(songs.get(0), 0);
                    } else {
                        int nextIndex = current.index + 1;
                        if (nextIndex < songs.size()) {
                            this.current = new SongEntry(songs.get(nextIndex), nextIndex);
                        } else if (repeat) {
                            current = new SongEntry(songs.get(0), 0);
                        } else {
                            current = null;
                        }
                    }
                    Song oldSong = oldCurrent == null ? null : oldCurrent.song;
                    Song newSong = current == null ? null : current.song;
                    support.firePropertyChange("song", oldSong, newSong);
                    if (newSong != null) {
                        playSong(newSong);
                    }
                }
            }
        }
    }

    @Override
    public void loadPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Plays the loaded playlist.
     * When a song finishes, the next song from the Playlist
     * will start playing.
     */
    @Override
    public void play() {
        this.playNextSong();
        this.onSongFinish(mediaPlayer -> playNextSong());
    }

    /**
     * Performs a given operation on each song that will be played.
     * Splits the playlist in two sublist with respect to the current song location.
     * Iterates through both sublist with given consumer.
     *
     * @param consumer to passed to foreach call.
     */
    public void forEachUpcomingSong(final Consumer<Song> consumer) {
        synchronized (this) {
            List<Song> songs = playlist.getSongs();
            if (current != null) {
                songs.subList(current.index, songs.size()).forEach(consumer);
                if (current.index > 0 && repeat) {
                    songs.subList(0, current.index).forEach(consumer);
                }
            }
        }
    }

    /**
     * Getting the current song from the playlist.
     * Is synchronized to enable thread safe operations form other methods.
     *
     * @return the current song.
     */
    @Override
    public Song getSong() {
        return current == null ? null : current.song;
    }

    @Override
    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener observer) {
        support.removePropertyChangeListener(observer);
    }


}
