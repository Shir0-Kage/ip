package benjamin;

import benjamin.command.Command;
import benjamin.parser.Parser;
import benjamin.storage.Storage;
import benjamin.task.TaskList;
import benjamin.ui.Ui;

/**
 * A chatbot that keeps a list of todos, deadlines and events.
 *
 * <p>This class only wires the parts together. Reading and printing belongs to
 * {@link Ui}, understanding input to {@link Parser}, the tasks themselves to
 * {@link TaskList}, and the save file to {@link Storage}.
 */
public class Benjamin {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a chatbot that saves to the given path, and loads whatever has
     * been saved there before.
     *
     * @param first the first part of the save path, such as {@code data}.
     * @param more the remaining parts, such as {@code benjamin.txt}.
     */
    public Benjamin(String first, String... more) {
        ui = new Ui();
        storage = new Storage(first, more);

        try {
            tasks = new TaskList(storage.load());

            for (String warning : storage.getLoadWarnings()) {
                ui.showError(warning);
            }
        } catch (BenjaminException exception) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /** Greets the user, then handles commands until there are none left. */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();

                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (BenjaminException exception) {
                ui.showError(exception.getMessage());
            } finally {
                ui.showLine();
            }
        }

        if (!isExit) {
            // The input ended without a bye command, so sign off anyway.
            ui.showLine();
            ui.showFarewell();
            ui.showLine();
        }

        ui.close();
    }

    public static void main(String[] args) {
        new Benjamin("data", "benjamin.txt").run();
    }
}
