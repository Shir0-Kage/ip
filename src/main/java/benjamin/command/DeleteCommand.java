package benjamin.command;

import benjamin.BenjaminException;
import benjamin.storage.Storage;
import benjamin.task.Task;
import benjamin.task.TaskList;
import benjamin.ui.Ui;

/** Removes one task from the list. */
public class DeleteCommand extends Command {
    private final int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BenjaminException {
        int index = toIndex(taskNumber, "delete", tasks);

        Task removedTask = tasks.remove(index);
        ui.showRemoved(removedTask, tasks.size());
        storage.save(tasks);
    }
}
