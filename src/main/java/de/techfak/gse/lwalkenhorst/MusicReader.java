package de.techfak.gse.lwalkenhorst;

import java.io.File;

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
    private File[] searchForMp3Files(final File directory) throws NoMusicFileFoundException {
        File[] musicFiles;
        musicFiles = directory.listFiles((file, filename) -> filename.endsWith(".mp3"));
        if (musicFiles == null || musicFiles.length == 0) {
            throw new NoMusicFileFoundException("No mp3-files found in directory " + directory.getAbsolutePath());
        } else {
            return musicFiles;
        }
    }

    public File[] getMusicFiles() throws NoMusicFileFoundException {
        return searchForMp3Files(new File(directoryName));
    }
}
