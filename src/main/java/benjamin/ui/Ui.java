package benjamin.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import benjamin.task.Task;
import benjamin.task.TaskDateTime;
import benjamin.task.TaskList;

/**
 * Deals with everything the user sees and types.
 *
 * <p>Keeping all reading and printing here means the rest of the program never
 * touches {@code System.in} or {@code System.out} directly, so the way the
 * chatbot talks can be changed in one place.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";

    private static final String BANNER = " ____             _                 _\n"
            + "| __ )  ___ _ __ (_) __ _ _ __ ___ (_)_ __\n"
            + "|  _ \\ / _ \\ '_ \\| |/ _` | '_ ` _ \\| | '_ \\\n"
            + "| |_) |  __/ | | | | (_| | | | | | | | | | |\n"
            + "|____/ \\___|_| |_|/ |\\__,_|_| |_| |_|_|_| |_|\n"
            + "                |__/\n";

    private final Scanner scanner;

    /** Creates a user interface that reads from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Returns true while the user still has a line of input to give. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Returns the next command typed by the user, without surrounding spaces. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Prints the divider line used to separate replies. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /** Prints the banner and the opening greeting. */
    public void showWelcome() {
        System.out.println(BANNER + "Hello! I'm Benjamin.\nWhat can I do for you?\n" + DIVIDER);
    }

    /** Prints the sign off line. The surrounding dividers are printed by the caller. */
    public void showFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Reports a problem to the user.
     *
     * @param message the wording to show, which is prefixed here so that every
     *     problem looks the same.
     */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    /** Reports that the save file could not be read at all. */
    public void showLoadingError() {
        showError("I could not read your saved tasks, so I am starting empty.");
    }

    /**
     * Prints every task, numbered from one.
     *
     * @param tasks the list to show.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d.%s%n", i + 1, tasks.get(i));
        }
    }

    /**
     * Reports the tasks that fall on the given date, or says there are none.
     *
     * @param date the day that was asked about.
     * @param matches the tasks falling on that day, possibly empty.
     */
    public void showTasksOn(LocalDate date, List<Task> matches) {
        System.out.println("Here are the tasks on " + TaskDateTime.formatDate(date) + ":");

        if (matches.isEmpty()) {
            System.out.println("There is nothing on that date.");
            return;
        }

        for (int i = 0; i < matches.size(); i++) {
            System.out.printf("%d.%s%n", i + 1, matches.get(i));
        }
    }

    /**
     * Confirms that a task was added.
     *
     * @param task the task just added.
     * @param taskCount how many tasks there are now.
     */
    public void showAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task the task just removed.
     * @param taskCount how many tasks are left.
     */
    public void showRemoved(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task the task in its new state.
     */
    public void showMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task the task in its new state.
     */
    public void showUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /** Stops reading input. */
    public void close() {
        scanner.close();
    }

    private void showTaskCount(int taskCount) {
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
