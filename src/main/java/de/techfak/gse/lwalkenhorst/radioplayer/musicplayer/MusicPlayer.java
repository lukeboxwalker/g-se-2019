package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer extends VLCJApiPlayer implements RadioModel {

    private Song currentPlayingSong;
    private Playlist currentPlaylist;

    public MusicPlayer() {
        super();
    }

    /**
     * Plays a given playlist.
     * When a song finishes, the next song from the Playlist
     * will start playing. The playlist plays on repeat.
     *
     * @param playlist that will be played.
     */
    @Override
    public void play(Playlist playlist) {
        currentPlaylist = playlist;
        currentPlayingSong = playlist.getFirst();
        playSong(currentPlayingSong);
        onSongFinish(mediaPlayer -> {
            currentPlaylist.dropFirst();
            currentPlaylist.appendSong(currentPlayingSong);
            currentPlayingSong = currentPlaylist.getFirst();
            playSong(currentPlayingSong);
        });
    }

    @Override
    public Song getCurrentPlayingSong() {
        return currentPlayingSong;
    }

    @Override
    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
}
