package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Controller responsible for the playlist.
 */
public class PlaylistController {
    private static final String COLORING_GREEN = "-fx-text-fill:lightgreen";
    private static final String COLORING_WHITE = "-fx-text-fill:white";

    private final Callback<TableColumn<TableEntry, String>, TableCell<TableEntry, String>> coloring;
    private final Callback<TableColumn<TableEntry, String>, TableCell<TableEntry, String>> voting;
    private final TableView<TableEntry> playlist;
    private final RadioModel radio;

    /**
     * Creates a new PlaylistController to fill the table.
     * Setting callbacks when list updates.
     *
     * @param playlist the tableView
     * @param radio    to get information when notified
     */
    public PlaylistController(final TableView<TableEntry> playlist, final RadioModel radio) throws IOException {
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

        this.voting = (column -> {
            try {
                return new TableCell<>() {
                    final FXMLLoader fxmlLoader =
                        new FXMLLoader(getClass().getClassLoader().getResource("view/cellVote.fxml"));
                    final Button voting = fxmlLoader.load();

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? "" : getItem());
                        voting.setOnAction(event -> {
                            TableEntry entry = getTableView().getItems().get(getIndex());
                            radio.vote(entry.getSong());
                        });
                        setGraphic(voting);
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
                };
            } catch (IOException e) {
                e.printStackTrace();
                return new TableCell<>();
            }
        });

        initTable();
    }

    private void initTable() {
        initColumn("Title", coloring);
        initColumn("Artist", coloring);
        initColumn("Album", coloring);
        initColumn("Duration", coloring);
        initColumn("Votes", voting);

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

    private void initColumn(String column,
                            Callback<TableColumn<TableEntry, String>, TableCell<TableEntry, String>> callback) {
        TableColumn<TableEntry, String> tableColumn = new TableColumn<>(column);
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(column));
        tableColumn.setCellFactory(callback);
        tableColumn.setSortable(false);
        playlist.getColumns().add(tableColumn);

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
