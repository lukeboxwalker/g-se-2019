package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

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
    private TableView<PlaylistController.TableEntry> playlist;

    @FXML
    private Label songLabel;

    @FXML
    private ImageView image;

    @FXML
    private ProgressBar timeLine;

    @FXML
    private Button skip;

    /**
     * Loading the given radio model.
     * Inits the content of the view from model information.
     *
     * @param radio the model the view is observing
     * @param advanced to decide if control buttons are loaded
     */
    public void load(RadioModel radio, boolean advanced) {
        radio.addPropertyChangeListener(this);
        this.playlistController = new PlaylistController(playlist, radio);
        this.songController = new SongController(songLabel, image, timeLine, radio);
        if (advanced) {
            loadControls(radio);
        } else {
            skip.setVisible(false);
        }
    }

    public void loadControls(RadioModel radio) {
        skip.setOnAction(e -> radio.skipSong());
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            switch (propertyChangeEvent.getPropertyName()) {
                case MusicPlayer.SONG_UPDATE:
                    Property<Song> songProperty = castProperty(propertyChangeEvent, Song.class);
                    playlistController.updatePlaylist();
                    songController.updateSong(songProperty);
                    break;
                case MusicPlayer.VOTE_UPDATE:
                    playlistController.updatePlaylist();
                    break;
                case MusicPlayer.TIME_UPDATE:
                    Property<Float> floatProperty = castProperty(propertyChangeEvent, Float.class);
                    songController.updateTime(floatProperty);
                    break;
                default:
                    break;
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

    private <T> Property<T> castProperty(PropertyChangeEvent propertyChangeEvent, Class<T> clazz) {
        return new Property<>(clazz.cast(propertyChangeEvent.getOldValue()),
            clazz.cast(propertyChangeEvent.getNewValue()));
    }
}
