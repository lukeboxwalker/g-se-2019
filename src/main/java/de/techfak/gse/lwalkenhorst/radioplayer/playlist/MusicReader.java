package de.techfak.gse.lwalkenhorst.radioplayer.playlist;

import de.techfak.gse.lwalkenhorst.cleanup.NoCleanUpFoundException;
import de.techfak.gse.lwalkenhorst.exceptions.FileNotLoadableException;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;
import de.techfak.gse.lwalkenhorst.radioplayer.song.SongFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
     * @throws NoMusicFileFoundException when the directory is empty or doesn't exist
     */
    public List<Song> getSongs() throws NoMusicFileFoundException {
        List<Song> songs = new ArrayList<>();
        try (SongFactory songFactory = new SongFactory()) {
            for (File file : searchForMp3Files(new File(directoryName))) {
                try {
                    songs.add(songFactory.newSong(file));
                } catch (FileNotLoadableException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (NoCleanUpFoundException e) {
            e.printStackTrace();
        }
        return songs;
    }
}
