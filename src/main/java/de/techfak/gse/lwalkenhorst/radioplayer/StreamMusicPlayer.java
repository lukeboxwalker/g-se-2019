package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.server.WebClient;
import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Plays vljc stream by using the vlcj library.
 */
public class StreamMusicPlayer extends VLCJMediaPlayer implements StreamPlayer {

    private static final int DELAY_MS = 0;
    private static final int PERIOD_MS = 10000;

    private WebClient client;
    private Song currentSong;
    private Playlist playlist;
    private TimerTask timerTask;

    public StreamMusicPlayer() {
        this.currentSong = new Song();
        this.playlist = new Playlist();
    }

    @Override
    public Song getSong() {
        return currentSong;
    }

    @Override
    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public void start() {
        if (client != null) {
            Timer timer = new Timer();
            this.timerTask = new TimerTask() {
                @Override
                public void run() {
                    currentSong = client.requestSong();
                    playlist = client.requestPlaylist();
                    getSupport().firePropertyChange(SONG_UPDATE, null, currentSong);
                }
            };
            timer.schedule(timerTask, DELAY_MS, PERIOD_MS);
            CleanUpDemon.getInstance().register(this, timer::cancel);
            play(currentSong);
        }
    }

    @Override
    public int getVotes(Song song) {
        return client.requestVote(song);
    }

    @Override
    public void vote(Song song) {
        timerTask.run();
    }

    @Override
    public void useWebClient(WebClient client) {
        this.client = client;
    }
}
