package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.apiwrapper.MediaLoader;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFormatException;
import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;

import java.io.File;

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
     * @param file        the mp3 the song is loaded from.
     * @param mediaLoader to load the metadata form the file {@link MediaLoader}.
     * @throws NoMusicFileFormatException when the given file is no mp3 file.
     */
    public Song(File file, MediaLoader mediaLoader) throws NoMusicFileFormatException {
        if (file.getName().endsWith(".mp3")) {
            this.file = file;
            Media media = mediaLoader.loadMedia(file);
            initMetaData(media.meta().asMetaData(), media.info());
            media.release();
        } else {
            throw new NoMusicFileFormatException("File :" + file.getAbsolutePath() + " is supposed to be an .mp3 File");
        }
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
