package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFormatException;

import java.io.File;

public class Song {

    private File file;

    public Song(File file) throws NoMusicFileFormatException {
        if (file.getName().endsWith(".mp3")) {
            this.file = file;
        } else {
            throw new NoMusicFileFormatException("File :" + file.getAbsolutePath() + " is supposed to be an .mp3 File");
        }
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String getName() {
        return file.getName();
    }
}
