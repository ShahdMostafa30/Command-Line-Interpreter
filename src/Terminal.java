import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.io.IOException;

/**
 * Command interface: used to hold the methods of the supported commands, to be used in the commands HashMap
 */
interface Command {
    void execute(String[] args);
}

/**
 * Terminal class: the main class of the program, which contains the main method and the methods of the supported commands
 * The terminal class is responsible for executing the user commands, and printing the output.
 */
public class Terminal {
    Parser parser;
    Path currentDirectory;
    private ArrayList<String> commandHistory;
    private HashMap<String, Command> commands;

    public Terminal() {
        parser = new Parser();
        currentDirectory = Path.of(System.getProperty("user.dir"));
        commandHistory = new ArrayList<>();
        initCommands();
    }

    /**
     * Initializes the commands HashMap with the supported commands
     */
    private void initCommands() {
        commands = new HashMap<>();
        // Methods that take a String[] as an argument
        commands.put("echo", this::echo);
        commands.put("cd", this::cd);
        commands.put("ls", this::ls);
        commands.put("mkdir", this::mkdir);
        commands.put("rmdir", this::rmdir);
        commands.put("touch", this::touch);
        commands.put("rm", this::rm);
        commands.put("cat", this::cat);
        commands.put("cp", this::cp);
        // Methods that take no arguments
        commands.put("pwd", (String[] args) -> pwd());
        commands.put("exit", (String[] args) -> System.exit(0));
        commands.put("history", (String[] args) -> history());
        commands.put("help", (String[] args) -> help());
    }

    /**
     * Prints the prompt of the terminal, which is the current directory
     */
    public void showPrompt() {
        currentDirectory = currentDirectory.normalize(); // Normalize the path to remove redundant parts
        System.out.print(currentDirectory + "> ");
    }

