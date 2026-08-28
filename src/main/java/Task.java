import java.time.LocalDate;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns true if this task falls on the given date. Plain tasks carry no
     * date, so they never do; dated task types override this.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns the line used to represent this task in the save file.
     * Subclasses prepend their own type letter.
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
