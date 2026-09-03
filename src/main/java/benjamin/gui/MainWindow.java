package benjamin.gui;

import benjamin.Benjamin;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main chat window.
 */
public class MainWindow extends AnchorPane {
    /** How long the goodbye stays on screen before the window closes. */
    private static final Duration CLOSING_DELAY = Duration.seconds(1.5);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Benjamin benjamin;

    private final Image userImage =
            new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image benjaminImage =
            new Image(this.getClass().getResourceAsStream("/images/DaBenjamin.png"));

    /** Keeps the newest message in view as the conversation grows. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Hands the controller the chatbot it should talk to, and shows its greeting.
     *
     * @param benjamin the chatbot answering the user.
     */
    public void setBenjamin(Benjamin benjamin) {
        this.benjamin = benjamin;
        dialogContainer.getChildren().add(
                DialogBox.getBenjaminDialog(benjamin.getWelcome().strip(), benjaminImage));
    }

    /**
     * Shows what the user typed and the reply to it, then clears the input box.
     *
     * <p>A bye command closes the window shortly afterwards, so that the user
     * has time to read the goodbye.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();

        if (input.isEmpty()) {
            return;
        }

        String response = benjamin.getResponse(input).strip();

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBenjaminDialog(response, benjaminImage));
        userInput.clear();

        if (benjamin.isExit()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);

            PauseTransition pause = new PauseTransition(CLOSING_DELAY);
            pause.setOnFinished(event -> Platform.exit());
            pause.play();
        }
    }
}
