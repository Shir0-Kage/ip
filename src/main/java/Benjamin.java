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
            Command command = Command.from(input);

            if (command == Command.BYE) {
                break;
            }

            ui.showLine();

            try {
                switch (command) {
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case ON:
                    String dateText = input.substring(2).trim();

                    if (dateText.isEmpty()) {
                        throw new BenjaminException("Please provide a date after on.");
                    }

                    LocalDate queryDate = TaskDateTime.parse(dateText).getDate();
                    ui.showTasksOn(queryDate, tasks.findOn(queryDate));
                    break;
                case MARK:
                    int taskIndex = parseTaskIndex(input, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    ui.showMarked(tasks.get(taskIndex));
                    save(tasks);
                    break;
                case UNMARK:
                    int unmarkTaskIndex = parseTaskIndex(input, "unmark", tasks.size());
                    tasks.get(unmarkTaskIndex).markAsNotDone();
                    ui.showUnmarked(tasks.get(unmarkTaskIndex));
                    save(tasks);
                    break;
                case DELETE:
                    int deleteTaskIndex = parseTaskIndex(input, "delete", tasks.size());
                    Task removedTask = tasks.remove(deleteTaskIndex);
                    ui.showRemoved(removedTask, tasks.size());
                    save(tasks);
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = parseTask(input, command);
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

    private static Task parseTask(String input, Command command) throws BenjaminException {
        if (command == Command.TODO) {
            String description = input.substring(4).trim();

            if (description.isEmpty()) {
                throw new BenjaminException("The description of a todo cannot be empty.");
            }

            return new Todo(description);
        }

        if (command == Command.DEADLINE) {
            String arguments = input.substring(8).trim();
            int byIndex = arguments.indexOf("/by");

            if (byIndex < 0) {
                throw new BenjaminException("A deadline needs a /by date or time.");
            }

            String description = arguments.substring(0, byIndex).trim();
            String by = arguments.substring(byIndex + 3).trim();

            if (description.isEmpty()) {
                throw new BenjaminException("The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new BenjaminException("The /by date or time of a deadline cannot be empty.");
            }

            return new Deadline(description, TaskDateTime.parse(by));
        }

        if (command == Command.EVENT) {
            String arguments = input.substring(5).trim();
            int fromIndex = arguments.indexOf("/from");

            if (fromIndex < 0) {
                throw new BenjaminException("An event needs a /from date or time.");
            }

            int toIndex = arguments.indexOf("/to", fromIndex + 5);

            if (toIndex < 0) {
                throw new BenjaminException("An event needs a /to date or time.");
            }

            String description = arguments.substring(0, fromIndex).trim();
            String from = arguments.substring(fromIndex + 5, toIndex).trim();
            String to = arguments.substring(toIndex + 3).trim();

            if (description.isEmpty()) {
                throw new BenjaminException("The description of an event cannot be empty.");
            }
            if (from.isEmpty()) {
                throw new BenjaminException("The /from date or time of an event cannot be empty.");
            }
            if (to.isEmpty()) {
                throw new BenjaminException("The /to date or time of an event cannot be empty.");
            }

            return new Event(description, TaskDateTime.parse(from), TaskDateTime.parse(to));
        }

        throw new BenjaminException("I'm sorry, but I don't know what that means :-(");
    }

    private static int parseTaskIndex(String input, String command, int taskCount) throws BenjaminException {
        String taskNumberText = input.substring(command.length()).trim();

        if (taskNumberText.isEmpty()) {
            throw new BenjaminException("Please provide a task number after " + command + ".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new BenjaminException("The task number after " + command + " must be a whole number.");
        }

        if (taskCount == 0) {
            throw new BenjaminException("There are no tasks to " + command + ".");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BenjaminException("Choose a task number between 1 and " + taskCount + ".");
        }

        return taskNumber - 1;
    }
}
