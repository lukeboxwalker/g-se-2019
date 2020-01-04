package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.cleanup.NoCleanUpFoundException;
import de.techfak.gse.lwalkenhorst.exceptions.FileNotLoadableException;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.waiter.UnexpectedWaiterErrorException;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * PlaylistFactory to create new Playlists form given directory.
 */
public class PlaylistFactory {

    private static final Pattern URL_QUICK_MATCH = Pattern.compile("^\\p{Alpha}[\\p{Alnum}+.-]*:.*$");
    private static final String FALLBACK_URL = "file:src/main/resources/view/fallback.png";
    private static final String EXCEPTION_MESSAGE = "No mp3-files found in directory ";

    private String directoryName;

    /**
     * Factory to create a new Playlist from given directory.
     *
     * @param directoryName the directory in which the mp3 file will be searched.
     */
    public PlaylistFactory(final String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * Searches through the directory for '.mp3' files.
     *
     * @param directory the directory in which the method searches
     * @return an array of mp3 files in the directory
     * @throws NoMusicFileFoundException when the directory is empty or does't exist
     */
    private File[] searchForMp3Files(final File directory) throws NoMusicFileFoundException {
        final File[] musicFiles = directory.listFiles((file, filename) -> filename.endsWith(".mp3"));
        if (musicFiles == null || musicFiles.length == 0) {
            throw new NoMusicFileFoundException(EXCEPTION_MESSAGE + directory.getAbsolutePath());
        } else {
            return musicFiles;
        }
    }

    /**
     * Creates a new Playlist object.
     * Using factory method {@link #newSong(File, MediaPlayerFactory)} to create songs.
     * Inits a MediaPlayerFactory to read Metadata
     *
     * @return a new Playlist
     * @throws NoMusicFileFoundException when playlist is empty
     */
    public Playlist newPlaylist() throws NoMusicFileFoundException {
        final List<Song> songs = new ArrayList<>();
        final File dir = new File(directoryName);
        try (VLCJFactory factory = new VLCJFactory()) {
            final MediaPlayerFactory mediaPlayerFactory = factory.newMediaPlayerFactory();
            for (final File file : searchForMp3Files(dir)) {
                try {
                    songs.add(newSong(file, mediaPlayerFactory));
                } catch (FileNotLoadableException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (NoCleanUpFoundException e) {
            e.printStackTrace();
        }
        if (songs.isEmpty()) {
            throw new NoMusicFileFoundException(EXCEPTION_MESSAGE + dir.getAbsolutePath());
        }
        Playlist playlist = new Playlist();
        playlist.setSongList(songs);
        return playlist;
    }

    /**
     * Loading a media by its file path.
     * The Media should be prepared to read its metadata or additional information.
     *
     * @param file    the media file to load
     * @param factory to create new media objects.
     * @return the loaded media file
     * @throws FileNotLoadableException when given file could not be loaded as a mp3 media.
     */
    private Media loadMedia(final File file, final MediaPlayerFactory factory) throws FileNotLoadableException {
        final Media media = factory.media().newMedia(file.getAbsolutePath());
        final ParsedWaiter parsed = new ParsedWaiter(media) {
            @Override
            protected boolean onBefore(final Media component) {
                return media.parsing().parse();
            }
        };
        try {
            parsed.await();
        } catch (InterruptedException | UnexpectedWaiterErrorException cause) {
            throw new FileNotLoadableException("Could not load media: " + file.getAbsolutePath(), cause);
        }
        return media;
    }

    /**
     * Creating a new Song object.
     * Using the file to load the metadata from the song
     * with {@link #loadMedia(File, MediaPlayerFactory)} and releasing the medias memory afterwards.
     * Inits a new song with loaded metadata.
     *
     * @param file    to read the song.
     * @param factory to create new media objects.
     * @return a new Song object from given file.
     * @throws FileNotLoadableException when given file could not be loaded as a mp3 media.
     */
    public Song newSong(final File file, final MediaPlayerFactory factory) throws FileNotLoadableException {
        final Media media = loadMedia(file, factory);
        final MetaData metaData = media.meta().asMetaData();

        final String rawTitle = metaData.get(Meta.TITLE);
        final String rawArtist = metaData.get(Meta.ARTIST);
        final String rawAlbum = metaData.get(Meta.ALBUM);
        final String rawGenre = metaData.get(Meta.GENRE);
        final String rawArtWorkURL = metaData.get(Meta.ARTWORK_URL);

        final String title = rawTitle == null ? "" : rawTitle;
        final String artist = rawArtist == null ? "" : rawArtist;
        final String album = rawAlbum == null ? "" : rawAlbum;
        final String genre = rawGenre == null ? "" : rawGenre;
        final String artWorldURL = validateUrl(rawArtWorkURL);
        final long duration = media.info().duration();

        media.release();
        final Song song = new Song();
        song.setFilePath(file.getAbsolutePath());
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setGenre(genre);
        song.setArtWorkURL(artWorldURL);
        song.setDurationMillis(duration);

        // Printing song after reading metadata
        System.out.println(song.toString());
        return song;
    }

    /**
     * Checks if a given imageUrl exists.
     *
     * @param url to check
     * @return the url if valid otherwise returns fallback url
     */
    private static String validateUrl(final String url) {
        final char separate = '/';
        if (url == null || url.isEmpty()) {
            return FALLBACK_URL;
        } else {
            try {
                if (URL_QUICK_MATCH.matcher(url).matches()) {
                    return new URL(url).toString();
                } else {
                    final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                    URL resource;
                    if (url.charAt(0) == separate) {
                        resource = contextClassLoader.getResource(url.substring(1));
                    } else {
                        resource = contextClassLoader.getResource(url);
                    }

                    if (resource == null) {
                        throw new IllegalArgumentException("Invalid URL or resource not found");
                    } else {
                        return resource.toString();
                    }
                }
            } catch (IllegalArgumentException | MalformedURLException e) {
                return FALLBACK_URL;
            }
        }
    }
}
