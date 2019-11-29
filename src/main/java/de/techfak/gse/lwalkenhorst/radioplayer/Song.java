package de.techfak.gse.lwalkenhorst.radioplayer;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Represents a mp3 format Song.
 * The Song saves all metadata available form a loaded media,
 * as well as its complete file.
 */
public class Song {

    private File file;

    private String title = "";
    private String artist = "";
    private String album = "";
    private String genre = "";
    private String artWorkURL = "";
    private long duration = 0;

    /**
     * Creating a new Song from given file and metadata.
     *
     * @param file     the mp3 is located
     * @param title    of the song
     * @param artist   of the song
     * @param album    of the song
     * @param genre    of the song
     * @param artWorkURL of the image cover
     * @param duration of the song in millis
     */
    public Song(File file, String title, String artist, String album, String genre, String artWorkURL, long duration) {
        this(file);
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.artWorkURL = artWorkURL;
        this.duration = duration;
    }

    protected Song(File file) {
        this.file = file;
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public String getDuration() {
        return millisToString(duration);
    }

    public String getArtWorkURL() {
        return artWorkURL;
    }

    public long getDurationMillis() {
        return duration;
    }

    @Override
    public String toString() {
        final String comma = ", ";
        final String separate = " - ";
        return getMetaDataString("", getTitle())
            + getMetaDataString(comma, getArtist())
            + getMetaDataString(separate, getGenre())
            + getMetaDataString(comma, getAlbum())
            + getMetaDataString(comma, getDuration()) + "min";
    }

    private String getMetaDataString(final String separator, final String metadata) {
        return metadata.isEmpty() ? "" : separator + metadata;
    }

    private String millisToString(final long milliseconds) {
        DecimalFormat numberFormat = new DecimalFormat("00");
        final long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        final long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds - TimeUnit.MINUTES.toMillis(min));
        return milliseconds <= 0 ? "" : min + ":" + numberFormat.format(sec);

    }
}
