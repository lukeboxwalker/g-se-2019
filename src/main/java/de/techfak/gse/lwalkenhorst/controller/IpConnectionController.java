package de.techfak.gse.lwalkenhorst.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URI;
import java.util.function.Consumer;

/**
 * Controller for Ip Connect Window.
 */
public class IpConnectionController implements Initializable<Consumer<URI>> {

    @FXML
    private Button connect;

    @FXML
    private TextField address;

    @FXML
    private TextField port;

    @Override
    public void initialize(final Consumer<URI> consumer) {
        ClientController.resetFocus(address, address.getParent());
        connect.setOnAction(event -> {
            final String serverAddress = address.getText();
            final String serverPort = port.getText();
            final URI uri = URI.create("http://" + serverAddress + ":" + serverPort);
            consumer.accept(uri);
        });
    }
}
