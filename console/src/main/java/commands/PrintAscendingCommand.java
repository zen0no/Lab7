package commands;

/**
 * Class of command that displays the elements of the collection in ascending order
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

public class PrintAscendingCommand extends AbstractCommand {

    /**
     * Constructor of class
     * @param repository
     */


    /**
     * Method to print ascending sorted collection
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
        for(HumanBeing h: col) {
            Console.print(h.show());
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "displays the elements of the collection in ascending order";
    }

    @Override
    public String getName() {
        return "print_ascending";
    }

    @Override
    public boolean validateArguments(List<String> args) throws ConsoleException {
        return args.size() == 0;
    }
}
