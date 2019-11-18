package de.techfak.gse.lwalkenhorst.radioplayer.song;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Represents a mp3 format Song.
 * The Song saves all metadata available form a loaded media,
 * as well as its complete file.
 */
public class Song {

    private File file;
    private MetaData metaData;

    /**
     * Creates a new Song from given file.
     *
     * @param file     the mp3 the song is loaded from.
     * @param metaData to have access to the metadata.
     */
    public Song(File file, MetaData metaData) {
        this.file = file;
        this.metaData = metaData;
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public String toString() {
        final String comma = ", ";
        final String separate = " - ";
        return getMetaDataString("", metaData.getTitle())
            + getMetaDataString(comma, metaData.getArtist())
            + getMetaDataString(separate, metaData.getGenre())
            + getMetaDataString(comma, metaData.getAlbum())
            + getMetaDataString(comma, metaData.getGenre())
            + getMetaDataString(comma, metaData.getDuration());
    }

    private String getMetaDataString(String tag, String metadata) {
        return metadata == null ? "" : tag + metadata;
    }

    private String getMetaDataString(String tag, long milliseconds) {
        long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds - TimeUnit.MINUTES.toMillis(min));
        return milliseconds <= 0 ? "" : tag + min + ":" + sec + " min";
    }
}
