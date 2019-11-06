package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.apiwrapper.MediaLoader;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFormatException;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;

import java.io.File;
import java.util.LinkedList;

/**
 * Reading the content of a directory.
 * Filtering all music files (mp3) in a given directory
 * and collect them in a List.
 */
public class MusicReader {

    private String directoryName;
    private MediaLoader mediaLoader;

    /**
     * Creates a new MusicReader to search for music files.
     * Prepare to search in the directory for mp3 files.
     * Using the current working directory when directoryName is null.
     *
     * @param directoryName the directory in which the reader will search.
     * @param mediaLoader   to load the metadata form the file {@link MediaLoader}.
     */
    public MusicReader(String directoryName, MediaLoader mediaLoader) {
        this.directoryName = directoryName == null ? System.getProperty("user.dir") : directoryName;
        this.mediaLoader = mediaLoader;
    }

    /**
     * Searches through the directory for '.mp3' files.
     *
     * @param directory the directory in which the method searches
     * @return the list of songs in the directory
     * @throws NoMusicFileFoundException when the directory is empty or does't exist
     */
    private LinkedList<Song> searchForMp3Files(final File directory) throws NoMusicFileFoundException {
        File[] musicFiles = directory.listFiles((file, filename) -> filename.endsWith(".mp3"));
        if (musicFiles == null || musicFiles.length == 0) {
            throw new NoMusicFileFoundException("No mp3-files found in directory " + directory.getAbsolutePath());
        } else {
            LinkedList<Song> songs = new LinkedList<>();
            for (File file : musicFiles) {
                try {
                    songs.add(new Song(file, mediaLoader));
                } catch (NoMusicFileFormatException e) {
                    System.err.println(e.getMessage());
                }
            }
            return songs;
        }
    }

    public LinkedList<Song> getSongs() throws NoMusicFileFoundException {
        return searchForMp3Files(new File(directoryName));
    }
}
