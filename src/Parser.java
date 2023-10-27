import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    private String commandName;
    private String[] commandArgs;

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

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return Arrays.copyOf(commandArgs, commandArgs.length);
    }
}
