package benjamin.parser;

import java.time.LocalDate;

import benjamin.BenjaminException;
import benjamin.command.AddCommand;
import benjamin.command.Command;
import benjamin.command.DeleteCommand;
import benjamin.command.ExitCommand;
import benjamin.command.FindCommand;
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
    private static final String KEYWORD_TODO = "todo";
    private static final String KEYWORD_DEADLINE = "deadline";
    private static final String KEYWORD_EVENT = "event";
    private static final String KEYWORD_MARK = "mark";
    private static final String KEYWORD_UNMARK = "unmark";
    private static final String KEYWORD_DELETE = "delete";
    private static final String KEYWORD_FIND = "find";
    private static final String KEYWORD_ON = "on";

    private static final String MARKER_BY = "/by";
    private static final String MARKER_FROM = "/from";
    private static final String MARKER_TO = "/to";

    private static final String MESSAGE_UNKNOWN_COMMAND =
            "I'm sorry, but I don't know what that means :-(";

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
        case FIND:
            return new FindCommand(parseKeyword(input));
        case ON:
            return new OnCommand(parseDate(input));
        case MARK:
            return new MarkCommand(parseTaskNumber(input, KEYWORD_MARK));
        case UNMARK:
            return new UnmarkCommand(parseTaskNumber(input, KEYWORD_UNMARK));
        case DELETE:
            return new DeleteCommand(parseTaskNumber(input, KEYWORD_DELETE));
        case TODO:
        case DEADLINE:
        case EVENT:
            return new AddCommand(parseTask(input, commandType));
        default:
            throw new BenjaminException(MESSAGE_UNKNOWN_COMMAND);
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
            return parseTodo(input);
        }
        if (commandType == CommandType.DEADLINE) {
            return parseDeadline(input);
        }
        if (commandType == CommandType.EVENT) {
            return parseEvent(input);
        }

        throw new BenjaminException(MESSAGE_UNKNOWN_COMMAND);
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
     * Returns the text to search for, named by a {@code find} command.
     *
     * @throws BenjaminException if no keyword was given.
     */
    public static String parseKeyword(String input) throws BenjaminException {
        String keyword = input.substring(KEYWORD_FIND.length()).trim();

        if (keyword.isEmpty()) {
            throw new BenjaminException("Please provide a keyword after " + KEYWORD_FIND + ".");
        }

        return keyword;
    }

    /**
     * Returns the date named by an {@code on} command.
     *
     * @throws BenjaminException if no date was given or it cannot be read.
     */
    public static LocalDate parseDate(String input) throws BenjaminException {
        String dateText = input.substring(KEYWORD_ON.length()).trim();

        if (dateText.isEmpty()) {
            throw new BenjaminException("Please provide a date after " + KEYWORD_ON + ".");
        }

        return TaskDateTime.parse(dateText).getDate();
    }

    /** Returns the todo described by a {@code todo} command. */
    private static Task parseTodo(String input) throws BenjaminException {
        String description = input.substring(KEYWORD_TODO.length()).trim();

        if (description.isEmpty()) {
            throw new BenjaminException("The description of a todo cannot be empty.");
        }

        return new Todo(description);
    }

    /** Returns the deadline described by a {@code deadline} command. */
    private static Task parseDeadline(String input) throws BenjaminException {
        String arguments = input.substring(KEYWORD_DEADLINE.length()).trim();
        int byIndex = arguments.indexOf(MARKER_BY);

        if (byIndex < 0) {
            throw new BenjaminException("A deadline needs a " + MARKER_BY + " date or time.");
        }

        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + MARKER_BY.length()).trim();

        if (description.isEmpty()) {
            throw new BenjaminException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new BenjaminException("The " + MARKER_BY
                    + " date or time of a deadline cannot be empty.");
        }

        return new Deadline(description, TaskDateTime.parse(by));
    }

    /** Returns the event described by an {@code event} command. */
    private static Task parseEvent(String input) throws BenjaminException {
        String arguments = input.substring(KEYWORD_EVENT.length()).trim();
        int fromIndex = arguments.indexOf(MARKER_FROM);

        if (fromIndex < 0) {
            throw new BenjaminException("An event needs a " + MARKER_FROM + " date or time.");
        }

        int toIndex = arguments.indexOf(MARKER_TO, fromIndex + MARKER_FROM.length());

        if (toIndex < 0) {
            throw new BenjaminException("An event needs a " + MARKER_TO + " date or time.");
        }

        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + MARKER_FROM.length(), toIndex).trim();
        String to = arguments.substring(toIndex + MARKER_TO.length()).trim();

        if (description.isEmpty()) {
            throw new BenjaminException("The description of an event cannot be empty.");
        }
        if (from.isEmpty()) {
            throw new BenjaminException("The " + MARKER_FROM
                    + " date or time of an event cannot be empty.");
        }
        if (to.isEmpty()) {
            throw new BenjaminException("The " + MARKER_TO
                    + " date or time of an event cannot be empty.");
        }

        return new Event(description, TaskDateTime.parse(from), TaskDateTime.parse(to));
    }
}
