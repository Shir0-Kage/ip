import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

public class Benjamin {
    /** Where the task list is kept between runs, relative to the project root. */
    private static final Path DATA_FILE = Paths.get("data", "benjamin.txt");

    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        ArrayList<Task> tasks = load();

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
                    ui.showTasksOn(queryDate, findTasksOn(tasks, queryDate));
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

    /** Returns the tasks that fall on the given date, in list order. */
    private static ArrayList<Task> findTasksOn(ArrayList<Task> tasks, LocalDate date) {
        ArrayList<Task> matches = new ArrayList<>();

        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matches.add(task);
            }
        }

        return matches;
    }

    /**
     * Reads the saved task list. A missing file simply means there is
     * nothing saved yet, so an empty list is returned. Lines that are not in
     * the expected format are reported and skipped, so one bad line does not
     * cost the user the rest of the list.
     */
    private static ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }

        try {
            int lineNumber = 0;

            for (String line : Files.readAllLines(DATA_FILE)) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                try {
                    tasks.add(parseSavedTask(line));
                } catch (BenjaminException exception) {
                    ui.showSkippedLine(lineNumber, exception.getMessage());
                }
            }
        } catch (IOException exception) {
            ui.showLoadingError();
            return new ArrayList<>();
        }

        return tasks;
    }

    /**
     * Rebuilds a task from one line of the save file.
     *
     * @throws BenjaminException if the line is not in the expected format.
     */
    private static Task parseSavedTask(String line) throws BenjaminException {
        String[] parts = line.split(" \\| ", -1);

        if (parts.length < 3) {
            throw new BenjaminException("it does not have enough fields.");
        }

        String type = parts[0].trim();
        String doneFlag = parts[1].trim();
        String description = parts[2].trim();

        if (!doneFlag.equals("0") && !doneFlag.equals("1")) {
            throw new BenjaminException("the done marker should be 0 or 1.");
        }
        if (description.isEmpty()) {
            throw new BenjaminException("the description is empty.");
        }

        Task task;
        switch (type) {
        case "T":
            requireFieldCount(parts, 3);
            task = new Todo(description);
            break;
        case "D":
            requireFieldCount(parts, 4);
            task = new Deadline(description,
                    TaskDateTime.parse(requireNonBlank(parts[3], "the /by field")));
            break;
        case "E":
            requireFieldCount(parts, 5);
            task = new Event(description,
                    TaskDateTime.parse(requireNonBlank(parts[3], "the /from field")),
                    TaskDateTime.parse(requireNonBlank(parts[4], "the /to field")));
            break;
        default:
            throw new BenjaminException("\"" + type + "\" is not a known task type.");
        }

        if (doneFlag.equals("1")) {
            task.markAsDone();
        }

        return task;
    }

    private static void requireFieldCount(String[] parts, int expected) throws BenjaminException {
        if (parts.length != expected) {
            throw new BenjaminException("type " + parts[0].trim() + " needs exactly "
                    + expected + " fields but has " + parts.length + ".");
        }
    }

    private static String requireNonBlank(String value, String fieldName) throws BenjaminException {
        String trimmed = value.trim();

        if (trimmed.isEmpty()) {
            throw new BenjaminException(fieldName + " is empty.");
        }

        return trimmed;
    }

    /**
     * Writes the whole task list to the save file, creating the data folder
     * first if it is not there yet.
     */
    private static void save(ArrayList<Task> tasks) {
        try {
            Files.createDirectories(DATA_FILE.getParent());

            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toSaveFormat());
            }

            Files.write(DATA_FILE, lines);
        } catch (IOException exception) {
            ui.showError("I could not save your tasks.");
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