    /**
     * Runs the terminal interface until the user exits
     */
    public void runInterface() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showPrompt();
            String command = scanner.nextLine();
            if (parser.parse(command)) {
                String parsedCommand = parser.getCommandName();
                if (isCommandAvailable(parsedCommand)) {
                    commandHistory.add(command); // Add the command to the history
                    chooseCommandAction();
                } else {
                    System.out.println(parsedCommand + ": command not found");
                }
            } // else do nothing (empty command)
        }
    }

    /**
     * Executes the command that was parsed by the parser
     */
    public void chooseCommandAction() {
        commands.get(parser.getCommandName()).execute(parser.getArgs());
    }

    /**
     * Checks if a command is available in the list of supported commands.
     *
     * @param command The command to check for availability
     * @return true if the command is available, false otherwise
     */
    private boolean isCommandAvailable(String command) {
        return commands.containsKey(command);
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

    /**
     * ls command: lists the contents of the current directory
     *
     * @param args The array of arguments to print (currently only supports 1 argument, which is -r)
     */
    public void ls(String[] args) {
        File[] contents = currentDirectory.toFile().listFiles();
        try {
            if (args.length > 0 && args[0].equals("-r")) {
                Arrays.sort(contents, Comparator.reverseOrder());
            } else if (args.length > 1) {
                System.out.println("ls: too many arguments (currently only supports one argument)");
                return;
            } else if (args.length > 0) {
                System.out.println("ls: invalid argument (currently only supports -r)");
                return;
            }
            for (File file : contents) {
                System.out.println(file.getName());
            }
        } catch (SecurityException | NullPointerException e) {
            System.out.println("ls: failed to list contents of '" + currentDirectory + "': Permission denied");
        }
    }

    /**
     * cp command: copies a file/directory to another location
     *
     * @param args The array of paths of files/directories to copy (currently only supports two files/directories)
     */
    public void cp(String[] args){
        if(args.length == 0 ||(args.length ==1 && args[0] =="-r")) {
            System.out.println("cp: missing file operand");
            return;
        }

        else if(args.length > 3 && args[0] =="-r") {
            System.out.println("cp: too many arguments");
            return;
        }
        else if(args.length ==2 && args[0]=="-r"){
            System.out.println("cp: missing destination file operand after '" + args[1] + "'");
            return;
        }
        else if(args.length ==1 && args[0]!="-r"){
            System.out.println("cp: missing destination file operand after '" + args[0] + "'");
            return;
        }
        else{
            String src, dest;
            if(args.length == 3 && args[0].equals("-r")) {
                src = args[1];
                dest = args[2];
            }
            else{
                src = args[0];
                dest = args[1];
            }
            try {
                if (args.length == 3 && args[0].equals("-r")) {
                    copyDirectory(new File(src), new File(dest));
                    System.out.println("Directory copied successfully.");
                    return;
                }
                Path srcPath = currentDirectory.resolve(src);
                Path destPath = currentDirectory.resolve(dest);
                Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied successfully.");

            } catch (IOException e) {
                System.out.println("cp: failed to copy '" + src + "': Permission denied");
            }
        }
    }


    // Helper method for cp -r command to copy directories
    public static void copyDirectory(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            // Create the destination directory if it doesn't exist
            if (!destination.exists()) {
                destination.mkdir();
            }
            // List all files and directories in the source directory
            String[] files = source.list();

            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(source, file);
                    File destFile = new File(destination, file);

                    // Recursively copy subdirectories and their contents
                    copyDirectory(srcFile, destFile);
                }
            }
        } else {
            // Copy a file from source to destination
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }



    /**
     * isEmptyDir: checks if a directory is empty or not
     *
     * @param dir The directory to check
     * @return true if the directory is empty, false otherwise
     */
    public boolean isEmptyDir(File dir) {
        String[] contents = dir.list();
        return contents == null || contents.length == 0;
    }

    /**
     * rmdir command: removes a directory
     *
     * @param args The array of directory paths to be removed (currently only supports one file)
     */
    public void rmdir(String[] args) {
        if (args.length == 0) {
            System.out.println("rmdir: missing file operand");
            return;
        } else if (args.length > 1) {
            System.out.println("rmdir: too many arguments (currently only supports one argument)");
            return;
        }

        String dir = args[0];
        if (dir.equals("*")) {
            File[] contents = currentDirectory.toFile().listFiles();
            if (contents != null) {
                for (File directory : contents) {
                    if (directory.isDirectory() && isEmptyDir(directory)) {
                        try {
                            Files.delete(directory.toPath());
                        } catch (IOException e) {
                            System.out.println("rmdir: failed to remove '" + directory.getName() + "': Permission denied");
                        }
                    }
                }
            }
        } else {
            try {
                Path dirPath = currentDirectory.resolve(dir);
                if (!Files.isDirectory(dirPath)) {
                    System.out.println("rmdir: failed to remove '" + dir + "': Not a directory");
                } else if (!isEmptyDir(dirPath.toFile())) {
                    System.out.println("rmdir: failed to remove '" + dir + "': Directory not empty");
                } else
                    Files.delete(dirPath);
            } catch (NoSuchFileException e) {
                System.out.println("rmdir: failed to remove '" + dir + "': No such file or directory");
            } catch (IOException e) {
                System.out.println("rmdir: failed to remove '" + dir + "': Permission denied");
            } catch (InvalidPathException e) {
                System.out.println("rmdir: failed to remove '" + dir + "': Invalid Path");
            }
        }
    }

    /**
     * mkdir command: creates a directory
     * If the directory already exists, it prints an error message
     *
     * @param args The array of directory paths to be created
     */
    public void mkdir(String[] args) {
        if (args.length < 1) {
            System.out.println("mkdir: needs at least one argument");
            return;
        }
        try {
            for (String dir : args) {
                Path DirPath = currentDirectory.resolve(dir);
                File directory = new File(DirPath.toString());
                if (directory.exists())
                    System.out.println("\"Directory already exists at: \"" + directory.toPath());
                else {
                    directory.mkdir();
                }
            }
        } catch (InvalidPathException e) {
            System.out.println("mkdir: failed to create directory '" + args[0] + "': Invalid path");
        } catch (SecurityException e) {
            System.out.println("mkdir: failed to create directory '" + args[0] + "': Permission denied");
        }
    }

    /**
     * rm command: removes a file
     *
     * @param args The array of file paths to be removed (currently only supports one file)
     */
    public void rm(String[] args) {
        if (args.length == 1) {
            String file = args[0];
            try {
                Path filePath = currentDirectory.resolve(file);
                // Check if the path points to a regular file before deleting
                if (Files.isRegularFile(filePath)) {
                    Files.delete(filePath);
                } else {
                    System.out.println("rm: cannot remove '" + file + "': Not a regular file");
                }
            } catch (NoSuchFileException e) {
                System.out.println("rm: cannot remove '" + file + "': No such file or directory");
            } catch (IOException e) {
                System.out.println("rm: cannot remove '" + file + "': Permission denied");
            } catch (InvalidPathException e) {
                System.out.println("rm: failed to remove '" + args[0] + "': Invalid path");
            }
        } else {
            System.out.println("rm: Invalid number of arguments");
        }
    }

    /**
     * cat command: prints the content of a file or concatenates the content of two files and prints it
     *
     * @param args The array of file paths to be printed or concatenated (one or two files)
     */
    public void cat(String[] args) {
        if (args.length == 1 || args.length == 2) {
            // Iterate over each argument to process
            for (String arg : args) {
                try {
                    Path filePath = currentDirectory.resolve(arg);
                    System.out.println(Files.readString(filePath));  // Read and print the content of the file
                } catch (NoSuchFileException e) {
                    System.out.println("cat: " + arg + ": No such file or directory");
                } catch (IOException e) {
                    System.out.println("cat: " + arg + ": Error reading the file");
                } catch (InvalidPathException e) {
                    System.out.println("cat: " + arg + ": Invalid path");
                }
            }
        } else {
            System.out.println("cat: Invalid number of arguments");
        }
    }


    /**
     * history command: displays an enumerated list of past commands
     */
    public void history() {
        if (commandHistory.isEmpty()) {
            System.out.println("No commands in history");
        } else {
            for (int i = 0; i < commandHistory.size(); i++) {
                System.out.println((i + 1) + " " + commandHistory.get(i));
            }
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
     * help command: prints the list of supported commands
     */
    public void help() {
        System.out.println("1.help     -> prints the list of supported commands");
        System.out.println("2.echo     -> prints the arguments passed to it");
        System.out.println("3.pwd      -> prints the current working directory");
        System.out.println("4.cd       -> changes the current working directory");
        System.out.println("5.ls       -> lists the contents of the current directory");
        System.out.println("6.ls -r    -> lists the contents of the current directory in reverse order");
        System.out.println("7.cp       -> copies a file to a new location");
        System.out.println("8.cp -r    -> copies a directory to a new location");
        System.out.println("9.history  -> prints the last 5 commands");
        System.out.println("10.mkdir   -> creates a new directory");
        System.out.println("11.rmdir   -> removes an empty directory");
        System.out.println("12.touch   -> creates a new file");
        System.out.println("13.rm      -> removes a file");
        System.out.println("14.cat     -> prints the contents of a file");
        System.out.println("15.exit    -> exits the terminal");
    }

    /**
     * Entry point of the program
     *
     * @param args The arguments passed to the program (not used)
     */
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.runInterface();
    }
}
