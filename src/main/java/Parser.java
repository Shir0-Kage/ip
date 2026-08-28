import java.time.LocalDate;

/**
 * Makes sense of what the user typed.
 *
 * <p>Every method here turns raw text into something the rest of the program
 * can use, and reports a clear problem when the text does not make sense.
 */
public class Parser {
    /** Returns the kind of command the input asks for. */
    public static CommandType parseCommandType(String input) {
        return CommandType.from(input);
    }

    /**
     * Returns the task described by an add command.
     *
     * @param input the full line typed by the user.
     * @param commandType which of the three task types to build.
     * @throws BenjaminException if a required part of the command is missing.
     */
    public static Task parseTask(String input, CommandType commandType) throws BenjaminException {
        if (commandType == CommandType.TODO) {
            String description = input.substring(4).trim();

            if (description.isEmpty()) {
                throw new BenjaminException("The description of a todo cannot be empty.");
            }

            return new Todo(description);
        }

        if (commandType == CommandType.DEADLINE) {
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

        if (commandType == CommandType.EVENT) {
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

    /**
     * Returns the zero based index named by a command such as {@code mark 2}.
     *
     * @param keyword the command word, used both to find the number and to
     *     word any problem message.
     * @param taskCount how many tasks there are, so the number can be checked.
     * @throws BenjaminException if the number is missing, not a number, or out of range.
     */
    public static int parseTaskIndex(String input, String keyword, int taskCount)
            throws BenjaminException {
        String taskNumberText = input.substring(keyword.length()).trim();

        if (taskNumberText.isEmpty()) {
            throw new BenjaminException("Please provide a task number after " + keyword + ".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new BenjaminException("The task number after " + keyword
                    + " must be a whole number.");
        }

        if (taskCount == 0) {
            throw new BenjaminException("There are no tasks to " + keyword + ".");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BenjaminException("Choose a task number between 1 and " + taskCount + ".");
        }

        return taskNumber - 1;
    }

    /**
     * Returns the date named by an {@code on} command.
     *
     * @throws BenjaminException if no date was given or it cannot be read.
     */
    public static LocalDate parseDate(String input) throws BenjaminException {
        String dateText = input.substring(2).trim();

        if (dateText.isEmpty()) {
            throw new BenjaminException("Please provide a date after on.");
        }

        return TaskDateTime.parse(dateText).getDate();
    }
}
