package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.eventadapter.FinishedEventAdapter;
import de.techfak.gse.lwalkenhorst.radioplayer.eventadapter.TimeChangedEventAdapter;

import java.util.List;
import java.util.function.Consumer;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer extends VLCJApiPlayer implements RadioModel {
    private VotingManager votingManager;

    private Playlist playlist = new Playlist();
    private Song currentSong = new Song();

    /**
     * Creating a new MusicPlayer to play music.
     * Will repeat its playing songs
     */
    public MusicPlayer(final IPlayAble playAble) {
        super(playAble);
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
                playSong(currentSong);
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
     * Performs a given operation on each song that will be played.
     * Splits the playlist in two sublist with respect to the current song location.
     * Iterates through both sublist with given consumer.
     *
     * @param consumer to passed to foreach call.
     */
    @Override
    public void forEachUpcomingSong(final Consumer<Song> consumer) {
        synchronized (this) {
            final List<Song> songs = playlist.getSongs();
            songs.forEach(consumer);
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

    /**
     * Getting the votes of a song.
     *
     * @param song to get the votes from
     * @return the votes from given song
     */
    @Override
    public int getVotes(final Song song) {
        return votingManager.getVotes(song);
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
