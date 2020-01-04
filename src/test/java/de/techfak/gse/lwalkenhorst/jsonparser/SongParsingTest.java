package de.techfak.gse.lwalkenhorst.jsonparser;

import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SongParsingTest {

    @Test
    public void songToJSONTest() {
        JSONParser parser = new JSONParser();
        Song song = new Song();
        try {
            String json = parser.toJSON(song);
            assertThat(json).isNotNull().isNotEmpty();
        } catch (SerialisationException e) {
            assertThat(e).isNull();
        }
    }

    @Test
    public void JSONToSongTest() {
        JSONParser parser = new JSONParser();
        Song song = new Song();
        try {
            String json = parser.toJSON(song);
            assertThat(json).isNotNull().isNotEmpty();

            Song songFromJson = parser.parseJSON(json, Song.class);
            assertThat(songFromJson).isEqualTo(song);
        } catch (SerialisationException e) {
            assertThat(e).isNull();
        }
    }

    @Test
    public void songHasInformationTest() {
        JSONParser parser = new JSONParser();
        Song song = new Song();

        final String title = "TestSong";
        final String artist = "TestInterpret";
        final String uuid = song.getUuid();

        song.setTitle(title);
        song.setArtist(artist);

        try {
            String json = parser.toJSON(song);
            assertThat(json).isNotNull().isNotEmpty();

            Song songFromJson = parser.parseJSON(json, Song.class);
            assertThat(songFromJson).isEqualTo(song);

            assertThat(title).isEqualTo(songFromJson.getTitle());
            assertThat(artist).isEqualTo(songFromJson.getArtist());
            assertThat(uuid).isEqualTo(songFromJson.getUuid());
        } catch (SerialisationException e) {
            assertThat(e).isNull();
        }
    }
}
