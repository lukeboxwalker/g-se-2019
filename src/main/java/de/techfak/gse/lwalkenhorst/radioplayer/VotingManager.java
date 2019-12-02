package de.techfak.gse.lwalkenhorst.radioplayer;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Voting System for a given playlist.
 */
public class VotingManager {

    private HashMap<Song, AtomicInteger> songMap = new HashMap<>();
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
            final AtomicInteger voting = new AtomicInteger(0);
            songMap.put(song, voting);
        });
    }

    /**
     * Votes for a given song.
     * Increment the vote score for given song.
     * Sorts the playlist with respect to the voting scores
     *
     * @param song to vote for
     */
    public void vote(Song song) {
        synchronized (this) {
            if (songMap.containsKey(song)) {
                List<Song> songs = playlist.getSongs();
                Song first = songs.remove(0);
                AtomicInteger voting = songMap.get(song);
                voting.incrementAndGet();
                songs.sort((song1, song2) -> Integer.compare(songMap.get(song2).get(), songMap.get(song1).get()));
                songs.add(0, first);
            }
        }
    }

    /**
     * Resets votes for a given song.
     *
     * @param song to reset votes
     */
    public void resetVotes(Song song) {
        synchronized (this) {
            if (song != null && songMap.containsKey(song)) {
                songMap.get(song).set(0);
            }
        }
    }

    /**
     * Getting the current voting score form given song.
     *
     * @param song to get votes from
     * @return the voting score for given song
     */
    public int getVotes(Song song) {
        synchronized (this) {
            if (song != null && songMap.containsKey(song)) {
                return songMap.get(song).get();
            } else {
                return 0;
            }
        }
    }
}
