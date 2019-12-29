package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.WebClient;

import java.util.function.Consumer;

public class StreamPlayer extends VLCJApiPlayer implements RadioModel {

    private WebClient client;
    private Song currentSong = new Song();
    private Playlist playlist = new Playlist();

    public StreamPlayer(IPlayAble playAble, WebClient client) {
        super(playAble);
        this.client = client;
    }

    @Override
    public Song getSong() {
        currentSong = client.requestSong();
        return currentSong;
    }

    @Override
    public Playlist getPlaylist() {
        playlist = client.requestPlaylist();
        return playlist;
    }

    @Override
    public void start() {

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

    }
}
