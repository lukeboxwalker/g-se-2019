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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for the playlist.
 */
public class PlaylistController {

    private static final List<String> COLUMNS = Arrays.asList("Title", "Artist", "Album", "Duration", "Votes");

    private static final String COLORING_GREEN = "-fx-text-fill:lightgreen";
    private static final String COLORING_WHITE = "-fx-text-fill:white";

    private final Callback<TableColumn<TableEntry, String>, TableCell<TableEntry, String>> coloring;
    private final TableView<TableEntry> playlist;
    private final RadioModel radio;

    /**
     * Creates a new PlaylistController to fill the table.
     * Setting callbacks when list updates.
     *
     * @param playlist the tableView
     * @param radio    to get information when notified
     */
    public PlaylistController(final TableView<TableEntry> playlist, final RadioModel radio) {
        this.playlist = playlist;
        this.radio = radio;
        this.coloring = (column -> new TableCell<>() {
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
        initTable();
    }

    private void initTable() {
        COLUMNS.forEach(this::initColumn);
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

        // Filling table with playlist songs
        setItems();

    }

    /**
     * Setting updated playlist from radio as table content.
     */
    public void setItems() {
        playlist.setItems(FXCollections.observableList(radio.getPlaylist().getSongs()
            .stream()
            .map(TableEntry::new)
            .collect(Collectors.toList()))
        );
    }

    private void initColumn(String column) {
        TableColumn<TableEntry, String> tableColumn = new TableColumn<>(column);
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(column));
        playlist.getColumns().add(tableColumn);
        tableColumn.setCellFactory(coloring);
        tableColumn.setSortable(false);

    }

    public void updatePlaylist() {
        setItems();
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
