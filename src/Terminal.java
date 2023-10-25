import java.util.Scanner;

public class Terminal {
    Parser parser;

    public Terminal() {
        parser = new Parser();
    }

    public static void showPrompt() {
        System.out.print("> ");
    }

    public void echo(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].isEmpty()) continue;
            System.out.print(args[i] + " ");
        }
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
