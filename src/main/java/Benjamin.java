import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Benjamin {
    /** Where the task list is kept between runs, relative to the project root. */
    private static final Path DATA_FILE = Paths.get("data", "benjamin.txt");

    public static void main(String[] args) {
        String banner = " ____             _                 _\n"
                + "| __ )  ___ _ __ (_) __ _ _ __ ___ (_)_ __\n"
                + "|  _ \\ / _ \\ '_ \\| |/ _` | '_ ` _ \\| | '_ \\\n"
                + "| |_) |  __/ | | | | (_| | | | | | | | | | |\n"
                + "|____/ \\___|_| |_|/ |\\__,_|_| |_| |_|_|_| |_|\n"
                + "                |__/\n";
        String greeting = "Hello! I'm Benjamin.\n"
                + "What can I do for you?\n";

        String divider = "____________________________________________________________";

        ArrayList<Task> tasks = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        System.out.println(banner + greeting + divider);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            Command command = Command.from(input);

            if (command == Command.BYE) {
                break;
            }

            System.out.println(divider);

            try {
                switch (command) {
                case LIST:
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.printf("%d.%s%n", i + 1, tasks.get(i));
                    }
                    break;
                case MARK:
                    int taskIndex = parseTaskIndex(input, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex));
                    save(tasks);
                    break;
                case UNMARK:
                    int unmarkTaskIndex = parseTaskIndex(input, "unmark", tasks.size());
                    tasks.get(unmarkTaskIndex).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(unmarkTaskIndex));
                    save(tasks);
                    break;
                case DELETE:
                    int deleteTaskIndex = parseTaskIndex(input, "delete", tasks.size());
                    Task removedTask = tasks.remove(deleteTaskIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    save(tasks);
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = parseTask(input, command);
                    addTask(tasks, task);
                    save(tasks);
                    break;
                case UNKNOWN:
                    throw new BenjaminException("I'm sorry, but I don't know what that means :-(");
                case BYE:
                    break;
                }
            } catch (BenjaminException exception) {
                System.out.println("OOPS!!! " + exception.getMessage());
            }

            System.out.println(divider);
        }

        String farewell = "Bye. Hope to see you again soon!";
        System.out.println(divider);
        System.out.println(farewell);
        System.out.println(divider);

        scanner.close();
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
            System.out.println("OOPS!!! I could not save your tasks.");
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

            return new Deadline(description, by);
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

            return new Event(description, from, to);
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

    private static void addTask(ArrayList<Task> tasks, Task task) {
        tasks.add(task);

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }
}
