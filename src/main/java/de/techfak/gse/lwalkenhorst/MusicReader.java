package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;

import java.io.File;
import java.util.Arrays;
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
    private List<File> searchForMp3Files(final File directory) throws NoMusicFileFoundException {
        File[] musicFiles;
        musicFiles = directory.listFiles((file, filename) -> filename.endsWith(".mp3"));
        if (musicFiles == null || musicFiles.length == 0) {
            throw new NoMusicFileFoundException("No mp3-files found in directory " + directory.getAbsolutePath());
        } else {
            return Arrays.asList(musicFiles);
        }
    }

    public List<File> getMusicFiles() throws NoMusicFileFoundException {
        return searchForMp3Files(new File(directoryName));
    }
}
