package de.techfak.gse.lwalkenhorst.radioplayer.song;

import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;

/**
 * Represents the metadata from a song.
 * Can only be initiated within package.
 * Loads metadata from given media object.
 */
public class MetaData {
    private String title;
    private String artist;
    private String album;
    private String genre;
    private long duration;

    MetaData() {
    }

    void loadDataFrom(Media media) {
        uk.co.caprica.vlcj.media.MetaData metaData = media.meta().asMetaData();
        InfoApi info = media.info();

        this.title = metaData.get(Meta.TITLE);
        this.artist = metaData.get(Meta.ARTIST);
        this.album = metaData.get(Meta.ALBUM);
        this.genre = metaData.get(Meta.GENRE);
        this.duration = info.duration();
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
