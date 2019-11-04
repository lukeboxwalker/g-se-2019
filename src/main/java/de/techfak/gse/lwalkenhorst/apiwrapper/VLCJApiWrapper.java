package de.techfak.gse.lwalkenhorst.apiwrapper;

import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;

public class VLCJApiWrapper implements MediaLoader {

    private MediaPlayerFactory mediaPlayerFactory;
    private MediaPlayer mediaPlayer;

    public VLCJApiWrapper() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
    }

    @Override
    public Media loadMedia(File file) {
        final Media media = mediaPlayerFactory.media().newMedia(file.getAbsolutePath());
        final ParsedWaiter parsed = new ParsedWaiter(media) {
            @Override
            protected boolean onBefore(final Media component) {
                return media.parsing().parse();
            }
        };

        try {
            parsed.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return media;
    }

    public void playSong(Song song) {
        mediaPlayer.submit(() -> mediaPlayer.media().play(song.getMedia().newMediaRef()));
    }

    public void addEventListener(MediaPlayerEventAdapter eventAdapter) {
        mediaPlayer.events().addMediaPlayerEventListener(eventAdapter);
    }

    public void release() {
        //releasing Memory used by mediaPlayerFactory and mediaPlayer
        mediaPlayer.controls().stop();
        mediaPlayerFactory.release();
        mediaPlayer.release();
        System.out.println("Closed MediaPlayer");
    }
}
