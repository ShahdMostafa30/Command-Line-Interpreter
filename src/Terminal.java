import java.io.File;
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
        } else if (commandName.equals("rmdir")) {
                rmdir(commandArgs);
        } else if (commandName.equals("mkdir")) {
                mkdir(commandArgs);
        } else if (commandName.equals("exit")) {
            System.exit(0);
        }
    }


    // Command methods (called by chooseCommandAction)

    public void echo(String[] args) {
        for (String arg : args)
            System.out.print(arg + " ");
        System.out.println();
    }
    public boolean isEmptyDir(File dir){
        String[] contents = dir.list();
        return contents.length == 0 || contents == null;
    }
    public void rmdir(String[] args){
        if(args.length > 1)
        {
            System.out.println("invalid number of arguments");
            return;
        }

        String dir = args[0];
        if(dir.equals("*")){
            File[] contents = currentDirectory.toFile().listFiles();
            for(File directory : contents){
                if(directory.isDirectory() && isEmptyDir(directory)){
                    try {
                        Files.delete(directory.toPath());
                    } catch (IOException e) {
                        System.out.println("rmdir: failed to remove '" + directory.getName() + "': Permission denied");
                    }
                }
            }
        }
        else{
            try {
                Path dirPath = currentDirectory.resolve(dir);
                if(!Files.isDirectory(dirPath)){
                    System.out.println("rmdir: failed to remove '" + dir + "': Not a directory");
                }
                else if(!isEmptyDir(dirPath.toFile())){
                    System.out.println("rmdir: failed to remove '" + dir + "': Directory not empty");
                }
                else
                    Files.delete(dirPath);
            } catch (NoSuchFileException e) {
                System.out.println("rmdir: failed to remove '" + dir + "': No such file or directory");
            } catch (IOException e) {
                System.out.println("rmdir: failed to remove '" + dir + "': Permission denied");
            }catch (InvalidPathException e){
                System.out.println("rmdir: failed to remove '" + dir + "': Invalid Path");
            }
        }
    }

    public void mkdir(String[] args){
        if(args.length < 1)
        {
            System.out.println("invalid input");
            return;
        }

        for(String dir : args){
            Path dirPath = Paths.get(dir);
            if(dirPath.isAbsolute())
            {
                try{
                    File directory = new File(dir);
                    if(directory.exists())
                        System.out.println("\"Directory already exists at: \"" + directory.toPath());
                    else{
                        directory.mkdir();
                    }

                }catch (Exception e) {
                    System.out.println("Error occurred while creating directory: " + e.getMessage());
                }
            }
            else{
                try{
                    Path newDirPath = currentDirectory.resolve(dir);
                    File directory = new File(newDirPath.toString());
                    if(directory.exists())
                        System.out.println("\"Directory already exists at: \"" + directory.toPath());
                    else{
                        directory.mkdir();
                    }
                } catch (InvalidPathException e){
                    System.out.println("mkdir: failed to make directory '" + dir + "': Invalid Path");
                } catch (Exception e) {
                    System.out.println("Error occurred while creating directory: " + e.getMessage());
                }
            }
        }
    }

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

    // Entry point of the program
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.runInterface();
    }
}
