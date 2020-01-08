package de.techfak.gse.lwalkenhorst.server;

import de.techfak.gse.lwalkenhorst.closeup.ObjectCloseupManager;
import de.techfak.gse.lwalkenhorst.exceptions.NoConnectionException;
import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.PlaylistFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WebServerTest {

    private static final String dir = System.getProperty("user.dir") + "/src/test/resources/mp3s";

    /**
     * Test für User Story 13 #9200 Abfrage des aktuellen Songs des Servers.
     * Die Anfrage http://<IP>:<PORT>/current-song wird vom Server mit dem Status Code 200 (Ok) und
     * der Information über den aktuellen Song im JSON format antwortet.
     * <p>
     * Um den Fehler eines falschen StatusCodes oder eines falschen response types zu finden, wurde die Äquivalenzklasse
     * zum überprüfen richtigen Kodierung und des StatusCodes gebildet.
     * <p>
     * Als Repräsentant wurde der WebServer gewählt, um eine korrekte Bereitstellung zu überprüfen
     * <p>
     * Testfall 1A: Server ist gestartet und der MusicPlayer spielt die Playlist.
     * Soll verhalten: Die Antwort des Server ist ein statusCode 200 (Ok) und ein JSON String
     * - status code 200
     * - mimeType application/json
     * - body not empty
     */
    @Test
    public void requestSongStatusCodeMimeTypePlaying() {
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

            //request current song
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://127.0.0.1:8080/current-song")).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            //check if status code is 200
            assertThat(response.statusCode()).isEqualTo(200);

            //check if body is not empty
            assertThat(response.body()).isNotEqualTo("");

            //check if header mime type is set to application/json (json string)
            List<String> mimeTypes = response.headers().allValues("content-type");
            assertThat(mimeTypes.size()).isEqualTo(1);
            assertThat(mimeTypes.get(0)).isEqualTo("application/json");
        } catch (NoMusicFileFoundException | NoConnectionException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            ObjectCloseupManager.getInstance().closeReferences();
        }
    }

    /**
     * Test für User Story 13 #9200 Abfrage des aktuellen Songs des Servers.
     * Die Anfrage http://<IP>:<PORT>/current-song wird vom Server mit dem Status Code 200 (Ok) und
     * der Information über den aktuellen Song im JSON format antwortet.
     * <p>
     * Um den Fehler eines falschen StatusCodes oder eines falschen response types zu finden, wurde die Äquivalenzklasse
     * zum überprüfen richtigen Kodierung und des StatusCodes gebildet.
     * <p>
     * Als Repräsentant wurde der WebServer gewählt, um eine korrekte Bereitstellung zu überprüfen
     * <p>
     * Testfall 2A: Server ist gestartet, der MusicPlayer spielt keine playlist.
     * Soll verhalten: Die Antwort des Server ist ein statusCode 200 (Ok) und ein JSON String
     * - status code 200
     * - mimeType application/json
     * - body not empty
     */
    @Test
    public void requestSongStatusCodeMimeTypeNotPlaying() {
        try {
            //setting up musicPlayer
            MusicPlayer musicPlayer = new MusicPlayer();

            //setting up server
            WebServer server = new WebServer(8080, musicPlayer);
            server.setMusicStream("8080");
            server.startTSPSocket();

            //request current song
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://127.0.0.1:8080/current-song")).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            //check if status code is 200
            assertThat(response.statusCode()).isEqualTo(200);

            //check if body is not empty
            assertThat(response.body()).isNotEqualTo("");

            //check if header mime type is set to application/json (json string)
            List<String> mimeTypes = response.headers().allValues("content-type");
            assertThat(mimeTypes.size()).isEqualTo(1);
            assertThat(mimeTypes.get(0)).isEqualTo("application/json");
        } catch (NoConnectionException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            ObjectCloseupManager.getInstance().closeReferences();
        }
    }

}
