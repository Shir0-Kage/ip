package benjamin.parser;

import java.time.LocalDate;

import benjamin.BenjaminException;
import benjamin.command.AddCommand;
import benjamin.command.Command;
import benjamin.command.DeleteCommand;
import benjamin.command.ExitCommand;
import benjamin.command.ListCommand;
import benjamin.command.MarkCommand;
import benjamin.command.OnCommand;
import benjamin.command.UnmarkCommand;
import benjamin.task.Deadline;
import benjamin.task.Event;
import benjamin.task.Task;
import benjamin.task.TaskDateTime;
import benjamin.task.Todo;

/**
 * Makes sense of what the user typed.
 *
 * <p>Every method here turns raw text into something the rest of the program
 * can use, and reports a clear problem when the text does not make sense.
 */
public class Parser {
    /**
     * Returns the command described by a line of user input.
     *
     * <p>Only the wording is checked here. Anything that depends on the tasks
     * currently in the list, such as whether a task number exists, is checked
     * when the command runs.
     *
     * @throws BenjaminException if the line cannot be understood.
     */
    public static Command parse(String input) throws BenjaminException {
        CommandType commandType = CommandType.from(input);

        switch (commandType) {
        case BYE:
            return new ExitCommand();
        case LIST:
            return new ListCommand();
        case ON:
            return new OnCommand(parseDate(input));
        case MARK:
            return new MarkCommand(parseTaskNumber(input, "mark"));
        case UNMARK:
            return new UnmarkCommand(parseTaskNumber(input, "unmark"));
        case DELETE:
            return new DeleteCommand(parseTaskNumber(input, "delete"));
        case TODO:
        case DEADLINE:
        case EVENT:
            return new AddCommand(parseTask(input, commandType));
        default:
            throw new BenjaminException("I'm sorry, but I don't know what that means :-(");
        }
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
     * Returns the one based task number named by a command such as {@code mark 2}.
     *
     * @param input the full line typed by the user.
     * @param keyword the command word, used both to find the number and to
     *     word any problem message.
     * @throws BenjaminException if the number is missing or is not a whole number.
     */
    public static int parseTaskNumber(String input, String keyword) throws BenjaminException {
        String taskNumberText = input.substring(keyword.length()).trim();

        if (taskNumberText.isEmpty()) {
            throw new BenjaminException("Please provide a task number after " + keyword + ".");
        }

        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new BenjaminException("The task number after " + keyword
                    + " must be a whole number.");
        }
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
