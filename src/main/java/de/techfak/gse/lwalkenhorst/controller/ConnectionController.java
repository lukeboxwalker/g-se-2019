package de.techfak.gse.lwalkenhorst.controller;


import de.techfak.gse.lwalkenhorst.radioplayer.*;
import de.techfak.gse.lwalkenhorst.radioplayer.playbehavior.StreamListeningPlayBehavior;
import de.techfak.gse.lwalkenhorst.exceptions.NoConnectionException;
import de.techfak.gse.lwalkenhorst.exceptions.NoValidUrlException;
import de.techfak.gse.lwalkenhorst.server.UrlParser;
import de.techfak.gse.lwalkenhorst.server.WebClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

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

    private StreamPlayer streamPlayer;
    private Stage currentStage;

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
            String serverAddress = address.getText();
            String serverPort = port.getText();
            String url = urlAddress.getText();
            try {
                UrlParser parser = new UrlParser();
                WebClient client;
                if (parser.isValidServerAddress(serverAddress) && parser.isValidPort(serverPort)) {
                    client = new WebClient(serverAddress, Integer.parseInt(serverPort));
                } else if (parser.isValidURL(url)) {
                    serverAddress = parser.extractServerAddress(url);
                    serverPort = parser.extractPort(url);
                    client = new WebClient(serverAddress, Integer.parseInt(serverPort));
                } else {
                    throw new NoValidUrlException("could not parse address");
                }
                streamPlayer.useWebClient(client);
                streamPlayer.setPlayBehavior(
                    new StreamListeningPlayBehavior(parser.toAddress(serverAddress), serverPort));
                loadGUIStage(streamPlayer);
                currentStage.close();
            } catch (NoConnectionException | NoValidUrlException e) {
                response.setText(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadGUIStage(RadioPlayer radio) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(
            Thread.currentThread().getContextClassLoader().getResource("view/view.fxml"));
        final Pane root = fxmlLoader.load();
        final RadioController controller = fxmlLoader.getController();
        controller.load(radio, false);
        Stage stage = new Stage();
        stage.setTitle("GSE-Radio Client connection");
        stage.setScene(new Scene(root));
        stage.show();

        radio.start();
    }

    public void load(StreamPlayer streamPlayer, Stage stage) {
        this.streamPlayer = streamPlayer;
        this.currentStage = stage;
    }

}
