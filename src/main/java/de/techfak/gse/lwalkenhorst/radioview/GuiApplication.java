package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.controller.RadioController;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Application to launch the static fxml file.
 * Prepares the stage and loads the Controller specified in the fxml
 */
public class GuiApplication extends Application {
    private static RadioModel radio;
    private static boolean advanced = false;
    private static final List<String> ADVANCED_MODE = Arrays.asList("-a", "--advanced");

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/view.fxml"));

        Pane root = fxmlLoader.load();
        RadioController controller = fxmlLoader.getController();
        controller.load(radio, advanced);

        Scene scene = new Scene(root);
        stage.setTitle("GSE-Radio");
        stage.getIcons().add(new Image("file:src/main/resources/view/icon.png"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Starting the Gui.
     * Use arguments -a or --advanced to have control buttons in Gui
     *
     * @param radioPlayer the radio model
     * @param args        the arguments.
     */
    public static void start(final RadioModel radioPlayer, final String... args) {
        radio = radioPlayer;
        String[] param = args;
        if (param.length >= 1 && ADVANCED_MODE.contains(param[0])) {
            advanced = true;
            param = Arrays.copyOfRange(args, 1, args.length);
        }
        GuiApplication.main(param);
    }

    public static void main(final String... args) {
        launch(args);
    }
}
