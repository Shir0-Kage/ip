package benjamin.task;

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

    /**
     * Creates a task list holding the given tasks, such as those just loaded
     * from the save file.
     *
     * @param tasks the tasks to start with, in the order they should appear.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns a task list holding the given tasks, in the order given.
     *
     * <p>A varargs factory keeps callers that know their tasks up front, such
     * as tests, from having to build a list and add to it one call at a time.
     *
     * @param tasks the tasks to start with.
     */
    public static TaskList of(Task... tasks) {
        TaskList taskList = new TaskList();

        for (Task task : tasks) {
            taskList.add(task);
        }

        return taskList;
    }

    /** Returns how many tasks are in the list. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given zero based index.
     *
     * @throws IndexOutOfBoundsException if there is no task at that index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given zero based index.
     *
     * @throws IndexOutOfBoundsException if there is no task at that index.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the tasks that fall on the given date, in list order.
     *
     * @param date the day being asked about.
     * @return the matching tasks, which is empty if none fall on that day.
     */
    public List<Task> findOn(LocalDate date) {
        List<Task> matches = new ArrayList<>();

        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matches.add(task);
            }
        }

        return matches;
    }

    /**
     * Returns the tasks whose description contains the given text, in list order.
     *
     * @param keyword the text being searched for, matched without regard to case.
     * @return the matching tasks, which is empty if none match.
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();

        for (Task task : tasks) {
            if (task.hasKeyword(keyword)) {
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
