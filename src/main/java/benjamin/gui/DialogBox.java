package benjamin.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * One line of the conversation: a picture beside the words that were said.
 *
 * <p>The user's own messages are shown picture on the right, and the chatbot's
 * replies are flipped so that the two sides are easy to tell apart.
 */
public class DialogBox extends HBox {
    private final Label text;
    private final ImageView displayPicture;

    private DialogBox(String message, Image picture) {
        text = new Label(message);
        text.setWrapText(true);
        text.setPadding(new Insets(8));

        displayPicture = new ImageView(picture);
        displayPicture.setFitWidth(48.0);
        displayPicture.setFitHeight(48.0);
        displayPicture.setClip(new Circle(24.0, 24.0, 24.0));

        this.setAlignment(Pos.TOP_RIGHT);
        this.setSpacing(8);
        this.setPadding(new Insets(8));
        this.getChildren().addAll(text, displayPicture);
    }

    /**
     * Returns a dialog box for something the user said, with the picture on the right.
     *
     * @param message the words to show.
     * @param picture the speaker's picture.
     */
    public static DialogBox getUserDialog(String message, Image picture) {
        return new DialogBox(message, picture);
    }

    /**
     * Returns a dialog box for something the chatbot said, with the picture on the left.
     *
     * @param message the words to show.
     * @param picture the speaker's picture.
     */
    public static DialogBox getBenjaminDialog(String message, Image picture) {
        DialogBox dialogBox = new DialogBox(message, picture);
        dialogBox.flip();

        return dialogBox;
    }

    /** Puts the picture on the left and the words on the right. */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);

        ObservableList<Node> children = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(children);
        this.getChildren().setAll(children);
    }
}
