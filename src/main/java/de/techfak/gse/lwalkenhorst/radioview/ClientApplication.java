package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.controller.ConnectionController;
import de.techfak.gse.lwalkenhorst.controller.RadioController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        final FXMLLoader fxmlClientLoader = new FXMLLoader(
            Thread.currentThread().getContextClassLoader().getResource("view/connectionView.fxml")
        );
        final Pane root = fxmlClientLoader.load();
        final ConnectionController connectionController = fxmlClientLoader.getController();
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

    public static void main(String[] args) {
        launch(args);
    }

    public void setStage(Pane root) {
        final Scene scene = new Scene(root);
        this.stage.setTitle("GSE-Radio");
        this.stage.getIcons().add(new Image("file:src/main/resources/view/icon.png"));
        this.stage.setScene(scene);
        this.stage.show();
    }
}