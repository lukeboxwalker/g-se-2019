package de.techfak.gse.lwalkenhorst.radioplayer.song;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import de.techfak.gse.lwalkenhorst.cleanup.NoCleanUpFoundException;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;

/**
 * Building new song instances.
 * Using MediaPlayerFactory form vlcj library to load metadata.
 * Registers its cleanup to {@link CleanUpDemon}
 */
public class SongFactory implements AutoCloseable {

    private final MediaPlayerFactory mediaPlayerFactory;

    public SongFactory() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        CleanUpDemon.register(this, mediaPlayerFactory::release);
    }

    /**
     * Loading a media by its file path.
     * The Media should be prepared to read its metadata or additional information.
     *
     * @param file the media file to load
     * @return the loaded media file
     */
    private Media loadMedia(File file) {
        final Media media = mediaPlayerFactory.media().newMedia(file.getAbsolutePath());
        final ParsedWaiter parsed = new ParsedWaiter(media) {
            @Override
            protected boolean onBefore(final Media component) {
                return media.parsing().parse();
            }
        };
        try {
            parsed.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return media;
    }

    /**
     * Creating a new Song object.
     * Using the file to load the metadata from the song
     * with {@link #loadMedia(File)} and releasing the medias memory afterwards.
     * Inits a new song with loaded metadata.
     *
     * @param file to read the song.
     * @return a new Song object from given file.
     */
    public Song newSong(File file) {
        Media media = loadMedia(file);
        MetaData metaData = media.meta().asMetaData();

        final String rawTitle = metaData.get(Meta.TITLE);
        final String rawArtist = metaData.get(Meta.ARTIST);
        final String rawAlbum = metaData.get(Meta.ALBUM);
        final String rawGenre = metaData.get(Meta.GENRE);

        final String title = rawTitle == null ? "" : rawTitle;
        final String artist = rawArtist == null ? "" : rawArtist;
        final String album = rawAlbum == null ? "" : rawAlbum;
        final String genre = rawGenre == null ? "" : rawGenre;
        final long duration = media.info().duration();

        media.release();
        Song song = new Song(file, title, artist, album, genre, duration);
        // Printing song after reading metadata
        System.out.println(song.toString());
        return song;
    }

    @Override
    public void close() throws NoCleanUpFoundException {
        CleanUpDemon.cleanup(this);
    }
}
