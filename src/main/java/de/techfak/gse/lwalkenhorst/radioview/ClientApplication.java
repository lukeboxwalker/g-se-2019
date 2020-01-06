package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.controller.ConnectionController;
import de.techfak.gse.lwalkenhorst.controller.RadioController;
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
    private Stage stage;


    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        final FXMLLoader fxmlClientLoader = new FXMLLoader(
            Thread.currentThread().getContextClassLoader().getResource("view/connectionView.fxml")
        );
        final Pane root = fxmlClientLoader.load();
        final ConnectionController connectionController = fxmlClientLoader.getController();
        connectionController.load(streamPlayer, stage);


        connectionController.setOnRadioStart((radio -> {
            try {
                stage.close();
                final FXMLLoader fxmlGuiLoader = new FXMLLoader(
                    Thread.currentThread().getContextClassLoader().getResource("view/view.fxml")
                );

                final Pane rootPane = fxmlGuiLoader.load();
                final RadioController controller = fxmlGuiLoader.getController();
                controller.load(radio, false);

                setStage(rootPane);

                radio.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        setStage(root);
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

    /**
     * Setting current stage with given Pane.
     * @param root the pane to set
     */
    private void setStage(Pane root) {
        final Scene scene = new Scene(root);
        this.stage.setTitle("GSE-Radio");
        this.stage.setScene(scene);
        this.stage.show();
    }
}
