package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.musicplayer.RadioModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The main Controller of the GUI.
 * Responsible for communication between sub controllers
 */
public class RadioController implements PropertyChangeListener {

    private RadioModel radio;

    public void load(RadioModel radio) {
        this.radio = radio;
        this.radio.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }
}
