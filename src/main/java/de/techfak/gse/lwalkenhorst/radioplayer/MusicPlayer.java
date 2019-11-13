package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.apiwrapper.VLCJApiWrapper;

import java.util.LinkedList;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer implements RadioModel {

    private VLCJApiWrapper apiWrapper;
    private Song currentPlayingSong;
    private Playlist currentPlaylist;

    public MusicPlayer(VLCJApiWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    /**
     * Plays a given playlist.
     * Using the song queue from the playlist {@link Playlist#getQueue()},
     * to play its songs. When a song finishes, the next song from the Playlist
     * will start playing. The playlist plays on repeat.
     *
     * @param playlist that will be played.
     */
    public void play(Playlist playlist) {
        this.currentPlaylist = playlist;
        this.currentPlayingSong = playlist.getQueue().peek();
        apiWrapper.playSong(currentPlayingSong);
        apiWrapper.onSongFinish(mediaPlayer -> {
            LinkedList<Song> songs = currentPlaylist.getQueue();
            songs.poll();
            songs.add(currentPlayingSong);
            this.currentPlayingSong = songs.peek();
            apiWrapper.playSong(currentPlayingSong);
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
