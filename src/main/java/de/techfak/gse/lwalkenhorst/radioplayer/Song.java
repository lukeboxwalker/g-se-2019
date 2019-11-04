package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.apiwrapper.MediaLoader;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFormatException;
import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;

import java.io.File;

public class Song {

    private File file;
    private Media media;

    private String title;
    private String artist;
    private String album;
    private String genre;
    private long duration;

    public Song(File file, MediaLoader mediaLoader) throws NoMusicFileFormatException {
        if (file.getName().endsWith(".mp3")) {
            this.file = file;
            this.media = mediaLoader.loadMedia(file);
            initMetaData(media.meta().asMetaData(), media.info());
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

    public Media getMedia() {
        return media;
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
