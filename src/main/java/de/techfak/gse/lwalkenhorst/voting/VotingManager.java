package de.techfak.gse.lwalkenhorst.voting;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Voting System for a given playlist.
 */
public class VotingManager {

    private HashMap<Song, Pair<AtomicBoolean, AtomicInteger>> songMap = new HashMap<>();
    private final Playlist playlist;

    /**
     * Creates a new VotingManger.
     * Inits its songMap to relate each song with a voting score.
     *
     * @param playlist that is wrapped to vote for songs
     */
    public VotingManager(Playlist playlist) {
        this.playlist = playlist;
        this.playlist.getSongs().forEach(song -> {
            final Pair<AtomicBoolean, AtomicInteger> voting =
                new Pair<>(new AtomicBoolean(false), new AtomicInteger(0));
            songMap.put(song, voting);
        });
    }

    /**
     * Votes for a given song.
     * Increment the vote score for given song.
     * Sets recently resetted to false
     * Sorts the playlist with respect to the voting scores
     *
     * @param song to vote for
     * @throws NoSongFoundException when given song isn't managed by this VotingManager
     */
    public void vote(Song song) throws NoSongFoundException {
        synchronized (this) {
            if (songMap.containsKey(song)) {
                Pair<AtomicBoolean, AtomicInteger> voting = songMap.get(song);
                voting.getValue().incrementAndGet();
                voting.getKey().set(false);
                playlist.getSongs().sort((song1, song2) -> {
                    Pair<AtomicBoolean, AtomicInteger> voting1 = songMap.get(song1);
                    Pair<AtomicBoolean, AtomicInteger> voting2 = songMap.get(song2);
                    int vote1 = voting1.getKey().get() ? -1 : voting1.getValue().get();
                    int vote2 = voting2.getKey().get() ? -1 : voting2.getValue().get();
                    return Integer.compare(vote2, vote1);
                });
            } else {
                throw new NoSongFoundException(
                    "Trying to vote for a Song that is not managed by this VotingManager");
            }
        }
    }

    /**
     * Resets votes for a given song.
     * Sets recently resetted to true
     *
     * @param song to reset votes
     * @throws NoSongFoundException when given song isn't managed by this VotingManager
     */
    public void resetVotes(Song song) throws NoSongFoundException {
        synchronized (this) {
            if (song != null) {
                if (songMap.containsKey(song)) {
                    Pair<AtomicBoolean, AtomicInteger> voting = songMap.get(song);
                    voting.getValue().set(-1);
                    voting.getKey().set(true);
                    playlist.getSongs().sort((song1, song2) -> {
                        Pair<AtomicBoolean, AtomicInteger> voting1 = songMap.get(song1);
                        Pair<AtomicBoolean, AtomicInteger> voting2 = songMap.get(song2);
                        return Integer.compare(voting2.getValue().get(), voting1.getValue().get());
                    });
                    voting.getValue().set(0);
                } else {
                    throw new NoSongFoundException(
                        "Trying to reset the votes for a Song that is not managed by this VotingManager");
                }
            }
        }
    }

    /**
     * Getting the current voting score form given song.
     *
     * @param song to get votes from
     * @return the voting score for given song
     * @throws NoSongFoundException when given song isn't managed by this VotingManager
     */
    public int getVotes(Song song) throws NoSongFoundException {
        synchronized (this) {
            if (song != null) {
                if (songMap.containsKey(song)) {
                    return songMap.get(song).getValue().get();
                } else {
                    throw new NoSongFoundException(
                        "Trying to get the votes for a Song that is not managed by this VotingManager");
                }
            } else {
                return 0;
            }
        }
    }
}
