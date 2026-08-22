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

            if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.printf("%d.%s%n", i + 1, tasks[i]);
                }
            } else if (input.equals("mark") || input.startsWith("mark ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(4).trim());
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("Please enter a valid task number.");
                    } else {
                        tasks[taskIndex].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please enter a valid task number.");
                }
            } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(6).trim());
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("Please enter a valid task number.");
                    } else {
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please enter a valid task number.");
                }
            } else if (input.equals("todo") || input.startsWith("todo ")) {
                String description = input.substring(4).trim();
                if (description.isEmpty()) {
                    System.out.println("Please enter a task description after todo.");
                } else {
                    taskCount = addTask(tasks, taskCount, new Todo(description));
                }
            } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                String arguments = input.substring(8).trim();
                int byIndex = arguments.indexOf(" /by ");

                if (byIndex < 0) {
                    System.out.println("Please use: deadline <description> /by <date/time>");
                } else {
                    String description = arguments.substring(0, byIndex).trim();
                    String by = arguments.substring(byIndex + 5).trim();

                    if (description.isEmpty() || by.isEmpty()) {
                        System.out.println("Please use: deadline <description> /by <date/time>");
                    } else {
                        taskCount = addTask(tasks, taskCount, new Deadline(description, by));
                    }
                }
            } else if (input.equals("event") || input.startsWith("event ")) {
                String arguments = input.substring(5).trim();
                int fromIndex = arguments.indexOf(" /from ");
                int toIndex = arguments.indexOf(" /to ", fromIndex + 7);

                if (fromIndex < 0 || toIndex < 0) {
                    System.out.println("Please use: event <description> /from <date/time> /to <date/time>");
                } else {
                    String description = arguments.substring(0, fromIndex).trim();
                    String from = arguments.substring(fromIndex + 7, toIndex).trim();
                    String to = arguments.substring(toIndex + 5).trim();

                    if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        System.out.println("Please use: event <description> /from <date/time> /to <date/time>");
                    } else {
                        taskCount = addTask(tasks, taskCount, new Event(description, from, to));
                    }
                }
            } else {
                System.out.println("Please enter a valid command.");
            }

            System.out.println(divider);
        }

        String farewell = "Bye. Hope to see you again soon!";
        System.out.println(divider);
        System.out.println(farewell);
        System.out.println(divider);

        scanner.close();
    }

    private static int addTask(Task[] tasks, int taskCount, Task task) {
        tasks[taskCount] = task;
        int updatedTaskCount = taskCount + 1;

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + updatedTaskCount + " tasks in the list.");

        return updatedTaskCount;
    }
}
