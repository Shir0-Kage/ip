/** Adds one new task to the list. */
public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BenjaminException {
        tasks.add(task);
        ui.showAdded(task, tasks.size());
        storage.save(tasks);
    }
}
