package benjamin.command;

import benjamin.storage.Storage;
import benjamin.task.TaskList;
import benjamin.ui.Ui;

/** Shows every task in the list. */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
