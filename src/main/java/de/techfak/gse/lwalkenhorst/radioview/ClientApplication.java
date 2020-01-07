package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.controller.ConnectionController;
import de.techfak.gse.lwalkenhorst.radioplayer.StreamPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * ClientApplication to select server connection.
 */
public class ClientApplication extends Application {

    private static StreamPlayer streamPlayer;


    @Override
    public void start(Stage stage) throws IOException {
        final FXMLLoader fxmlClientLoader = new FXMLLoader(
            Thread.currentThread().getContextClassLoader().getResource("view/connectionView.fxml")
        );
        final Pane root = fxmlClientLoader.load();
        final ConnectionController connectionController = fxmlClientLoader.getController();
        connectionController.load(streamPlayer, stage);

        final Scene scene = new Scene(root);
        stage.setTitle("GSE-Radio");
        stage.setScene(scene);
        stage.show();
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
