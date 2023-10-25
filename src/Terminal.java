import java.nio.file.Path;
import java.util.Scanner;

public class Terminal {
    Parser parser;
    Path currentDirectory;

    public Terminal() {
        parser = new Parser();
        currentDirectory = Path.of(System.getProperty("user.dir"));
    }

    public static void showPrompt() {
        System.out.print("> ");
    }

    public void echo(String[] args) {
        for (String arg : args)
            System.out.print(arg + " ");
        System.out.println();
    }

    public void chooseCommandAction() {
        String commandName = parser.getCommandName();
        String[] commandArgs = parser.getArgs();
        if (commandName.equals("echo")) {
            echo(commandArgs);
        } else if (commandName.equals("exit")) {
            System.exit(0);
        }
    }

    public void runInterface() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showPrompt();
            String command = scanner.nextLine();
            if (parser.parse(command)) {
                chooseCommandAction();
            } else {
                System.out.println("Invalid command");
            }
        }
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.runInterface();
    }
}
