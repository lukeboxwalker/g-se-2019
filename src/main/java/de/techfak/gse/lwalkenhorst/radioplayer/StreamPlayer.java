package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.server.WebClient;
import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class StreamPlayer extends VLCJApiPlayer implements RadioModel {

    private static final int DELAY_MS = 1000;
    private static final int PERIOD_MS = 10000;

    private WebClient client;
    private Song currentSong = new Song();
    private Playlist playlist = new Playlist();
    private TimerTask timerTask;

    public StreamPlayer(IPlayAble playAble, WebClient client) {
        super(playAble);
        this.client = client;
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
        playSong(currentSong);
    }

    @Override
    public void forEachUpcomingSong(Consumer<Song> consumer) {
        playlist.getSongs().forEach(consumer);
    }


    @Override
    public int getVotes(Song song) {
        return client.requestVote(song);
    }

    @Override
    public void vote(Song song) {
        timerTask.run();
    }
}
