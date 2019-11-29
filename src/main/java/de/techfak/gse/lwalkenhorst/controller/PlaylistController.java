package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.musicplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * Controller responsible for the playlist.
 */
public class PlaylistController {

    private static final String TITLE = "Title";
    private static final String ARTIST = "Artist";
    private static final String ALBUM = "Album";
    private static final String DURATION = "Duration";

    private static final String COLORING_GREEN = "-fx-text-fill:lightgreen";
    private static final String COLORING_WHITE = "-fx-text-fill:white";

    private final TableView<Song> playlist;
    private final RadioModel radio;

    /**
     * Creates a new PlaylistController to fill the table.
     * Setting callbacks when list updates.
     *
     * @param playlist the tableView
     * @param radio to get information when notified
     */
    public PlaylistController(final TableView<Song> playlist, final RadioModel radio) {
        this.playlist = playlist;
        this.radio = radio;
        initTable();
    }

    private void initTable() {
        TableColumn<Song, String> title = new TableColumn<>(TITLE);
        TableColumn<Song, String> artist = new TableColumn<>(ARTIST);
        TableColumn<Song, String> album = new TableColumn<>(ALBUM);
        TableColumn<Song, String> duration = new TableColumn<>(DURATION);

        title.setCellValueFactory(new PropertyValueFactory<>(TITLE));
        artist.setCellValueFactory(new PropertyValueFactory<>(ARTIST));
        album.setCellValueFactory(new PropertyValueFactory<>(ALBUM));
        duration.setCellValueFactory(new PropertyValueFactory<>(DURATION));

        playlist.getColumns().add(title);
        playlist.getColumns().add(artist);
        playlist.getColumns().add(album);
        playlist.getColumns().add(duration);

        Callback<TableColumn<Song, String>, TableCell<Song, String>> songColor = (column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : getItem());
                setGraphic(null);
                TableRow<Song> currentRow = getTableRow();
                if (currentRow != null && currentRow.getItem() != null) {
                    Song song = radio.getSong();
                    if (song != null && currentRow.getItem().equals(song)) {
                        setStyle(COLORING_GREEN);
                        return;
                    }
                }
                setStyle(COLORING_WHITE);
            }
        });

        title.setCellFactory(songColor);
        artist.setCellFactory(songColor);
        album.setCellFactory(songColor);
        duration.setCellFactory(songColor);

        // Filling table with playlist songs
        playlist.setItems(FXCollections.observableList(radio.getPlaylist().getSongs()));
    }

    public void updatePlaylist(RadioController.Property<Song> property) {
        playlist.refresh();
    }
}
