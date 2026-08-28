package benjamin.task;

import java.time.LocalDate;

public class Deadline extends Task {
    protected TaskDateTime by;

    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.getDate().equals(date);
    }

    @Override
    public String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + by.toStorageString();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
