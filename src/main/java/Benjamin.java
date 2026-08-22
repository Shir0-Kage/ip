import java.util.Scanner;

/**
 * Runs the Benjamin chatbot and manages its in-memory task list.
 */
public class Benjamin {
    /**
     * Starts the chatbot's command loop.
     *
     * @param args command-line arguments, which are not used
     */
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
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("added: " + input);
            }

            System.out.println(divider);
        }

        String farewell = "Bye. Hope to see you again soon!";
        System.out.println(divider);
        System.out.println(farewell);
        System.out.println(divider);

        scanner.close();

    }
}
