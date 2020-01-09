package de.techfak.gse.lwalkenhorst.jsonparser;

import de.techfak.gse.lwalkenhorst.exceptions.NoMusicFileFoundException;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.PlaylistFactory;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JSONParserTest {

    private static final String dir = System.getProperty("user.dir") + "/src/test/resources/mp3s";

    /**
     * Test für User Story 13 #9200 Abfrage des aktuellen Songs des Servers.
     * Die Anfrage http://<IP>:<PORT>/current-song wird vom Server mit der Information über den aktuellen Song
     * im JSON format geantwortet welcher über den JSONParser gebaut wird.
     *
     * Um den Fehler der falschen Serialisierung/Deserialisierung zu finden, wurde die Äquivalenzklasse
     * zum überprüfen korrekten Serialisierung/Deserialisierung gebildet.
     *
     * Als Repräsentant wurde der JSONParser gewählt, um die korrekten Serialisierung/Deserialisierung zu prüfen.
     *
     * Testfall 1A: Ein eingelesener Song wird serialisiert und wieder deserialisiert
     * Soll verhalten: Der Song wird als der selbe song betrachtet.
     * - song gleich
     * - alle metadaten gleich
     */
    @Test
    public void SongSerialisation() {
        try {
            //read in songs
            Playlist testMusic = new PlaylistFactory(dir).newPlaylist(false);
            Song actualSong = testMusic.getSongs().get(0);

            //setup parser
            JSONParser parser = new JSONParser();
            String json = parser.toJSON(actualSong);

            Song songFromJson = parser.parseJSON(json, Song.class);

            //checking if songs would be treated as same 'song'
            assertThat(songFromJson).isEqualTo(actualSong);

            //check song information is equal
            assertThat(songFromJson.getUuid()).isEqualTo(actualSong.getUuid());
            assertThat(songFromJson.getTitle()).isEqualTo(actualSong.getTitle());
            assertThat(songFromJson.getArtist()).isEqualTo(actualSong.getArtist());
            assertThat(songFromJson.getAlbum()).isEqualTo(actualSong.getAlbum());
            assertThat(songFromJson.getGenre()).isEqualTo(actualSong.getGenre());
            assertThat(songFromJson.getDurationMillis()).isEqualTo(actualSong.getDurationMillis());
        } catch (NoMusicFileFoundException | SerialisationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test für User Story 15 #9203 Abfrage der aktuellen Playlist des Servers.
     * Die Anfrage http://<IP>:<PORT>/playlist wird vom Server mit der Information über die aktuelle Playlist
     * im JSON format geantwortet welcher über den JSONParser gebaut wird.
     * <p>
     * Um den Fehler der falschen Serialisierung/Deserialisierung zu finden, wurde die Äquivalenzklasse
     * zum überprüfen korrekten Serialisierung/Deserialisierung gebildet.
     *
     * Als Repräsentant wurde der JSONParser gewählt, um die korrekten Serialisierung/Deserialisierung zu prüfen.
     *
     * Testfall 1A: Eine eingelesene Playlist wird serialisiert und wieder deserialisiert
     * Soll verhalten: Die liste der Songs in der Playlist wird als die selbe liste betrachtet.
     * - songlists sind identisch
     */
    @Test
    public void PlaylistSerialisation() {
        try {
            //read in songs
            Playlist actualPlaylist = new PlaylistFactory(dir).newPlaylist(false);
            List<Song> actualSongs = actualPlaylist.getSongList();

            //setup parser
            JSONParser parser = new JSONParser();
            String json = parser.toJSON(actualPlaylist);

            Playlist playlistFromJson = parser.parseJSON(json, Playlist.class);
            List<Song> songsFromJson = playlistFromJson.getSongList();

            //checking if song list would be treated as same list of songs
            assertThat(actualSongs).isEqualTo(songsFromJson);
        } catch (NoMusicFileFoundException | SerialisationException e) {
            e.printStackTrace();
        }
    }

}
