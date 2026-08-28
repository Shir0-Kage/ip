package benjamin.command;

import benjamin.storage.Storage;
import benjamin.task.TaskList;
import benjamin.ui.Ui;

/** Says goodbye and stops the chatbot. */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showFarewell();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
