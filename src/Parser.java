import java.util.Arrays;
public class Parser {
    private String commandName;
    private String[] commandArgs;
    public boolean parse(String command) {
        if (command.isEmpty())
            return false;
        command = command.trim();
        String[] commandParts = command.split(" ");
        commandName = commandParts[0];
        commandArgs = Arrays.copyOfRange(commandParts, 1, commandParts.length);
        return true;
    }
    public String getCommandName() {
        return commandName;
    }
    public String[] getArgs() {
        return Arrays.copyOf(commandArgs, commandArgs.length);
    }
}
