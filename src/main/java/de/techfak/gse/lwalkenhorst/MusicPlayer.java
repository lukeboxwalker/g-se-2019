package de.techfak.gse.lwalkenhorst;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.LinkedList;
import java.util.Objects;

public class MusicPlayer {

    private static final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    private static final MediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();

    public MusicPlayer() {
    }

    private void playSong(LinkedList<Song> songs) {
        mediaPlayer.submit(() -> {
            Song firstSong = songs.poll();
            mediaPlayer.media().play(Objects.requireNonNull(firstSong).getAbsolutePath());
            songs.add(firstSong);
        });
    }

    public void play(Playlist playlist) {
        LinkedList<Song> songs = playlist.getQueue();
        playSong(songs);

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                playSong(songs);
            }
        });
    }

    public void release() {
        //releasing Memory used by mediaPlayerFactory and mediaPlayer
        mediaPlayer.controls().stop();
        mediaPlayerFactory.release();
        mediaPlayer.release();
        System.out.println("Closed MediaPlayer");
    }

    public MediaPlayerFactory getMediaPlayerFactory() {
        return mediaPlayerFactory;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
