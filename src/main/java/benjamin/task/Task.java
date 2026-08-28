package benjamin.task;

import java.time.LocalDate;

/**
 * A single thing the user wants to keep track of.
 *
 * <p>This base class holds what every task has, namely a description and
 * whether it is done. Subclasses add whatever else their kind of task needs,
 * such as a due date, and decide how the task is shown and saved.
 */
public class Task {
    /** What the user wants to be reminded of. */
    protected String description;

    /** Whether the user has finished this task. */
    protected boolean isDone;

    /**
     * Creates a task that is not done yet.
     *
     * @param description what the user typed as the task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the mark shown inside the status box.
     *
     * @return {@code X} when the task is done, or a space when it is not.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Records that this task has been completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Records that this task still has to be done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns true if this task falls on the given date. Plain tasks carry no
     * date, so they never do; dated task types override this.
     *
     * @param date the day being asked about.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns true if the description contains the given text.
     *
     * <p>Capitalisation is ignored, so searching for {@code Book} finds a task
     * described as {@code read book}.
     *
     * @param keyword the text being searched for.
     */
    public boolean hasKeyword(String keyword) {
        return description.toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * Returns the line used to represent this task in the save file.
     * Subclasses prepend their own type letter.
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    /** Returns the task as shown to the user, such as {@code [X] read book}. */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
