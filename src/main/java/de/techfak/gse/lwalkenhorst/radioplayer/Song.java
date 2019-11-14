package de.techfak.gse.lwalkenhorst.radioplayer;

import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;

import java.io.File;
import java.util.function.Function;

/**
 * Represents a mp3 format Song.
 * The Song saves all metadata available form a loaded media,
 * as well as its complete file.
 */
public class Song {

    private File file;

    private String title;
    private String artist;
    private String album;
    private String genre;
    private long duration;

    /**
     * Creates a new Song from given file.
     * Loading the media to read {@link #initMetaData(MetaData metaData, InfoApi info)} its metadata,
     * finally releases the memory used for loading the media.
     *
     * @param file  the mp3 the song is loaded from.
     * @param media to load the metadata.
     */
    public Song(File file, Media media) {
        this.file = file;
        initMetaData(media.meta().asMetaData(), media.info());
        media.release();
    }

    private void initMetaData(MetaData metaData, InfoApi info) {
        this.title = metaData.get(Meta.TITLE);
        this.artist = metaData.get(Meta.ARTIST);
        this.album = metaData.get(Meta.ALBUM);
        this.genre = metaData.get(Meta.GENRE);
        this.duration = info.duration();
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

    public long getDuration() {
        return duration;
    }
}
