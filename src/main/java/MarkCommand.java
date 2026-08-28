/** Marks one task as done. */
public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BenjaminException {
        int index = toIndex(taskNumber, "mark", tasks);

        tasks.get(index).markAsDone();
        ui.showMarked(tasks.get(index));
        storage.save(tasks);
    }
}
