package de.techfak.gse.lwalkenhorst.radioview;

import de.techfak.gse.lwalkenhorst.argumentparser.*;
import de.techfak.gse.lwalkenhorst.controller.RadioController;
import de.techfak.gse.lwalkenhorst.exceptions.OverlappingOptionException;
import de.techfak.gse.lwalkenhorst.exceptions.ParseException;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Application to launch the static fxml file.
 * Prepares the stage and loads the Controller specified in the fxml
 */
public class GuiApplication extends Application {
    private static RadioPlayer radio;
    private static boolean advanced = false;
    private static final String ADVANCED = "advanced";

    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(
            Thread.currentThread().getContextClassLoader().getResource("view/view.fxml")
        );

        final Pane root = fxmlLoader.load();
        final RadioController controller = fxmlLoader.getController();
        controller.load(radio, advanced);

        final Scene scene = new Scene(root);
        stage.setTitle("GSE-Radio");
        stage.setScene(scene);
        stage.show();

        radio.start();
    }

    /**
     * Starting the Gui.
     * Use arguments -a or --advanced to have control buttons in Gui
     *
     * @param radioPlayer the radio model
     * @param args        the arguments.
     */
    public static void start(final RadioPlayer radioPlayer, final String... args) {
        try {
            List<Option> options = new ArrayList<>();
            Option advancedOption = CommandLineOption.builder().withName(ADVANCED).build();
            options.add(advancedOption);
            CommandLine commandLine = new ArgumentParser().parse(options, args);
            radio = radioPlayer;
            if (commandLine.hasOption(ADVANCED)) {
                advanced = true;
            }
            GuiApplication.main();
        } catch (OverlappingOptionException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String... args) {
        launch(args);
    }
}
