import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parser class: Parses a command into a command name and arguments.
 * The command name is the first word of the command.
 * The arguments are the remaining words of the command.
 */
public class Parser {
    private String commandName;
    private String[] commandArgs;

    /**
     * Parses a command into a command name and arguments.
     *
     * @param command the full command to parse into a command name and arguments
     * @return true if the command was parsed successfully, false otherwise (Empty)
     */
    public boolean parse(String command) {
        command = command.trim(); // remove leading and trailing whitespace
        if (command.isEmpty())
            return false;
        // split on whitespace
        String[] commandParts = command.split(" ");

        // remove empty strings from commandParts
        ArrayList<String> nonEmptyCommandParts = new ArrayList<>();
        for (int i = 1; i < commandParts.length; i++) {
            if (!commandParts[i].isEmpty())
                nonEmptyCommandParts.add(commandParts[i]);
        }

        // set commandName and commandArgs
        commandName = commandParts[0];
        commandArgs = nonEmptyCommandParts.toArray(new String[0]);
        return true;
    }

    /**
     * Gets the command name.
     *
     * @return the command name
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Gets the command arguments.
     *
     * @return a copy of the command arguments array
     */
    public String[] getArgs() {
        return Arrays.copyOf(commandArgs, commandArgs.length);
    }

    /**
     * Gets the full command normalized (i.e. with no leading/trailing/extra whitespace).
     *
     * @return the full command normalized
     */
    public String getNormalizedCommand() {
        StringBuilder fullCommand = new StringBuilder(commandName);
        for (String arg : commandArgs) {
            fullCommand.append(" ").append(arg);
        }
        return fullCommand.toString();
    }
}
