package commands;

/**
 * Class of command that shuts down the application
 */

import exceptions.ConsoleException;
import exceptions.IncorrectArgumentConsoleException;
import manager.Console;
import net.Method;
import net.Request;
import net.Response;
import server.ServerConnection;

import java.util.List;

public class ExitCommand extends AbstractCommand{


    /**
     * Method to exit application without saving to file
     * @param args
     */
    @Override
    public boolean execute(List<String> args) {
       if (!validateArguments(args)) {
           throw new IncorrectArgumentConsoleException("Incorrect format for ExitCommand");
       }
        Console.exit();
        Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.EXIT, null));
       return true;
    }

    @Override
    public String getDescription() {
        return "shuts down the application";
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public boolean validateArguments(List<String> args) throws ConsoleException {
        return args.size() == 0;
    }
}
