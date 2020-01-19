package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.controller.ClientController;
import de.techfak.gse.lwalkenhorst.radioplayer.StreamPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * ClientApplication to select server connection.
 */
public class ClientApplication extends Application {

    private static StreamPlayer streamPlayer;

    @Override
    public void start(Stage stage) throws IOException {
        final URL location = Thread.currentThread().getContextClassLoader().getResource("view/ClientView.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        final Pane root = fxmlLoader.load();
        final ClientController controller = fxmlLoader.getController();

        final Scene scene = new Scene(root);
        stage.setTitle("GSE-Radio");
        stage.setScene(scene);
        stage.show();

        controller.initialize(streamPlayer);
    }

    /**
     * Starting the Client.
     *
     * @param stream musicPlayer to play music from stream.
     */
    public static void start(final StreamPlayer stream) {
        streamPlayer = stream;
        ClientApplication.main();
    }

    public static void main(String... args) {
        launch(args);
    }
}
