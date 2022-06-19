package commands;


/**
 * Class of command that outputs any collection element whose creationDate field value is the maximum
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

public class MaxByCreationDateCommand extends AbstractCommand{
    @Override
    public boolean execute(List<String> args){
        if (!validateArguments(args)){
            throw new IncorrectArgumentConsoleException("Incorrect argument exception for MaxByCreationDateCommand");
        }
       Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.GET, null));
       if (response.code == Response.StatusCode.ERROR) {Console.printError((String) response.body); return false;}
       List<HumanBeing> elements = (List<HumanBeing>) response.body;
       Console.print(elements.stream().max((o1, o2) -> o1.getCreationDate().compareTo(o2.getCreationDate())).get().show());
       return true;
    }

    @Override
    public String getDescription() {
        return "outputs collection element with maximum creationDate value";
    }

    @Override
    public String getName() {
        return "max_by_creation";
    }

    @Override
    public boolean validateArguments(List<String> args) throws ConsoleException {
        return args.size() == 0;
    }
}
