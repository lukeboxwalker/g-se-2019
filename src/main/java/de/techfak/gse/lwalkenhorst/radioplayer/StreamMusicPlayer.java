package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.server.ClientSocket;

/**
 * Plays vljc stream by using the vlcj library.
 */
public class StreamMusicPlayer extends VLCJMediaPlayer implements StreamPlayer {

    private ClientSocket client;
    private Song currentSong;
    private Playlist playlist;

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
        this.playlist = client.requestPlaylist();
        this.currentSong = client.requestSong();
        getSupport().firePropertyChange(SONG_UPDATE, null, currentSong);
        play(currentSong);
    }

    @Override
    public int getVotes(Song song) {
        return getVotes(song.getUuid());
    }

    @Override
    public int getVotes(String uuid) {
        return client.requestVote(uuid);
    }

    @Override
    public void vote(Song song) {
        vote(song.getUuid());
    }

    @Override
    public void vote(String uuid) {
        client.vote(uuid);
    }

    @Override
    public void setWebClient(ClientSocket client) {
        this.client = client;
    }

    @Override
    public float getCurrentPlayTime() {
        return client.requestCurrentTime();
    }

    @Override
    public void disconnect() {
        if (client != null) {
            this.stop();
            client.disconnect();
        }
    }

    @Override
    public void updateFromServer(String update) {
        switch (update) {
            case SONG_UPDATE:
                this.playlist = client.requestPlaylist();
                this.currentSong = client.requestSong();
                getSupport().firePropertyChange(update, null, currentSong);
                break;
            case VOTE_UPDATE:
                this.playlist = client.requestPlaylist();
                getSupport().firePropertyChange(update, 0, 1);
                break;
            case TIME_UPDATE:
                getSupport().firePropertyChange(update, 0f, getCurrentPlayTime());
                break;
            default:
        }
    }
}
