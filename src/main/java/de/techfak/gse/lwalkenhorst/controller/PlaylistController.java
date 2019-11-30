package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.stream.Collectors;

/**
 * Controller responsible for the playlist.
 */
public class PlaylistController {

    private static final String TITLE = "Title";
    private static final String ARTIST = "Artist";
    private static final String ALBUM = "Album";
    private static final String DURATION = "Duration";
    private static final String VOTES = "Votes";

    private static final String COLORING_GREEN = "-fx-text-fill:lightgreen";
    private static final String COLORING_WHITE = "-fx-text-fill:white";

    private final TableView<TableEntry> playlist;
    private final RadioModel radio;

    /**
     * Creates a new PlaylistController to fill the table.
     * Setting callbacks when list updates.
     *
     * @param playlist the tableView
     * @param radio to get information when notified
     */
    public PlaylistController(final TableView<TableEntry> playlist, final RadioModel radio) {
        this.playlist = playlist;
        this.radio = radio;
        initTable();
    }

    private void initTable() {
        TableColumn<TableEntry, String> title = new TableColumn<>(TITLE);
        TableColumn<TableEntry, String> artist = new TableColumn<>(ARTIST);
        TableColumn<TableEntry, String> album = new TableColumn<>(ALBUM);
        TableColumn<TableEntry, String> duration = new TableColumn<>(DURATION);
        TableColumn<TableEntry, String> votes = new TableColumn<>(VOTES);

        title.setCellValueFactory(new PropertyValueFactory<>(TITLE));
        artist.setCellValueFactory(new PropertyValueFactory<>(ARTIST));
        album.setCellValueFactory(new PropertyValueFactory<>(ALBUM));
        duration.setCellValueFactory(new PropertyValueFactory<>(DURATION));
        votes.setCellValueFactory(new PropertyValueFactory<>(VOTES));

        playlist.getColumns().add(title);
        playlist.getColumns().add(artist);
        playlist.getColumns().add(album);
        playlist.getColumns().add(duration);
        playlist.getColumns().add(votes);

        Callback<TableColumn<TableEntry, String>, TableCell<TableEntry, String>> color = (column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : getItem());
                setGraphic(null);
                TableRow<TableEntry> currentRow = getTableRow();
                if (currentRow != null && currentRow.getItem() != null) {
                    Song song = radio.getSong();
                    if (song != null && currentRow.getItem().getSong().equals(song)) {
                        setStyle(COLORING_GREEN);
                        return;
                    }
                }
                setStyle(COLORING_WHITE);
            }
        });

        // Setting votes by clicking on the row (TEMP SOLUTION!!!)
        playlist.setRowFactory(tableView -> {
            TableRow<TableEntry> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    radio.vote(row.getItem().getSong());
                }
            });
            return row;
        });

        title.setCellFactory(color);
        artist.setCellFactory(color);
        album.setCellFactory(color);
        duration.setCellFactory(color);

        // Filling table with playlist songs
        setItems();
    }

    private void setItems() {
        playlist.setItems(FXCollections.observableList(radio.getPlaylist().getSongs()
            .stream()
            .map(TableEntry::new)
            .collect(Collectors.toList()))
        );
        playlist.refresh();
    }

    public void updatePlaylist() {
        playlist.refresh();
    }

    /**
     * Entry of the TableView.
     */
    protected final class TableEntry {

        private Song song;

        private TableEntry(Song song) {
            this.song = song;
        }

        public String getTitle() {
            return song.getTitle();
        }

        public String getArtist() {
            return song.getArtist();
        }

        public String getAlbum() {
            return song.getAlbum();
        }

        public String getDuration() {
            return song.getDuration();
        }

        public String getVotes() {
            return String.valueOf(radio.getVotes(song));
        }

        public Song getSong() {
            return song;
        }
    }
}
