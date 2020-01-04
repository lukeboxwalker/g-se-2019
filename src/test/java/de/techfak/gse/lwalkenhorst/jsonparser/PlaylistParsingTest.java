package de.techfak.gse.lwalkenhorst.jsonparser;

import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaylistParsingTest {

    @Test
    public void playlistToJSONTest() {
        JSONParser parser = new JSONParser();
        List<Song> songList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Song song = new Song();
            song.setTitle("TestSong" + i);
            song.setArtist("TestInterpret" + i);
            songList.add(song);
        }

        Playlist playlist = new Playlist();
        playlist.setSongList(songList);
        try {
            String json = parser.toJSON(playlist);
            assertThat(json).isNotNull().isNotEmpty();
        } catch (SerialisationException e) {
            assertThat(e).isNull();
        }
    }

    @Test
    public void JSONToPlaylistTest() {
        JSONParser parser = new JSONParser();
        List<Song> songList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Song song = new Song();
            song.setTitle("TestSong" + i);
            song.setArtist("TestInterpret" + i);
            songList.add(song);
        }

        Playlist playlist = new Playlist();
        playlist.setSongList(songList);
        try {
            String json = parser.toJSON(playlist);
            assertThat(json).isNotNull().isNotEmpty();

            Playlist playlistFromJson = parser.parseJSON(json, Playlist.class);
            assertThat(playlistFromJson.getSongs()).isEqualTo(playlist.getSongList());
            assertThat(playlistFromJson.getSongs().size()).isLessThanOrEqualTo(100);
        } catch (SerialisationException e) {
            assertThat(e).isNull();
        }
    }

    @Test
    public void playlistLimitTest() {
        JSONParser parser = new JSONParser();
        List<Song> songList = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            Song song = new Song();
            song.setTitle("TestSong" + i);
            song.setArtist("TestInterpret" + i);
            songList.add(song);
        }

        Playlist playlist = new Playlist();
        playlist.setSongList(songList);
        try {
            String json = parser.toJSON(playlist);
            assertThat(json).isNotNull().isNotEmpty();

            Playlist playlistFromJson = parser.parseJSON(json, Playlist.class);
            assertThat(playlistFromJson.getSongs()).isEqualTo(playlist.getSongList());
            assertThat(playlistFromJson.getSongs().size()).isLessThanOrEqualTo(100);
        } catch (SerialisationException e) {
            assertThat(e).isNull();
        }
    }
}
