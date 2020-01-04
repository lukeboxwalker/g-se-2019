package de.techfak.gse.lwalkenhorst.radioplayer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Represents a mp3 format Song.
 * The Song saves all metadata available form a loaded media
 */
public class Song {

    private String filePath;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String artWorkURL;
    private String duration;
    private long durationMillis;

    private String uuid = UUID.randomUUID().toString();

    /**
     * Creating a new empty Song.
     */
    public Song() {
        this.filePath = "";
        this.title = "";
        this.artist = "";
        this.album = "";
        this.genre = "";
        this.artWorkURL = "";
        this.duration = "";
        this.durationMillis = 0;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonIgnore
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @JsonIgnore
    public String getDuration() {
        return duration;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    /**
     * Setting formatted duration.
     *
     * @param duration in millis
     */
    public void setDurationMillis(long duration) {
        this.durationMillis = duration;
        final DecimalFormat numberFormat = new DecimalFormat("00");
        final long min = TimeUnit.MILLISECONDS.toMinutes(durationMillis);
        final long sec = TimeUnit.MILLISECONDS.toSeconds(durationMillis - TimeUnit.MINUTES.toMillis(min));
        this.duration = durationMillis <= 0 ? "" : min + ":" + numberFormat.format(sec);
    }

    @JsonIgnore
    public String getArtWorkURL() {
        return artWorkURL;
    }

    public void setArtWorkURL(String artWorkURL) {
        this.artWorkURL = artWorkURL;
    }

    @Override
    public String toString() {
        final String comma = ", ";
        final String separate = " - ";
        return toMetaDataString("", getTitle())
            + toMetaDataString(comma, getArtist())
            + toMetaDataString(separate, getGenre())
            + toMetaDataString(comma, getAlbum())
            + toMetaDataString(comma, getDuration()) + "min";
    }

    private String toMetaDataString(final String separator, final String metadata) {
        return metadata.isEmpty() ? "" : separator + metadata;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song) {
            return this == obj || this.hashCode() == obj.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
}
