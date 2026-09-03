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
 * <p>Replies are collected into a buffer rather than printed straight away.
 * The text interface empties that buffer to the console after each command,
 * while the graphical interface takes the same text and puts it in a dialog
 * box. Both interfaces therefore show exactly the same wording.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";

    private static final String GREETING = "Hello! I'm Benjamin.\nWhat can I do for you?";

    private static final String BANNER = " ____             _                 _\n"
            + "| __ )  ___ _ __ (_) __ _ _ __ ___ (_)_ __\n"
            + "|  _ \\ / _ \\ '_ \\| |/ _` | '_ ` _ \\| | '_ \\\n"
            + "| |_) |  __/ | | | | (_| | | | | | | | | | |\n"
            + "|____/ \\___|_| |_|/ |\\__,_|_| |_| |_|_|_| |_|\n"
            + "                |__/\n";

    private final StringBuilder buffer = new StringBuilder();
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

    /**
     * Returns everything shown since the last call and empties the buffer.
     *
     * @return the collected reply, which is empty if nothing was shown.
     */
    public String flush() {
        String reply = buffer.toString();
        buffer.setLength(0);

        return reply;
    }

    /** Writes everything shown since the last call to the console. */
    public void flushToConsole() {
        System.out.print(flush());
    }

    /** Shows the divider line used to separate replies in the text interface. */
    public void showLine() {
        print(DIVIDER);
    }

    /** Shows the banner and the opening greeting, for the text interface. */
    public void showWelcome() {
        print(BANNER + GREETING + "\n" + DIVIDER);
    }

    /** Shows the opening greeting without the banner, for the graphical interface. */
    public void showGreeting() {
        print(GREETING);
    }

    /** Shows the sign off line. The surrounding dividers are added by the caller. */
    public void showFarewell() {
        print("Bye. Hope to see you again soon!");
    }

    /**
     * Reports a problem to the user.
     *
     * @param message the wording to show, which is prefixed here so that every
     *     problem looks the same.
     */
    public void showError(String message) {
        print("OOPS!!! " + message);
    }

    /** Reports that the save file could not be read at all. */
    public void showLoadingError() {
        showError("I could not read your saved tasks, so I am starting empty.");
    }

    /**
     * Shows every task, numbered from one.
     *
     * @param tasks the list to show.
     */
    public void showTaskList(TaskList tasks) {
        print("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            print(String.format("%d.%s", i + 1, tasks.get(i)));
        }
    }

    /**
     * Reports the tasks that fall on the given date, or says there are none.
     *
     * @param date the day that was asked about.
     * @param matches the tasks falling on that day, possibly empty.
     */
    public void showTasksOn(LocalDate date, List<Task> matches) {
        print("Here are the tasks on " + TaskDateTime.formatDate(date) + ":");

        if (matches.isEmpty()) {
            print("There is nothing on that date.");
            return;
        }

        showNumbered(matches);
    }

    /**
     * Reports the tasks matching a search, or says that none do.
     *
     * @param matches the tasks whose description contained the keyword.
     */
    public void showMatchingTasks(List<Task> matches) {
        if (matches.isEmpty()) {
            print("There are no matching tasks in your list.");
            return;
        }

        print("Here are the matching tasks in your list:");
        showNumbered(matches);
    }

    /**
     * Confirms that a task was added.
     *
     * @param task the task just added.
     * @param taskCount how many tasks there are now.
     */
    public void showAdded(Task task, int taskCount) {
        print("Got it. I've added this task:", "  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task the task just removed.
     * @param taskCount how many tasks are left.
     */
    public void showRemoved(Task task, int taskCount) {
        print("Noted. I've removed this task:", "  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task the task in its new state.
     */
    public void showMarked(Task task) {
        print("Nice! I've marked this task as done:", "  " + task);
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task the task in its new state.
     */
    public void showUnmarked(Task task) {
        print("OK, I've marked this task as not done yet:", "  " + task);
    }

    /** Stops reading input. */
    public void close() {
        scanner.close();
    }

    private void showNumbered(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            print(String.format("%d.%s", i + 1, tasks.get(i)));
        }
    }

    private void showTaskCount(int taskCount) {
        print("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Adds one line per argument to the reply being built.
     *
     * <p>Taking a varargs list lets the callers that always show a fixed group
     * of lines say so in a single call.
     *
     * @param lines the lines to show, in order.
     */
    private void print(String... lines) {
        for (String line : lines) {
            buffer.append(line).append(System.lineSeparator());
        }
    }
}
