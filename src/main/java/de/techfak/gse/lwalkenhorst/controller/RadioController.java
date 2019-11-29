package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.musicplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.song.Song;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The main Controller of the GUI.
 * Responsible for communication between sub controllers
 */
public class RadioController implements PropertyChangeListener {

    private PlaylistController playlistController;
    private SongController songController;

    @FXML
    private VBox vBox;

    @FXML
    private TableView<Song> playlist;

    @FXML
    private Label songLabel;

    @FXML
    private ImageView image;

    @FXML
    private ProgressBar timeLine;

    /**
     * Loading the given radio model.
     * Inits the content of the view from model information.
     *
     * @param radio the model the view is observing
     */
    public void load(RadioModel radio) {
        radio.addPropertyChangeListener(this);
        this.playlistController = new PlaylistController(playlist, radio);
        this.songController = new SongController(songLabel, image, timeLine, radio);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            if (propertyChangeEvent.getPropertyName().equals("song")) {
                Property<Song> property = castProperty(propertyChangeEvent, Song.class);
                playlistController.updatePlaylist(property);
                songController.updateSong(property);
            } else if (propertyChangeEvent.getPropertyName().equals("timeChanged")) {
                Property<Float> property = castProperty(propertyChangeEvent, Float.class);
                songController.updateTime(property);
            }
        });
    }

    /**
     * Property from propertyChangeEvent with casted values.
     * Contains the properties casted oldValue and newValue.
     *
     * @param <T> the object type changed by the propertyChangeEvent
     */
    protected static final class Property<T> {
        private T oldValue;
        private T newValue;

        private Property(T oldValue, T newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public T getOldValue() {
            return oldValue;
        }

        public T getNewValue() {
            return newValue;
        }
    }

    private  <T> Property<T> castProperty(PropertyChangeEvent propertyChangeEvent, Class<T> clazz) {
        return new Property<>(clazz.cast(propertyChangeEvent.getOldValue()),
            clazz.cast(propertyChangeEvent.getNewValue()));
    }
}
