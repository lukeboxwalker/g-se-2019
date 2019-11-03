package de.techfak.gse.lwalkenhorst;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.io.File;
import java.util.LinkedList;
import java.util.Objects;

public class MusicPlayer {

    private MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    private MediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();

    public MusicPlayer() {
    }

    private void playSong(LinkedList<File> fileQueue) {
        mediaPlayer.submit(() -> {
            File first = fileQueue.poll();
            mediaPlayer.media().play(Objects.requireNonNull(first).getAbsolutePath());
            fileQueue.add(first);
        });
    }

    public void play(Playlist playlist) {
        LinkedList<File> fileQueue = playlist.getQueue();
        playSong(fileQueue);

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                playSong(fileQueue);
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
}
