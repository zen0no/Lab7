package commands;

/**
 * Class of command that displays the elements of the collection in descending order
 */

import dataClasses.HumanBeing;
import exceptions.ConsoleException;
import exceptions.IncorrectArgumentConsoleException;
import manager.Console;
import net.Method;
import net.Request;
import net.Response;
import server.ServerConnection;

import java.util.Collections;
import java.util.List;

public class PrintDescendingCommand extends AbstractCommand {

    /**
     * Method to print descending sorted collection
     */
    @Override
    public boolean execute(List<String> args) throws ConsoleException {
        if (!validateArguments(args)){
            throw new IncorrectArgumentConsoleException("Incorrect argument exception");
        }
        Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.GET, null));
        if (response.code == Response.StatusCode.ERROR) {
            Console.printError((String) response.body);
            return false;
        }
        List<HumanBeing> col = (List<HumanBeing>) response.body;
        Collections.sort(col);
        Collections.reverse(col);
        for(HumanBeing h: col) {
            System.out.println(h.show());
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "displays the elements of the collection in descending order";
    }

    @Override
    public String getName() {
        return "print_descending";
    }

    @Override
    public boolean validateArguments(List<String> args) throws ConsoleException {
        return args.size() == 0;
    }
}
