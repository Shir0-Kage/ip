package benjamin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests the entry points the graphical interface relies on.
 */
public class BenjaminTest {
    @TempDir
    Path root;

    private Benjamin chatbot() {
        return new Benjamin(root.toString(), "data", "benjamin.txt");
    }

    @Test
    public void getWelcome_freshStart_greetsWithoutTheTextBanner() {
        String welcome = chatbot().getWelcome();

        assertTrue(welcome.contains("Hello! I'm Benjamin."));
        assertTrue(welcome.contains("What can I do for you?"));
        assertFalse(welcome.contains("____"));
    }

    @Test
    public void getResponse_addTask_confirmsTheTask() {
        String reply = chatbot().getResponse("todo read book");

        assertTrue(reply.contains("Got it. I've added this task:"));
        assertTrue(reply.contains("[T][ ] read book"));
    }

    @Test
    public void getResponse_unknownCommand_reportsInsteadOfThrowing() {
        String reply = chatbot().getResponse("blah");

        assertTrue(reply.contains("I don't know what that means"));
    }

    @Test
    public void getResponse_badTaskNumber_reportsInsteadOfThrowing() {
        String reply = chatbot().getResponse("mark 99");

        assertTrue(reply.contains("There are no tasks to mark."));
    }

    @Test
    public void getResponse_eachCall_returnsOnlyTheLatestReply() {
        Benjamin benjamin = chatbot();

        benjamin.getResponse("todo read book");
        String second = benjamin.getResponse("todo join sports club");

        assertTrue(second.contains("join sports club"));
        assertFalse(second.contains("read book"));
    }

    @Test
    public void isExit_beforeBye_isFalse() {
        Benjamin benjamin = chatbot();
        benjamin.getResponse("list");

        assertFalse(benjamin.isExit());
    }

    @Test
    public void isExit_afterBye_isTrueAndSaysGoodbye() {
        Benjamin benjamin = chatbot();
        String reply = benjamin.getResponse("bye");

        assertTrue(reply.contains("Bye. Hope to see you again soon!"));
        assertTrue(benjamin.isExit());
    }

    @Test
    public void getResponse_findCommand_reportsMatches() {
        Benjamin benjamin = chatbot();
        benjamin.getResponse("todo read book");
        benjamin.getResponse("todo join sports club");

        String reply = benjamin.getResponse("find book");

        assertTrue(reply.contains("Here are the matching tasks in your list:"));
        assertTrue(reply.contains("read book"));
        assertFalse(reply.contains("sports club"));
    }

    @Test
    public void getResponse_tasksAddedEarlier_surviveIntoANewChatbot() {
        chatbot().getResponse("todo read book");

        assertTrue(chatbot().getResponse("list").contains("[T][ ] read book"));
    }
}
