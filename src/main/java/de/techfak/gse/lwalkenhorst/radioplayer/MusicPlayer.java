package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.apiwrapper.VLCJApiWrapper;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer {

    private VLCJApiWrapper apiWrapper;

    public MusicPlayer(VLCJApiWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    private void playSongs(LinkedList<Song> songs) {
        Song song = songs.poll();
        System.out.println("Now playing: " + song.getArtist() + " - " + song.getTitle() + " - " + TimeUnit.MILLISECONDS.toMinutes(song.getDuration()) + " min");
        apiWrapper.playSong(song);
        songs.add(song);
    }

    /**
     * Plays a given playlist.
     * Using the song queue from the playlist {@link Playlist#getQueue()},
     * to play its songs. Playing a song form the queue by using {@link #playSongs(LinkedList songs)}.
     * Repeatedly plays the playlist.
     *
     * @param playlist that will be played.
     */
    public void play(Playlist playlist) {
        LinkedList<Song> songs = playlist.getQueue();
        playSongs(songs);
        apiWrapper.addEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                playSongs(songs);
            }
        });
    }
}
