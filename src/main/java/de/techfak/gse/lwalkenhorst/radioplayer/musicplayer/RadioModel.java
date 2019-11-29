package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.playlist.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

/**
 * Model interface for the radio application.
 */
public interface RadioModel {
    Song getSong();
    Playlist getPlaylist();
    void skipSong();
    void forEachUpcomingSong(final Consumer<Song> consumer);

    void addPropertyChangeListener(PropertyChangeListener observer);
    void removePropertyChangeListener(PropertyChangeListener observer);
}
