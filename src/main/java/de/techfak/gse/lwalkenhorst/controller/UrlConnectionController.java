package de.techfak.gse.lwalkenhorst.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URI;
import java.util.function.Consumer;

/**
 * Controller for Url Connect Window.
 */
public class UrlConnectionController implements Initializable<Consumer<URI>> {

    @FXML
    private Button connect;

    @FXML
    private TextField url;

    @Override
    public void initialize(final Consumer<URI> consumer) {
        ClientController.resetFocus(url, url.getParent());
        connect.setOnAction(event -> {
            final String serverUrl = url.getText();
            final URI uri = URI.create(serverUrl);
            consumer.accept(uri);
        });
    }
}
