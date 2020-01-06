package de.techfak.gse.lwalkenhorst.controller;


import de.techfak.gse.lwalkenhorst.radioplayer.*;
import de.techfak.gse.lwalkenhorst.server.NoConnectionException;
import de.techfak.gse.lwalkenhorst.server.NoValidUrlException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

/**
 * Controller for ClientApplication.
 */
public class ConnectionController {

    @FXML
    private Button connect;

    @FXML
    private HBox hbox;

    @FXML
    private TextField urlAddress;

    @FXML
    private TextField address;

    @FXML
    private TextField port;

    @FXML
    private Label response;

    private Consumer<RadioModel> radioStart = (radioModel -> {
    });

    /**
     * Inits the controller.
     * When user tries to connect verifies connection
     * and changes to normal gui
     */
    public void initialize() {
        address.focusedProperty().addListener(new ChangeListener<>() {
            private boolean startup = true;

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if (newValue && startup) {
                    hbox.requestFocus();
                    startup = false;
                }
            }
        });

        connect.setOnAction(event -> {
            String url = urlAddress.getText();
            String serverAddress = address.getText();
            String serverPort = port.getText();
            StreamPlayerFactory factory = new StreamPlayerFactory();
            try {
                RadioModel radio;
                if (!url.isEmpty()) {
                    radio = factory.newStreamPlayer(url);
                    response.setText("");
                    radioStart.accept(radio);
                } else if (serverAddress.isEmpty() || serverPort.isEmpty()) {
                    response.setText("[ERROR] no input given");
                } else {
                    radio = factory.newStreamPlayer(serverAddress, serverPort);
                    response.setText("");
                    radioStart.accept(radio);
                }
            } catch (NoConnectionException | NoValidUrlException e) {
                response.setText(e.getMessage());
            }
        });
    }

    public void setOnRadioStart(Consumer<RadioModel> radioStart) {
        this.radioStart = radioStart;
    }
}
