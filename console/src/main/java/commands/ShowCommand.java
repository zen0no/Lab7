package commands;

/**
 * Class of command that shows collection for user
 */

import dataClasses.HumanBeing;
import exceptions.ConsoleException;
import exceptions.IncorrectArgumentConsoleException;
import manager.Console;
import net.Method;
import net.Request;
import net.Response;
import server.ServerConnection;

import java.util.List;

public class ShowCommand extends AbstractCommand {

    /**
     * Method to show collection
     */
    @Override
    public boolean execute(List<String> args) throws ConsoleException {
        if (!validateArguments(args)) {
            throw new IncorrectArgumentConsoleException("Incorrect argument exception");
        }
        Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.GET, null));

        if (response.code == Response.StatusCode.ERROR){
            Console.printError((String) response.body);
        }

        List<HumanBeing> col = (List<HumanBeing>) response.body;

        if (col.isEmpty()) Console.print("Collection is empty");
        for (HumanBeing h : col) {
            Console.print(h.show());}
        return true;
    }

    @Override
    public String getDescription() {
        return "shows collection for user";
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public boolean validateArguments(List<String> args) throws ConsoleException {
        return args.size() == 0;
    }
}
