import java.util.Scanner;

public class Benjamin {
    public static void main(String[] args) {
        String banner = " ____             _                 _\n"
                + "| __ )  ___ _ __ (_) __ _ _ __ ___ (_)_ __\n"
                + "|  _ \\ / _ \\ '_ \\| |/ _` | '_ ` _ \\| | '_ \\\n"
                + "| |_) |  __/ | | | | (_| | | | | | | | | | |\n"
                + "|____/ \\___|_| |_|/ |\\__,_|_| |_| |_|_|_| |_|\n"
                + "                |__/\n";
        String greeting = "Hello! I'm Benjamin.\n"
                + "What can I do for you?\n";

        String divider = "————————————————————————————————————————————————————————————\n";
        

        Scanner scanner = new Scanner(System.in); 

        System.out.println(banner + greeting + "\n");

        String input = scanner.nextLine(); 

        while (input != "bye") {

            System.out.println(divider + input + "\n" + divider);

            input = scanner.nextLine(); 
        }

        String farewell = "Bye. Hope to see you again soon!";

        System.out.println(farewell);

        scanner.close(); 

    }
}
