package de.techfak.gse.lwalkenhorst.controller;


import de.techfak.gse.lwalkenhorst.radioplayer.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

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

    @FXML
    private ProgressIndicator loading;

    private Consumer<RadioModel> radioStart = (radioModel -> {
    });

    public void initialize() {
        loading.setProgress(-1);
        loading.setVisible(false);
        address.focusedProperty().addListener(new ChangeListener<>() {
            private boolean startup = true;

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
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
                    radioStart.accept(radio);
                    response.setText("");
                } else if (serverAddress.isEmpty() || serverPort.isEmpty()) {
                    response.setText("[ERROR] no input given");
                } else {
                    radio = factory.newStreamPlayer(serverAddress, serverPort);
                    radioStart.accept(radio);
                    response.setText("");
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
