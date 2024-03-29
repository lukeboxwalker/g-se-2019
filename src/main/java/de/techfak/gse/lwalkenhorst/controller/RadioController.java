package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import de.techfak.gse.lwalkenhorst.radioplayer.VLCJMediaPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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

    @FXML
    private Button back;

    @FXML
    private ToggleButton play;

    /**
     * Loading the given radio model.
     * Inits the content of the view from model information.
     *
     * @param radio    the model the view is observing
     * @param advanced to decide if control buttons are loaded
     */
    public void load(final RadioPlayer radio, final boolean advanced) {
        radio.addPropertyChangeListener(this);
        this.playlistController = new PlaylistController(playlist, radio);
        this.songController = new SongController(songLabel, image, timeLine, radio);
        if (advanced) {
            loadControls(radio);
        } else {
            skip.setVisible(false);
            back.setVisible(false);
            play.setVisible(false);
        }
    }

    /**
     * Setting ActionHandlers for control buttons.
     *
     * @param radio to perform action on.
     */
    public void loadControls(final RadioPlayer radio) {
        skip.setOnAction(e -> radio.skipSong(VLCJMediaPlayer.Skip.FORWARD));
        back.setOnAction(e -> radio.skipSong(VLCJMediaPlayer.Skip.BACKWARD));
        play.setOnAction(e -> {
            if (play.isSelected()) {
                radio.pauseSong();
            } else {
                radio.resumeSong();
            }
        });

    }

    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            switch (propertyChangeEvent.getPropertyName()) {
                case MusicPlayer.SONG_UPDATE:
                    final Property<Song> songProperty = castProperty(propertyChangeEvent, Song.class);
                    playlistController.updatePlaylist();
                    songController.updateSong(songProperty);
                    break;
                case MusicPlayer.VOTE_UPDATE:
                    playlistController.updatePlaylist();
                    break;
                case MusicPlayer.TIME_UPDATE:
                    final Property<Float> floatProperty = castProperty(propertyChangeEvent, Float.class);
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

        private Property(final T oldValue, final T newValue) {
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

    private <T> Property<T> castProperty(final PropertyChangeEvent propertyChangeEvent, final Class<T> clazz) {
        return new Property<>(clazz.cast(propertyChangeEvent.getOldValue()),
            clazz.cast(propertyChangeEvent.getNewValue()));
    }

    public void resetFallbackNode() {
        this.playlistController.resetFallbackNode();
    }

    public void setFallbackNode(Node node) {
        this.playlistController.setFallbackNode(node);
    }
}
