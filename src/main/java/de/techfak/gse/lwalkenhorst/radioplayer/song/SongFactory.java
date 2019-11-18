package de.techfak.gse.lwalkenhorst.radioplayer.song;

import de.techfak.gse.lwalkenhorst.cleanup.CleanUpDemon;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;

/**
 * Building new song instances.
 * Using MediaPlayerFactory form vlcj library to load metadata.
 */
public class SongFactory {

    private final MediaPlayerFactory mediaPlayerFactory;

    public SongFactory() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        CleanUpDemon.register(this, this.mediaPlayerFactory::release);
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
        MetaData metaData = new MetaData();
        Media media = loadMedia(file);
        metaData.loadDataFrom(media);
        media.release();
        Song song = new Song(file, metaData);

        // Printing song after reading metadata
        System.out.println(song.toString());
        return song;
    }
}
