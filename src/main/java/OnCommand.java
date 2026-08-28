import java.time.LocalDate;

/** Shows the tasks that fall on one particular date. */
public class OnCommand extends Command {
    private final LocalDate date;

    public OnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOn(date, tasks.findOn(date));
    }
}
