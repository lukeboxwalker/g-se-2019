package de.techfak.gse.lwalkenhorst.radioplayer.musicplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;

import java.util.LinkedList;

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
     * Using the song queue from the playlist {@link Playlist#getQueue()},
     * to play its songs. When a song finishes, the next song from the Playlist
     * will start playing. The playlist plays on repeat.
     *
     * @param playlist that will be played.
     */
    @Override
    public void play(Playlist playlist) {
        this.currentPlaylist = playlist;
        this.currentPlayingSong = playlist.getQueue().peek();
        this.playSong(currentPlayingSong);
        this.onSongFinish(mediaPlayer -> {
            LinkedList<Song> songs = currentPlaylist.getQueue();
            songs.poll();
            songs.add(currentPlayingSong);
            this.currentPlayingSong = songs.peek();
            this.playSong(currentPlayingSong);
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