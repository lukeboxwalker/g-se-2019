package de.techfak.gse.lwalkenhorst.radioplayer;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Voting System for a given playlist.
 */
public class VotingManager {

    private HashMap<String, AtomicInteger> songMap = new HashMap<>();
    private final Playlist playlist;

    /**
     * Creates a new VotingManger.
     * Inits its songMap to relate each song with a voting score.
     *
     * @param playlist that is wrapped to vote for songs
     */
    public VotingManager(final Playlist playlist) {
        this.playlist = playlist;
        this.playlist.getSongs().forEach(song -> {
            final AtomicInteger voting = new AtomicInteger(0);
            songMap.put(song.getUuid(), voting);
        });
    }

    /**
     * Votes for a given song.
     * Increment the vote score for given song.
     * Sorts the playlist with respect to the voting scores
     *
     * @param uuid to vote for
     */
    public void vote(final String uuid) {
        synchronized (this) {
            if (songMap.containsKey(uuid)) {
                final List<Song> songs = playlist.getSongs();
                final Song first = songs.remove(0);
                final AtomicInteger voting = songMap.get(uuid);
                voting.incrementAndGet();
                songs.sort((song1, song2) -> Integer.compare(
                    songMap.get(song2.getUuid()).get(), songMap.get(song1.getUuid()).get()));
                songs.add(0, first);
            }
        }
    }

    /**
     * Resets votes for a given song.
     *
     * @param songUUID to reset votes
     */
    public void resetVotes(final String songUUID) {
        synchronized (this) {
            if (songUUID != null && songMap.containsKey(songUUID)) {
                songMap.get(songUUID).set(0);
            }
        }
    }

    /**
     * Getting the current voting score form given songUUID.
     *
     * @param songUUID to get votes from
     * @return the voting score for given songUUID
     */
    public int getVotes(final String songUUID) {
        synchronized (this) {
            if (songUUID != null && songMap.containsKey(songUUID)) {
                return songMap.get(songUUID).get();
            } else {
                return 0;
            }
        }
    }
}
