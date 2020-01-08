package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.closeup.ObjectCloseupManager;
import de.techfak.gse.lwalkenhorst.exceptions.NoConnectionException;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.PlaylistFactory;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebClientTest {

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
    void requestSongServerOnlinePlaying() {
        try {
            //setting up musicPlayer
            Playlist testMusic = new PlaylistFactory(dir).newPlaylist(false);
            MusicPlayer musicPlayer = new MusicPlayer();
            musicPlayer.setPlaylist(testMusic);


            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.setMusicStream("8080");
            server.startTSPSocket();

            musicPlayer.start();

            //setting up client
            WebClient client = new WebClient("127.0.0.1", 8080);

            //request a song to test
            Song requestedSong = client.requestSong();
            Song actualSong = musicPlayer.getSong();

            //checking if songs would be treated as same 'song'
            assertThat(requestedSong).isEqualTo(actualSong);

            //checking if minimal information is equal
            assertThat(requestedSong.getUuid()).isEqualTo(actualSong.getUuid());
            assertThat(requestedSong.getTitle()).isEqualTo(actualSong.getTitle());
            assertThat(requestedSong.getArtist()).isEqualTo(actualSong.getArtist());
        } catch (NoMusicFileFoundException | NoConnectionException e) {
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
     * Testfall 2A: Server ist gestartet, der MusicPlayer spielt keine playlist.
     * Soll verhalten: Der Song der durch den request gebaut wird ist leer, aber nicht null oder undefiniert
     * - leerer Song
     * - song gleich
     */
    @Test
    void requestSongServerOnlineNotPlaying() {
        try {
            //setting up musicPlayer
            MusicPlayer musicPlayer = new MusicPlayer();

            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.setMusicStream("8080");
            server.startTSPSocket();

            //setting up client
            WebClient client = new WebClient("127.0.0.1", 8080);

            //request a song to test
            Song requestedSong = client.requestSong();
            Song actualSong = musicPlayer.getSong();

            //checking if songs would be treated as same 'song' (empty song)
            assertThat(requestedSong).isEqualTo(actualSong);
            assertThat(requestedSong.getUuid()).isEqualTo(actualSong.getUuid());

            //checking if no minimal information is given because there is no song played
            //information should be not null
            assertThat(requestedSong.getTitle()).isEqualTo(actualSong.getTitle()).isEqualTo("");
            assertThat(requestedSong.getArtist()).isEqualTo(actualSong.getArtist()).isEqualTo("");
        } catch (NoConnectionException e) {
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
    void requestSongServerOfflinePlaying() {
        try {
            //setting up musicPlayer
            Playlist testMusic = new PlaylistFactory(dir).newPlaylist(false);
            MusicPlayer musicPlayer = new MusicPlayer();
            musicPlayer.setPlaylist(testMusic);

            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.setMusicStream("8080");
            server.startTSPSocket();

            //setting up client
            WebClient client = new WebClient("127.0.0.1", 8080);

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
        } catch (NoMusicFileFoundException | NoConnectionException e) {
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
     * Testfall 2B: Server ist gestartet verliert jedoch die Verbindung, der MusicPlayer spielt keine playlist.
     * Soll verhalten: Der Song der durch den request gebaut wird ist leer und
     * nicht der gleiche wie der des musicPlayers, aber nicht null oder undefiniert
     * - leerer Song
     * - song nicht gleich
     */
    @Test
    void requestSongServerOfflineNotPlaying() {
        try {
            //setting up musicPlayer
            MusicPlayer musicPlayer = new MusicPlayer();

            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.setMusicStream("8080");
            server.startTSPSocket();

            //setting up client
            WebClient client = new WebClient("127.0.0.1", 8080);

            ObjectCloseupManager.getInstance().closeObject(server);

            //request a song to test
            Song requestedSong = client.requestSong();
            Song actualSong = musicPlayer.getSong();

            //checking if songs would be treated as same 'song' (empty song)
            assertThat(requestedSong).isNotEqualTo(actualSong);
            assertThat(requestedSong.getUuid()).isNotEqualTo(actualSong.getUuid());

            //checking if no minimal information is given because there is no song played
            //information should be not null
            assertThat(requestedSong.getTitle()).isEqualTo(actualSong.getTitle()).isEqualTo("");
            assertThat(requestedSong.getArtist()).isEqualTo(actualSong.getArtist()).isEqualTo("");
        } catch (NoConnectionException e) {
            e.printStackTrace();
        } finally {
            ObjectCloseupManager.getInstance().closeReferences();
        }
    }
}
