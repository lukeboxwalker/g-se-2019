package de.techfak.gse.lwalkenhorst.radioplayer;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;
import de.techfak.gse.lwalkenhorst.radioplayer.song.SongFactory;

import java.io.File;
import java.util.LinkedList;

/**
 * Reading the content of a directory.
 * Filtering all music files (mp3) in a given directory
 * and collect them in a List.
 */
public class MusicReader {

    private String directoryName;

    /**
     * Creates a new MusicReader to search for music files.
     * Prepare to search in the directory for mp3 files.
     * Using the current working directory when directoryName is null.
     *
     * @param directoryName the directory in which the reader will search.
     */
    public MusicReader(String directoryName) {
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
        File[] musicFiles = directory.listFiles((file, filename) -> filename.endsWith(".mp3"));
        if (musicFiles == null || musicFiles.length == 0) {
            throw new NoMusicFileFoundException("No mp3-files found in directory " + directory.getAbsolutePath());
        } else {
           return musicFiles;
        }
    }

    /**
     * Creates a list of songs.
     * Generate new song object via {@link SongFactory}
     *
     * @return the list of songs in the directory
     * @throws NoMusicFileFoundException when the directory is empty or does't exist
     */
    public LinkedList<Song> getSongs() throws NoMusicFileFoundException {
        SongFactory songFactory = new SongFactory();
        LinkedList<Song> songs = new LinkedList<>();
        for (File file : searchForMp3Files(new File(directoryName))) {
            songs.add(songFactory.newSong(file));
        }
        songFactory.clean();
        return songs;
    }
}
