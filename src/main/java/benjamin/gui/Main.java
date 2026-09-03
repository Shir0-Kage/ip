package benjamin.gui;

import java.io.IOException;

import benjamin.Benjamin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The JavaFX application that shows the chat window.
 */
public class Main extends Application {
    private final Benjamin benjamin = new Benjamin();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Benjamin");
            stage.setMinHeight(600.0);
            stage.setMinWidth(400.0);

            fxmlLoader.<MainWindow>getController().setBenjamin(benjamin);
            stage.show();
        } catch (IOException exception) {
            System.err.println("Could not load the chat window: " + exception.getMessage());
        }
    }
}
