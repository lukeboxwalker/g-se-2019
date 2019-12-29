package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import de.techfak.gse.lwalkenhorst.radioplayer.Song;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller responsible the song section in the view.
 */
public class SongController {

    private final Label songLabel;
    private final ImageView image;
    private final ProgressBar timeLine;
    private final RadioModel radio;

    /**
     * Creates a new SongController to show the current song.
     *
     * @param songLabel label for the song
     * @param image     to display the songs image
     * @param timeLine  to show the current playing state from the song
     * @param radio     to get information when notified
     */
    public SongController(final Label songLabel, final ImageView image,
                          final ProgressBar timeLine, final RadioModel radio) {
        this.songLabel = songLabel;
        this.image = image;
        this.timeLine = timeLine;
        this.radio = radio;
        initLabel();
    }

    private void initLabel() {
        final Song currentSong = radio.getSong();
        songLabel.setAlignment(Pos.CENTER);
        timeLine.setProgress(0);
        setSong(currentSong);
    }

    private void setSong(final Song song) {
        songLabel.setText(song.getTitle() + " - " + song.getArtist());
        if (!song.getArtWorkURL().isEmpty()) {
            image.setImage(new Image(song.getArtWorkURL()));
        }
    }

    public void updateSong(final RadioController.Property<Song> property) {
        setSong(property.getNewValue());
        timeLine.setProgress(0);
    }

    public void updateTime(final RadioController.Property<Float> property) {
        timeLine.setProgress(property.getNewValue());
    }

}
