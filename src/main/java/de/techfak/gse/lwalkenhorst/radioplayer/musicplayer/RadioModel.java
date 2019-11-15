package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

public interface RadioModel {
    Song getCurrentPlayingSong();
    Playlist getCurrentPlaylist();
}
