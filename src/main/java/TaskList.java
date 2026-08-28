import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The list of tasks, together with the operations that change it.
 *
 * <p>The backing list is kept private so that callers go through these
 * operations rather than editing the list directly.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Creates a task list holding the given tasks, such as those just loaded from disk. */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the given zero based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the tasks that fall on the given date, in list order. */
    public List<Task> findOn(LocalDate date) {
        List<Task> matches = new ArrayList<>();

        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matches.add(task);
            }
        }

        return matches;
    }

    /** Returns one save file line per task, in list order. */
    public List<String> toSaveFormat() {
        List<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            lines.add(task.toSaveFormat());
        }

        return lines;
    }
}
