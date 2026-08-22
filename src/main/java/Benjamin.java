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
        
        String[] list_of_inputs = new String[100];
        int size = 0; 

        Scanner scanner = new Scanner(System.in); 

        System.out.println(banner + greeting + divider);

        String input = scanner.nextLine(); 

        while (!input.equals("bye")) {

            switch (input) {
                case "list": 
                    System.out.println(divider);
                    for (int i = 0; i < size; i++) {
                        String list_string = String.format("%d. %s", i + 1, list_of_inputs[i]);
                        System.out.println(list_string);
                    }
                    System.out.println(divider);
                    break;

                default:
                    list_of_inputs[size] = input;
                    size++; 
                    System.out.println("added: " + input + "\n");
                    break;
            }

            System.out.println(divider + input + "\n" + divider);

            input = scanner.nextLine(); 
        }

        String farewell = "Bye. Hope to see you again soon!";

        System.out.println(farewell);

        scanner.close(); 

    }
}
