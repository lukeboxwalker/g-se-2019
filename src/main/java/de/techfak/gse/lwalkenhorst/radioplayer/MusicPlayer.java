package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.voting.NoSongFoundException;
import de.techfak.gse.lwalkenhorst.voting.VotingManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.function.Consumer;

/**
 * Plays mp3 songs by using the vlcj library.
 */
public class MusicPlayer extends VLCJApiPlayer implements RadioModel {

    /**
     * Current song that is playing.
     * Represents the song and the corresponding index in the playlist
     */
    private static final class SongEntry {
        private Song song;
        private int index;

        private SongEntry(Song song, int index) {
            this.song = song;
            this.index = index;
        }
    }

    public static final String VOTE_UPDATE = "voteUpdate";
    public static final String SONG_UPDATE = "songUpdate";
    public static final String TIME_UPDATE = "timeUpdate";

    private VotingManager votingManager;
    private PropertyChangeSupport support;
    private Playlist playlist;
    private SongEntry current;
    private boolean repeat;

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
        this.repeat = true;
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
     * When a song finishes, the next song from the Playlist
     * will start playing.
     */
    @Override
    public void play() {
        this.playNextSong();
        this.onEventCall(mediaPlayer -> playNextSong(),
            (mediaPlayer, newTime) -> support.firePropertyChange(TIME_UPDATE, 0.0f, newTime));
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
     * Sets the current index to 0 to start from the highest voted songs
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

    /**
     * Getting the current song from the playlist.
     * Is synchronized to enable thread safe operations form other methods.
     *
     * @return the current song.
     */
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
