package de.techfak.gse.lwalkenhorst.radioplayer.song;

import java.io.File;

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

}
