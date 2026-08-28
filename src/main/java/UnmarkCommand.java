/** Marks one task as not done yet. */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BenjaminException {
        int index = toIndex(taskNumber, "unmark", tasks);

        tasks.get(index).markAsNotDone();
        ui.showUnmarked(tasks.get(index));
        storage.save(tasks);
    }
}
