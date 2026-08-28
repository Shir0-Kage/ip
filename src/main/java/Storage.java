import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from the save file and writes them back to it.
 *
 * <p>The path is built from separate name parts rather than one string with
 * slashes in it, so the same code works on any operating system.
 */
public class Storage {
    private final Path file;
    private final List<String> loadWarnings = new ArrayList<>();

    /**
     * Creates a storage backed by the given path, relative to the project root.
     *
     * @param first the first part of the path, such as {@code data}.
     * @param more the remaining parts, such as {@code benjamin.txt}.
     */
    public Storage(String first, String... more) {
        this.file = Paths.get(first, more);
    }

    /**
     * Returns the saved tasks. A missing file simply means nothing has been
     * saved yet, so an empty list is returned. Lines that are not in the
     * expected format are skipped and recorded in {@link #getLoadWarnings()},
     * so one bad line does not cost the user the rest of the list.
     *
     * @throws BenjaminException if the file exists but cannot be read at all.
     */
    public ArrayList<Task> load() throws BenjaminException {
        loadWarnings.clear();

        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(file)) {
            return tasks;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(file);
        } catch (IOException exception) {
            throw new BenjaminException("I could not read " + file + ".");
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            try {
                tasks.add(parseSavedTask(line));
            } catch (BenjaminException exception) {
                loadWarnings.add("Skipping line " + (i + 1) + " of the save file because "
                        + exception.getMessage());
            }
        }

        return tasks;
    }

    /** Returns the messages for lines skipped during the most recent load. */
    public List<String> getLoadWarnings() {
        return loadWarnings;
    }

    /**
     * Writes the whole task list to the save file, creating the folder first
     * if it is not there yet.
     *
     * @throws BenjaminException if the tasks cannot be written.
     */
    public void save(TaskList tasks) throws BenjaminException {
        try {
            Path folder = file.getParent();

            if (folder != null) {
                Files.createDirectories(folder);
            }

            Files.write(file, tasks.toSaveFormat());
        } catch (IOException exception) {
            throw new BenjaminException("I could not save your tasks.");
        }
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
}
