package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.closeup.ObjectCloseupManager;
import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import de.techfak.gse.lwalkenhorst.radioplayer.*;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class ClientSocketTest {

    private static final String dir = System.getProperty("user.dir") + "/src/test/resources/mp3s";

    /**
     * Test für User Story 13 #9200 Abfrage des aktuellen Songs des Servers.
     * Die Anfrage http://<IP>:<PORT>/current-song wird vom Server mit der Information über den aktuellen Song
     * im JSON format geantwortet, welcher wieder in einen Song überführt werden kann.
     *
     * Um den Fehler des übermitteln des falschen/fehlerhaften Song zu finden, wurde die Äquivalenzklasse
     * zum überprüfen der richtigen übermittlung gebildet.
     *
     * Als Repräsentant wurde der Webclient gewählt, um einen request zu senden und diesen zu überprüfen.
     *
     * Testfall 1A: Server ist gestartet und der MusicPlayer spielt die Playlist.
     * Soll verhalten: Der vom server gespielte Song ist der selbe welcher durch den request gebaut wird.
     * - selbe uuid
     * - sebler Titel
     * - selber Interpret/Artist
     * - song gleich
     */
    @Test
    public void requestSongServerOnline() {
        try {
            //setting up musicPlayer
            Playlist testMusic = new PlaylistFactory(dir).newPlaylist(false);
            MusicPlayer musicPlayer = new MusicPlayer();
            musicPlayer.setPlaylist(testMusic);


            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.streamMusic(8080);

            musicPlayer.start();

            //setting up client
            ClientSocket client = new ClientSocket("127.0.0.1", 8080, new StreamMusicPlayer());

            //request a song to test
            Song requestedSong = client.requestSong();
            Song actualSong = musicPlayer.getSong();

            //checking if songs would be treated as same 'song'
            assertThat(requestedSong).isEqualTo(actualSong);

            //checking if minimal information is equal
            assertThat(requestedSong.getUuid()).isEqualTo(actualSong.getUuid());
            assertThat(requestedSong.getTitle()).isEqualTo(actualSong.getTitle());
            assertThat(requestedSong.getArtist()).isEqualTo(actualSong.getArtist());
        } catch (ExitCodeException e) {
            e.printStackTrace();
        } finally {
            ObjectCloseupManager.getInstance().closeReferences();
        }
    }

    /**
     * Test für User Story 13 #9200 Abfrage des aktuellen Songs des Servers.
     * Die Anfrage http://<IP>:<PORT>/current-song wird vom Server mit der Information über den aktuellen Song
     * im JSON format geantwortet, welcher wieder in einen Song überführt werden kann.
     *
     * Um den Fehler des übermitteln des falschen/fehlerhaften Song zu finden, wurde die Äquivalenzklasse
     * zum überprüfen der richtigen übermittlung gebildet.
     *
     * Als Repräsentant wurde der Webclient gewählt, um einen request zu senden und diesen zu überprüfen.
     *
     * Testfall 2A: Server ist gestartet verliert jedoch die Verbindung, der MusicPlayer spielt die Playlist.
     * Soll verhalten: Der Song der durch den request gebaut wird ist leer, aber nicht null oder undefiniert
     * - leerer Song
     * - song nicht gleich
     */
    @Test
    public void requestSongServerOffline() {
        try {
            //setting up musicPlayer
            Playlist testMusic = new PlaylistFactory(dir).newPlaylist(false);
            MusicPlayer musicPlayer = new MusicPlayer();
            musicPlayer.setPlaylist(testMusic);

            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.streamMusic(8080);

            //setting up client
            ClientSocket client = new ClientSocket("127.0.0.1", 8080, new StreamMusicPlayer());

            ObjectCloseupManager.getInstance().closeObject(server);

            musicPlayer.start();

            //request a song to test
            Song requestedSong = client.requestSong();
            Song actualSong = musicPlayer.getSong();

            //checking if songs would be treated as same 'song'
            assertThat(requestedSong).isNotEqualTo(actualSong);
            assertThat(requestedSong.getUuid()).isNotEqualTo(actualSong.getUuid());

            //checking if no minimal information is given
            //information should be not null
            assertThat(requestedSong.getTitle()).isEqualTo("");
            assertThat(requestedSong.getArtist()).isEqualTo("");
        } catch (ExitCodeException e) {
            e.printStackTrace();
        } finally {
            ObjectCloseupManager.getInstance().closeReferences();
        }
    }

    /**
     * Test für User Story 15 #9203 Abfrage der aktuellen Playlist des Servers.
     * Die Anfrage http://<IP>:<PORT>/playlist wird vom Server mit der Information über die aktuelle
     * Playlist im JSON format beantwortet, welcher wieder in eine Playlist überführt werden kann.
     *
     * Um den Fehler des übermitteln der falschen/fehlerhaften Playlist zu finden, wurde die Äquivalenzklasse
     * zum überprüfen der richtigen übermittlung gebildet.
     *
     * Als Repräsentant wurde der Webclient gewählt, um einen request zu senden und diesen zu überprüfen.
     *
     * Testfall 1A: Server ist gestartet und der MusicPlayer spielt die Playlist.
     * Soll verhalten: Die vom server gespielte Playlist (Liste der Songs) ist der selbe,
     * welcher durch den request gebaut wird.
     * - selbe songlist (playlist)
     */
    @Test
    public void requestPlaylistServerOnline() {
        try {
            //setting up musicPlayer
            Playlist testMusic = new PlaylistFactory(dir).newPlaylist(false);
            MusicPlayer musicPlayer = new MusicPlayer();
            musicPlayer.setPlaylist(testMusic);


            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.streamMusic(8080);

            musicPlayer.start();

            //setting up client
            ClientSocket client = new ClientSocket("127.0.0.1", 8080, new StreamMusicPlayer());

            //request a song to test
            Playlist requestedPlaylist = client.requestPlaylist();

            //checking if songLists are equal
            assertThat(musicPlayer.getPlaylist().getSongList()).isEqualTo(requestedPlaylist.getSongList());
        } catch (ExitCodeException e) {
            e.printStackTrace();
        } finally {
            ObjectCloseupManager.getInstance().closeReferences();
        }
    }

    /**
     * Test für User Story 15 #9203 Abfrage der aktuellen Playlist des Servers.
     * Die Anfrage http://<IP>:<PORT>/playlist wird vom Server mit der Information über die aktuelle
     * Playlist im JSON format beantwortet, welcher wieder in eine Playlist überführt werden kann.
     *
     * Um den Fehler des übermitteln der falschen/fehlerhaften Playlist zu finden, wurde die Äquivalenzklasse
     * zum überprüfen der richtigen übermittlung gebildet.
     *
     * Als Repräsentant wurde der Webclient gewählt, um einen request zu senden und diesen zu überprüfen.
     *
     * Testfall 1A: Server ist gestartet  verliert jedoch die Verbindung, der MusicPlayer spielt die Playlist.
     * Soll verhalten: Die vom server gespielte Playlist (Liste der Songs) ist nicht dies selbe
     * welcher durch den request gebaut wird.
     * - leerer Playlist
     * - songLists nicht gleich
     */
    @Test
    public void requestPlaylistServerOffline() {
        try {
            //setting up musicPlayer
            Playlist testMusic = new PlaylistFactory(dir).newPlaylist(false);
            MusicPlayer musicPlayer = new MusicPlayer();
            musicPlayer.setPlaylist(testMusic);


            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.streamMusic(8080);

            musicPlayer.start();

            //setting up client
            ClientSocket client = new ClientSocket("127.0.0.1", 8080, new StreamMusicPlayer());

            ObjectCloseupManager.getInstance().closeObject(server);

            //request a song to test
            Playlist requestedPlaylist = client.requestPlaylist();

            //checking if songLists are equal
            assertThat(musicPlayer.getPlaylist().getSongList()).isNotEqualTo(requestedPlaylist.getSongList());
            assertThat(requestedPlaylist.getSongList().size()).isEqualTo(0);
        } catch (ExitCodeException e) {
            e.printStackTrace();
        } finally {
            ObjectCloseupManager.getInstance().closeReferences();
        }
    }
}
