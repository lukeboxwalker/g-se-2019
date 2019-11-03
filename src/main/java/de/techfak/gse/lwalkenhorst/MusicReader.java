package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFormatException;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicReader {

    private String directoryName;

    public MusicReader(String directoryName) {
        this.directoryName = directoryName != null ? directoryName : System.getProperty("user.dir");
    }

    /**
     * Searches through the directory for '.mp3' files.
     *
     * @param directory the directory in with the method searches.
     */
    private List<Song> searchForMp3Files(final File directory) throws NoMusicFileFoundException {
        File[] musicFiles = directory.listFiles((file, filename) -> filename.endsWith(".mp3"));
        if (musicFiles == null || musicFiles.length == 0) {
            throw new NoMusicFileFoundException("No mp3-files found in directory " + directory.getAbsolutePath());
        } else {
            List<Song> songs = new ArrayList<>();
            for (File file : musicFiles) {
                try {
                    Song song = new Song(file);
                    songs.add(song);
                } catch (NoMusicFileFormatException e) {
                    System.err.println(e.getMessage());
                }
            }
            return songs;
        }
    }

    public List<Song> getSongs() throws NoMusicFileFoundException {
        return searchForMp3Files(new File(directoryName));
    }
}
