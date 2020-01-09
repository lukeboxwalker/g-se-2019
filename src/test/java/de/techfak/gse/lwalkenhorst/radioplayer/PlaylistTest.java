package de.techfak.gse.lwalkenhorst.radioplayer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlaylistTest {

    /**
     * Test für User Story 15 #9203 Abfrage der aktuellen Playlist des Servers.
     * Die Anfrage http://<IP>:<PORT>/playlist wird vom Server mit dem Status Code 200 (Ok) und
     * der Information über die aktuelle Playlist im JSON format beantwortet.
     * <p>
     * Um den Fehler einer zu Langen Playlist zu finden, wurde die Äquivalenzklasse
     * zum überprüfen der playlist Länge gebildet.
     * <p>
     * Als Repräsentant wurde die Playlist gewählt, um die korrekte Länge der liste zu prüfen.
     * <p>
     * Testfall 1A: Playlist ist nicht länger als 100 Einträge
     * Soll verhalten: Eine Liste mit gleicher länge und gleichen Einträgen.
     * - gleiche Länge
     * - gleiche Einträge
     */
    @Test
    public void getSongListSizeBelowBoundary() {
        //creating songs
        List<Song> songsList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            songsList.add(new Song());
        }

        //init playlist
        Playlist playlist = new Playlist();
        playlist.setSongList(songsList);

        //get song list from playlist
        List<Song> playlistSongsList = playlist.getSongList();

        //checking size
        assertThat(playlistSongsList.size()).isEqualTo(songsList.size());
        assertThat(playlistSongsList.size()).isLessThanOrEqualTo(100);

        //checking elements
        assertThat(playlistSongsList).isEqualTo(songsList);
    }

    /**
     * Test für User Story 15 #9203 Abfrage der aktuellen Playlist des Servers.
     * Die Anfrage http://<IP>:<PORT>/playlist wird vom Server mit dem Status Code 200 (Ok) und
     * der Information über die aktuelle Playlist im JSON format beantwortet.
     * <p>
     * Um den Fehler einer zu Langen Playlist zu finden, wurde die Äquivalenzklasse
     * zum überprüfen der playlist Länge gebildet.
     * <p>
     * Als Repräsentant wurde die Playlist gewählt, um die korrekte Länge der liste zu prüfen.
     * <p>
     * Testfall 2A: Playlist ist länger als 100 Einträge
     * Soll verhalten: Eine Liste mit gleicher länge und gleichen Einträgen.
     * - ungleiche Länge
     * - nur die ersten 100 Einträge gleich
     */
    @Test
    public void getSongListSizeAboveBoundary() {
        //creating songs
        List<Song> songsList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            songsList.add(new Song());
        }

        //init playlist
        Playlist playlist = new Playlist();
        playlist.setSongList(songsList);

        //get song list from playlist
        List<Song> playlistSongsList = playlist.getSongList();

        //checking size
        assertThat(playlistSongsList.size()).isNotEqualTo(songsList.size());
        assertThat(playlistSongsList.size()).isEqualTo(100);

        //checking elements
        assertThat(playlistSongsList).isEqualTo(songsList.subList(0, 100));
    }

    /**
     * Test für User Story 15 #9203 Abfrage der aktuellen Playlist des Servers.
     * Die Anfrage http://<IP>:<PORT>/playlist wird vom Server mit dem Status Code 200 (Ok) und
     * der Information über die aktuelle Playlist im JSON format beantwortet.
     * <p>
     * Um den Fehler einer zu Langen Playlist zu finden, wurde die Äquivalenzklasse
     * zum überprüfen der playlist Länge gebildet.
     * <p>
     * Als Repräsentant wurde die Playlist gewählt, um die korrekte Länge der liste zu prüfen.
     * <p>
     * Testfall 3A: Playlist ist genau 100 Einträge lang.
     * Soll verhalten: Eine Liste mit gleicher länge und gleichen Einträgen.
     * - gleiche Länge
     * - gleiche Einträge
     */
    @Test
    public void getSongListSizeEqualsBoundary() {
        //creating songs
        List<Song> songsList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            songsList.add(new Song());
        }

        //init playlist
        Playlist playlist = new Playlist();
        playlist.setSongList(songsList);

        //get song list from playlist
        List<Song> playlistSongsList = playlist.getSongList();

        //checking size
        assertThat(playlistSongsList.size()).isEqualTo(songsList.size());
        assertThat(playlistSongsList.size()).isEqualTo(100);

        //checking elements
        assertThat(playlistSongsList).isEqualTo(songsList);
    }
}