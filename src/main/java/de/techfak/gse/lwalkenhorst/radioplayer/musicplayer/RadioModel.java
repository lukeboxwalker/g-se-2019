package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;

public interface RadioModel {
    Song getCurrentPlayingSong();
    Playlist getCurrentPlaylist();
}
