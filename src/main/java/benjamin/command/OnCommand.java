package benjamin.command;

import java.time.LocalDate;

import benjamin.storage.Storage;
import benjamin.task.TaskList;
import benjamin.ui.Ui;

/** Shows the tasks that fall on one particular date. */
public class OnCommand extends Command {
    private final LocalDate date;

    public OnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOn(date, tasks.findOn(date));
    }
}
