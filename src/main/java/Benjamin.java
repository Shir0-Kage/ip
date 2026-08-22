import java.util.Scanner;

public class Benjamin {
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

        Task[] tasks = new Task[100];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);

        System.out.println(banner + greeting + divider);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            System.out.println(divider);

            try {
                if (input.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.printf("%d.%s%n", i + 1, tasks[i]);
                    }
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(input, "mark", taskCount);
                    tasks[taskIndex].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskIndex]);
                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(input, "unmark", taskCount);
                    tasks[taskIndex].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskIndex]);
                } else {
                    Task task = parseTask(input);
                    taskCount = addTask(tasks, taskCount, task);
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

    private static Task parseTask(String input) throws BenjaminException {
        if (input.equals("todo") || input.startsWith("todo ")) {
            String description = input.substring(4).trim();

            if (description.isEmpty()) {
                throw new BenjaminException("The description of a todo cannot be empty.");
            }

            return new Todo(description);
        }

        if (input.equals("deadline") || input.startsWith("deadline ")) {
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

        if (input.equals("event") || input.startsWith("event ")) {
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

    private static int addTask(Task[] tasks, int taskCount, Task task) throws BenjaminException {
        if (taskCount >= tasks.length) {
            throw new BenjaminException("The task list is full.");
        }

        tasks[taskCount] = task;
        int updatedTaskCount = taskCount + 1;

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + updatedTaskCount + " tasks in the list.");

        return updatedTaskCount;
    }
}
