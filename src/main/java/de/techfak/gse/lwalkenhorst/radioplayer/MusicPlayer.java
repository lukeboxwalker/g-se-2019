package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.apiwrapper.VLCJApiWrapper;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer {

    private VLCJApiWrapper apiWrapper;

    public MusicPlayer(VLCJApiWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    private void playSong(LinkedList<Song> songs) {
        Song song = songs.poll();
        String separate = " - ";
        System.out.println("Now playing: " + song.getArtist() + separate + song.getTitle() + separate + TimeUnit.MILLISECONDS.toMinutes(song.getDuration()) + " min");
        apiWrapper.playSong(song);
        songs.add(song);
    }

    public void play(Playlist playlist) {
        LinkedList<Song> songs = playlist.getQueue();
        playSong(songs);
        apiWrapper.addEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                playSong(songs);
            }
        });
    }
}
