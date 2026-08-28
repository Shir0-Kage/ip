import java.time.LocalDate;

public class Benjamin {
    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage("data", "benjamin.txt");

    public static void main(String[] args) {
        TaskList tasks;

        try {
            tasks = new TaskList(storage.load());

            for (String warning : storage.getLoadWarnings()) {
                ui.showError(warning);
            }
        } catch (BenjaminException exception) {
            ui.showLoadingError();
            tasks = new TaskList();
        }

        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            CommandType commandType = Parser.parseCommandType(input);

            if (commandType == CommandType.BYE) {
                break;
            }

            ui.showLine();

            try {
                switch (commandType) {
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case ON:
                    LocalDate queryDate = Parser.parseDate(input);
                    ui.showTasksOn(queryDate, tasks.findOn(queryDate));
                    break;
                case MARK:
                    int taskIndex = Parser.parseTaskIndex(input, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    ui.showMarked(tasks.get(taskIndex));
                    save(tasks);
                    break;
                case UNMARK:
                    int unmarkTaskIndex = Parser.parseTaskIndex(input, "unmark", tasks.size());
                    tasks.get(unmarkTaskIndex).markAsNotDone();
                    ui.showUnmarked(tasks.get(unmarkTaskIndex));
                    save(tasks);
                    break;
                case DELETE:
                    int deleteTaskIndex = Parser.parseTaskIndex(input, "delete", tasks.size());
                    Task removedTask = tasks.remove(deleteTaskIndex);
                    ui.showRemoved(removedTask, tasks.size());
                    save(tasks);
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = Parser.parseTask(input, commandType);
                    tasks.add(task);
                    ui.showAdded(task, tasks.size());
                    save(tasks);
                    break;
                case UNKNOWN:
                    throw new BenjaminException("I'm sorry, but I don't know what that means :-(");
                case BYE:
                    break;
                }
            } catch (BenjaminException exception) {
                ui.showError(exception.getMessage());
            }

            ui.showLine();
        }

        ui.showGoodbye();
        ui.close();
    }

    /** Writes the tasks to disk, reporting the problem if that is not possible. */
    private static void save(TaskList tasks) {
        try {
            storage.save(tasks);
        } catch (BenjaminException exception) {
            ui.showError(exception.getMessage());
        }
    }
}
