import java.nio.file.*;
import java.util.Scanner;
import java.io.IOException;

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

    // Main loop of the program interface
    public void runInterface() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showPrompt();
            String command = scanner.nextLine();
            if (parser.parse(command)) {
                chooseCommandAction();
            } // else do nothing (empty command)
        }
    }

    // Method that chooses which command to run
    public void chooseCommandAction() {
        String commandName = parser.getCommandName();
        String[] commandArgs = parser.getArgs();
        if (commandName.equals("echo")) {
            echo(commandArgs);
        } else if (commandName.equals("rm")) {
            rm(commandArgs);
        } else if (commandName.equals("pwd")) {
            pwd();
        } else if (commandName.equals("touch")) {
            touch(commandArgs);
        } else if (commandName.equals("cd")) {
            cd(commandArgs);
        } else if (commandName.equals("exit")) {
            System.exit(0);
        } else {
            System.out.println(commandName + ": command not found");
        }
    }


    // Command methods (called by chooseCommandAction)

    /**
     * echo command: prints the arguments passed to it
     *
     * @param args The array of arguments to print
     */
    public void echo(String[] args) {
        for (String arg : args)
            System.out.print(arg + " ");
        System.out.println();
    }

    // rm command: removes a file
    public void rm(String[] args) {
        String file = args[0];
        try {
            Path filePath = currentDirectory.resolve(file);
            Files.delete(filePath);
        } catch (NoSuchFileException e) {
            System.out.println("rm: cannot remove '" + file + "': No such file or directory");
        } catch (IOException e) {
            System.out.println("rm: cannot remove '" + file + "': Permission denied");
        }
    }

    /**
     * pwd command: prints the current directory
     */
    public void pwd() {
        currentDirectory = currentDirectory.normalize();
        System.out.println(currentDirectory);
    }

    /**
     * touch command: creates a file
     *
     * @param args The array of paths of files to create (currently only supports one file)
     */
    public void touch(String[] args) {
        // Make sure only one argument is passed
        if (args.length == 0) {
            System.out.println("touch: missing file operand");
            return;
        } else if (args.length > 1) {
            System.out.println("touch: too many arguments (currently only supports one file)");
            return;
        }
        // If one argument is passed
        String file = args[0];
        try {
            // Get the path of the file and create it
            Path filePath = currentDirectory.resolve(file);
            Files.createFile(filePath);
        } catch (FileAlreadyExistsException e) {
            // If the file already exists, do nothing (real touch simulation)
        } catch (IOException e) {
            System.out.println("touch: cannot create file '" + file + "': Permission denied or invalid path/file name");
        }
    }

    /**
     * cd command: changes the current directory, or goes to the home directory if no arguments are passed
     *
     * @param args The arguments array, which holds the path of the directory to change to (must have 0 or 1 arguments)
     */
    public void cd(String[] args) {
        // Make sure 0 or 1 arguments are passed
        if (args.length > 1) {
            System.out.println("cd: too many arguments");
            return;
        }

        // If no arguments are passed
        if (args.length == 0) {
            // Go to the home directory
            Path homeDir = Path.of(System.getProperty("user.home"));
            if (!Files.isDirectory(homeDir)) { // if home directory doesn't exist
                // print error message
                System.out.println("cd: cannot change directory to home directory: No such directory");
            } else {
                currentDirectory = homeDir;
            }
            return;
        }

        // If one argument is passed
        String dir = args[0];
        Path dirPath = currentDirectory.resolve(dir);
        if (Files.isDirectory(dirPath)) { // if the directory exists
            currentDirectory = dirPath; // change the current directory
        } else {
            // print error message
            System.out.println("cd: cannot change directory '" + dir + "': No such directory");
        }
    }

    /**
     * Entry point of the program
     * @param args The arguments passed to the program (not used)
     */
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.runInterface();
    }
}
