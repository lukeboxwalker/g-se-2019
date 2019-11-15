package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.playlist.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

public interface RadioModel {
    Song getSong();
    Playlist getPlaylist();
    void skipSong();
}
