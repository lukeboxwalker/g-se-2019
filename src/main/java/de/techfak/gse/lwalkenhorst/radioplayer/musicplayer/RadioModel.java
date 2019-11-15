package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.playlist.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

/**
 * Model interface for the radio application.
 */
public interface RadioModel {
    Song getSong();
    Playlist getPlaylist();
    void skipSong();
}
