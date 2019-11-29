package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.musicplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The main Controller of the GUI.
 * Responsible for communication between sub controllers
 */
public class RadioController implements PropertyChangeListener {

    private PlaylistController playlistController;

    @FXML
    private VBox vBox;

    @FXML
    private TableView<Song> playlist;

    /**
     * Loading the given radio model.
     * Inits the content of the view from model information.
     *
     * @param radio the model the view is observing
     */
    public void load(RadioModel radio) {
        radio.addPropertyChangeListener(this);
        this.playlistController = new PlaylistController(playlist, radio);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            if (propertyChangeEvent.getPropertyName().equals("song")) {
                playlistController.updateProperty(propertyChangeEvent, Song.class);
            }
        });
    }
}
