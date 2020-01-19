package de.techfak.gse.lwalkenhorst.controller;

import de.techfak.gse.lwalkenhorst.radioplayer.StreamPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.playbehavior.StreamListeningPlayBehavior;
import de.techfak.gse.lwalkenhorst.server.ClientSocket;
import de.techfak.gse.lwalkenhorst.server.UrlParser;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Controller for Client GUI.
 */
public class ClientController implements Initializable<StreamPlayer> {

    private static final int LOST_CONNECTION = 1001;
    private static final int CONNECTION_PERIOD = 3000;

    @FXML
    private MenuItem wiki;

    @FXML
    private MenuItem ipConnect;

    @FXML
    private MenuItem urlConnect;

    @FXML
    private MenuItem disconnect;

    @FXML
    private VBox layout;

    private Node connectionInfo;
    private StreamPlayer streamPlayer;

    @Override
    public void initialize(final StreamPlayer streamPlayer) throws IOException {
        this.connectionInfo = loadInfo();
        this.streamPlayer = streamPlayer;

        final URL location = Thread.currentThread().getContextClassLoader().getResource("view/view.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        final Pane root = fxmlLoader.load();
        final RadioController controller = fxmlLoader.getController();
        controller.load(streamPlayer, false);
        controller.setFallbackNode(connectionInfo);
        layout.getChildren().add(root);
        ipConnect(controller);

        ipConnect.setOnAction(event -> ipConnect(controller));
        urlConnect.setOnAction(event -> urlConnect(controller));

        disconnect.setOnAction(e -> {
            controller.setFallbackNode(connectionInfo);
            streamPlayer.disconnect();
        });

        wiki.setOnAction(e -> {
            final String url = "https://se.techfak.de/projects/g-se-ws-2019-lwalkenhorst/wiki";
            openWebPage(URI.create(url));
        });
    }

    private Node loadInfo() throws IOException {
        final URL location = Thread.currentThread().getContextClassLoader().getResource("view/connectionInfo.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        return fxmlLoader.load();
    }

    private <T extends Initializable<Consumer<URI>>> Stage buildConnectionWindow(
            final RadioController controller, final FXMLLoader fxmlLoader) throws Exception {
        controller.resetFallbackNode();
        final Stage connectionWindow = new Stage();
        connectionWindow.initModality(Modality.APPLICATION_MODAL);
        connectionWindow.setTitle("Connect to server");

        final Pane pane = fxmlLoader.load();
        final T connectionController = fxmlLoader.getController();
        connectionController.initialize((uri -> {
            connectionWindow.close();
            final UrlParser parser = new UrlParser();
            if (parser.isValidURL(uri.toString())) {
                final String serverAddress = parser.toAddress(uri.getHost());
                ClientSocket client = new ClientSocket(serverAddress, uri.getPort(), streamPlayer) {
                    @Override
                    public void onClose(int closeCode, String reason, boolean b) {
                        super.onClose(closeCode, reason, b);
                        if (closeCode == LOST_CONNECTION) {
                            Platform.runLater(reconnectSchedule(CONNECTION_PERIOD));
                        }
                    }
                };
                if (client.canConnect()) {
                    streamPlayer.setPlayBehavior(new StreamListeningPlayBehavior(serverAddress, uri.getPort()));
                    client.connect();
                    streamPlayer.start();
                } else {
                    controller.setFallbackNode(connectionInfo);
                    System.err.println("could not connect to server");
                }

            } else {
                controller.setFallbackNode(connectionInfo);
            }
        }));

        Scene scene = new Scene(pane);
        connectionWindow.setScene(scene);
        return connectionWindow;
    }

    private void ipConnect(final RadioController controller) {
        final URL location = Thread.currentThread().getContextClassLoader().getResource("view/ipConnect.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        try {
            buildConnectionWindow(controller, fxmlLoader).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void urlConnect(final RadioController controller) {
        final URL location = Thread.currentThread().getContextClassLoader().getResource("view/urlConnect.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        try {
            buildConnectionWindow(controller, fxmlLoader).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openWebPage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Resetting Focus of javafx Node.
     *
     * @param focusedNode   node that is focused but should not be focused.
     * @param redirectFocus node to redirect focus to.
     */
    public static void resetFocus(final Node focusedNode, final Node redirectFocus) {
        focusedNode.focusedProperty().addListener(new ChangeListener<>() {
            private boolean startup = true;

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if (newValue && startup) {
                    redirectFocus.requestFocus();
                    startup = false;
                }
            }
        });
    }
}
