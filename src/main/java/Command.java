/**
 * One instruction from the user, ready to be carried out.
 *
 * <p>Each kind of command is its own subclass, so adding a new command means
 * adding a class rather than growing a switch statement.
 */
public abstract class Command {
    /**
     * Carries out this command.
     *
     * @param tasks the list the command may read or change.
     * @param ui used to report the outcome to the user.
     * @param storage used to keep any change on disk.
     * @throws BenjaminException if the command cannot be carried out.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws BenjaminException;

    /** Returns true if the chatbot should stop after this command. */
    public boolean isExit() {
        return false;
    }

    /**
     * Returns the list index for a one based task number, after checking it
     * against the tasks that are actually in the list.
     *
     * @param keyword the command word, used to word any problem message.
     * @throws BenjaminException if the list is empty or the number is out of range.
     */
    protected static int toIndex(int taskNumber, String keyword, TaskList tasks)
            throws BenjaminException {
        if (tasks.size() == 0) {
            throw new BenjaminException("There are no tasks to " + keyword + ".");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BenjaminException("Choose a task number between 1 and "
                    + tasks.size() + ".");
        }

        return taskNumber - 1;
    }
}
