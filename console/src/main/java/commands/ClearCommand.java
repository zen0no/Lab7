package commands;

/*
 * Class of command that clears collection
 */

import dataClasses.HumanBeing;
import exceptions.IncorrectArgumentConsoleException;
import manager.Console;
import net.Method;
import net.Request;
import net.Response;
import server.ServerConnection;

import java.util.ArrayList;
import java.util.List;

public class ClearCommand extends AbstractCommand {


    /**
     * Method to clear collection
     */
    @Override
    public boolean execute(List<String> args) {
        if (!validateArguments(args)){
            throw new IncorrectArgumentConsoleException("ClearCommand does not require arguments");
        }
        Response res = ServerConnection.getConnection().sendRequest(new Request(user, Method.GET, null));
        if (res.code == Response.StatusCode.OK) {
            Console.print("Коллекия была очищена");
            for (HumanBeing h: (ArrayList<HumanBeing>) res.body){
                Console.print(h.shortShow());
            }
        }
        else Console.printError((String) res.body);
        return true;

    }

    @Override
    public boolean validateArguments(List<String> args){
        return args.size() == 0;
    }

    @Override
    public String getDescription()
    {
        return "clears collection";
    }

    @Override
    public String getName() {
        return "clear";
    }
}
