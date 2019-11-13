package de.techfak.gse.lwalkenhorst.terminalScanner;

import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.util.Scanner;

public class Terminal {

    private static final String SONG = "song";
    private static final String PLAYLIST = "playlist";
    private static final String EXIT = "exit";

    private static final int EXIT_CODE = 1;

    private final Scanner scanner;
    private final RadioModel radio;

    public Terminal(RadioModel radio) {
        this.radio = radio;
        this.scanner = new Scanner(System.in);
    }

    public void listenForInstructions() {
        while (true) {
            String command = scanner.nextLine();
            switch (command) {
                case SONG:
                    printSongInfo(radio.getCurrentPlayingSong());
                    break;
                case PLAYLIST:
                    printPlaylistInfo(radio.getCurrentPlaylist());
                    break;
                case EXIT:
                    System.exit(EXIT_CODE);
                    break;
                default:
                    System.out.println("Help msg");
            }
        }
    }

    public void printSongInfo(Song song) {
        System.out.println(SONG);
    }

    public void printPlaylistInfo(Playlist playlist) {
        System.out.println(PLAYLIST);
    }
}
