import commands.*;
import login.User;
import manager.CommandManager;
import manager.Console;
import manager.LoginManager;
import server.ServerConnection;

import java.util.*;

public class Main {
    public static void main(String[] args){

        Console.print("Greetings, user");

        Console.print("This is database app");

        Console.print("To see all commands type \"help\"");

        LoginManager manager = new LoginManager();

        User user = manager.askUser();

        ServerConnection.getConnection();
        CommandManager commandManager = new CommandManager(user);
        List<Command> commands = new ArrayList<>();
        Command c = new ClearCommand();
        commands.add(c);

        c = new ExecuteScriptCommand(new HashSet<>(), 0);
        commands.add(c);

        c = new ExitCommand();
        commands.add(c);

        c = new InsertCommand();
        commands.add(c);

        c = new InfoCommand();
        commands.add(c);

        c = new MaxByCreationDateCommand();
        commands.add(c);

        c = new PrintAscendingCommand();
        commands.add(c);

        c = new PrintDescendingCommand();
        commands.add(c);

        c = new RemoveKeyCommand();
        commands.add(c);

        c = new RemoveLowerCommand();
        commands.add(c);

        c = new ReplaceIfGreaterCommand();
        commands.add(c);

        c = new ShowCommand();
        commands.add(c);

        c = new UpdateCommand();
        commands.add(c);

        commandManager.addCommands(commands);

        commandManager.read();

    }
}

