package benjamin.task;

import java.time.LocalDate;

public class Event extends Task {
    protected TaskDateTime from;
    protected TaskDateTime to;

    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** An event covers every day from its start date to its end date. */
    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(from.getDate()) && !date.isAfter(to.getDate());
    }

    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + from.toStorageString()
                + " | " + to.toStorageString();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
