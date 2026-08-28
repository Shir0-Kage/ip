package benjamin.task;

import java.time.LocalDate;

/** A task that runs from one point in time to another. */
public class Event extends Task {
    /** When the event starts. */
    protected TaskDateTime from;

    /** When the event ends. */
    protected TaskDateTime to;

    /**
     * Creates an event that is not done yet.
     *
     * @param description what the event is.
     * @param from when it starts.
     * @param to when it ends.
     */
    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns true if the given day is covered by the event.
     *
     * <p>An event covers every day from its start date to its end date, and
     * both of those days count as covered.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(from.getDate()) && !date.isAfter(to.getDate());
    }

    /** Returns the save line, marked with the letter {@code E}. */
    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + from.toStorageString()
                + " | " + to.toStorageString();
    }

    /** Returns the event as shown to the user, with both ends in brackets. */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
