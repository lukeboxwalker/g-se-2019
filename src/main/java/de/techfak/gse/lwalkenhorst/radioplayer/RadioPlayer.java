package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.radioplayer.playbehavior.PlayBehavior;

/**
 * Model interface for radio application.
 */
public interface RadioPlayer extends Observable {

    /**
     * Radio has current song.
     * If the Radio is not started yet returns
     * an empty song object.
     *
     * @return current played song.
     */
    Song getSong();

    /**
     * Radio operates on a playlist.
     * If the Radio is not started yet returns
     * an empty playlist object. (empty song list)
     *
     * @return current playlist
     */
    Playlist getPlaylist();

    /**
     * Skipping current song.
     * Using Skip enum {@link VLCJMediaPlayer.Skip} to
     * skip FORWARD or BACKWARDS
     *
     * @param skip operation
     */
    void skipSong(VLCJMediaPlayer.Skip skip);

    /**
     * Pauses the current song.
     * Will stop the song until
     * the radio resumes playing.
     */
    void pauseSong();

    /**
     * Resuming the current song.
     * Used after song was pauses to be
     * able to play again.
     */
    void resumeSong();

    /**
     * Starts the radio.
     * Should only called once to start
     * playing music on given playlist.
     */
    void start();

    /**
     * Getting votes for given song.
     *
     * @param song to get votes for
     * @return number of votes
     */
    int getVotes(Song song);

    /**
     * Getting votes for given song (uuid).
     *
     * @param uuid to get votes for
     * @return number of votes
     */
    int getVotes(String uuid);

    /**
     * Vote for a specific song.
     * @param song to vote for
     */
    void vote(Song song);

    /**
     * Setting the playBehavior of the radio.
     * Changes style of playing e.g. local, streaming
     * stream listener...
     *
     * @param playBehavior to set
     */
    void setPlayBehavior(PlayBehavior playBehavior);
}
