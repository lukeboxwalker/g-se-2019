package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.playlist.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer extends VLCJApiPlayer implements RadioModel {

    private Playlist playlist;

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
        this.playlist = playlist;
        this.playlist.forNextSong(this::playSong);
        this.onSongFinish(mediaPlayer -> this.playlist.forNextSong(this::playSong));
    }

    @Override
    public Song getSong() {
        return playlist.getCurrent();
    }

    @Override
    public Playlist getPlaylist() {
        return playlist;
    }
}
