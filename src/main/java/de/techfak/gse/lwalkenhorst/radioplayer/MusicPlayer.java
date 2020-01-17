package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.eventadapter.FinishedEventAdapter;
import de.techfak.gse.lwalkenhorst.radioplayer.eventadapter.TimeChangedEventAdapter;

import java.util.List;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer extends VLCJMediaPlayer {
    private VotingManager votingManager;

    private Playlist playlist = new Playlist();
    private Song currentSong = new Song();

    /**
     * Creating a new MusicPlayer to play music.
     * Will repeat its playing songs
     */
    public MusicPlayer() {
        super();
        this.votingManager = new VotingManager(this.playlist);
    }

    private void playNextSong() {
        synchronized (this) {
            if (playlist != null) {
                final List<Song> songs = playlist.getSongs();
                Song oldSong = null;
                if (currentSong == null) {
                    currentSong = songs.get(0);
                } else {
                    oldSong = songs.remove(0);
                    songs.add(oldSong);
                    currentSong = songs.get(0);
                    votingManager.resetVotes(oldSong);
                }
                getSupport().firePropertyChange(SONG_UPDATE, oldSong, currentSong);
                play(currentSong);
            }
        }
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        this.votingManager = new VotingManager(this.playlist);
    }

    /**
     * Plays the loaded playlist.
     * When a song finishes, the next song from the Playlist will start playing.
     */
    @Override
    public void start() {
        if (!playlist.getSongs().isEmpty()) {
            this.currentSong = null;
            this.playNextSong();
            this.registerEventListener(new FinishedEventAdapter(mediaPlayer -> playNextSong()));
            this.registerEventListener(new TimeChangedEventAdapter(
                (mediaPlayer, newTime) -> getSupport().firePropertyChange(TIME_UPDATE, 0.0f, newTime))
            );
        }
    }

    /**
     * Vote for a given song.
     *
     * @param song to vote for
     */
    @Override
    public void vote(final Song song) {
        synchronized (this) {
            if (!song.equals(currentSong)) {
                votingManager.vote(song);
                final int currentVotes = votingManager.getVotes(song);
                getSupport().firePropertyChange(VOTE_UPDATE, currentVotes - 1, currentVotes);
            }
        }
    }

    @Override
    public int getVotes(final Song song) {
        return votingManager.getVotes(song);
    }

    @Override
    public int getVotes(final String uuid) {
        return votingManager.getVotes(uuid);
    }

    public VotingManager getVotingManager() {
        return votingManager;
    }

    @Override
    public Song getSong() {
        return currentSong;
    }

    @Override
    public Playlist getPlaylist() {
        return playlist;
    }

}
