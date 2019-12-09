package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.eventadapter.FinishedEventAdapter;
import de.techfak.gse.lwalkenhorst.radioplayer.eventadapter.TimeChangedEventAdapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.function.Consumer;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer extends VLCJApiPlayer implements RadioModel {

    public static final String VOTE_UPDATE = "voteUpdate";
    public static final String SONG_UPDATE = "songUpdate";
    public static final String TIME_UPDATE = "timeUpdate";

    private VotingManager votingManager;
    private PropertyChangeSupport support;
    private Playlist playlist;

    private Song currentSong;

    /**
     * Creating a new MusicPlayer to play music.
     * Will repeat its playing songs
     *
     * @param playlist to play
     */
    public MusicPlayer(Playlist playlist) {
        super();
        this.support = new PropertyChangeSupport(this);
        loadPlaylist(playlist);
    }

    private void playNextSong() {
        synchronized (this) {
            if (playlist != null) {
                List<Song> songs = playlist.getSongs();
                Song oldSong = null;
                if (currentSong == null) {
                    currentSong = songs.get(0);
                } else {
                    oldSong = songs.remove(0);
                    songs.add(oldSong);
                    currentSong = songs.get(0);
                    votingManager.resetVotes(oldSong);
                }
                support.firePropertyChange(SONG_UPDATE, oldSong, currentSong);
                playSong(currentSong);
            }
        }
    }

    @Override
    public void loadPlaylist(Playlist playlist) {
        this.playlist = playlist;
        this.votingManager = new VotingManager(this.playlist);
    }

    /**
     * Plays the loaded playlist.
     * When a song finishes, the next song from the Playlist will start playing.
     */
    @Override
    public void play() {
        this.playNextSong();
        this.registerEventListener(new FinishedEventAdapter(mediaPlayer -> playNextSong()));
        this.registerEventListener(new TimeChangedEventAdapter(
            (mediaPlayer, newTime) -> support.firePropertyChange(TIME_UPDATE, 0.0f, newTime))
        );
    }

    /**
     * Performs a given operation on each song that will be played.
     * Splits the playlist in two sublist with respect to the current song location.
     * Iterates through both sublist with given consumer.
     *
     * @param consumer to passed to foreach call.
     */
    public void forEachUpcomingSong(final Consumer<Song> consumer) {
        synchronized (this) {
            List<Song> songs = playlist.getSongs();
            songs.forEach(consumer);
        }
    }

    /**
     * Vote for a given song.
     *
     * @param song to vote for
     */
    public void vote(Song song) {
        synchronized (this) {
            if (!song.equals(currentSong)) {
                votingManager.vote(song);
                int currentVotes = votingManager.getVotes(song);
                support.firePropertyChange(VOTE_UPDATE, currentVotes - 1, currentVotes);
            }
        }
    }

    /**
     * Getting the votes of a song.
     *
     * @param song to get the votes from
     * @return the votes from given song
     */
    public int getVotes(Song song) {
        return votingManager.getVotes(song);
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
    public void addPropertyChangeListener(PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener observer) {
        support.removePropertyChangeListener(observer);
    }


}
