package de.techfak.gse.lwalkenhorst.radioplayer;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;

public class SongFactory {

    private MediaPlayerFactory mediaPlayerFactory;

    public SongFactory() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
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

    public Song newSong(File file) {
        return new Song(file, this::loadMedia);
    }

    public void release() {
        this.mediaPlayerFactory.release();
    }
}
