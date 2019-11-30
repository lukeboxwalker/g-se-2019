package de.techfak.gse.lwalkenhorst.radioplayer;

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

    int getVotes(Song song);
    void vote(Song song);
}
