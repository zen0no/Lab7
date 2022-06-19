package commands;

/**
 * Class of command that returns information about Collection
 */

import exceptions.ConsoleException;
import exceptions.IncorrectArgumentConsoleException;
import manager.Console;
import net.Method;
import net.Request;
import net.Response;
import other.InfoBlock;
import server.ServerConnection;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class InfoCommand extends AbstractCommand{


    @Override
    public boolean execute(List<String> args) {
        if (!validateArguments(args)){
            throw new IncorrectArgumentConsoleException("InfoCommand does not require arguments");
        }
         Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.INFO, null));

            if (response.code == Response.StatusCode.ERROR){
                Console.printError((String) response.body);
                return false;
            }

            InfoBlock info = (InfoBlock) response.body;
            Console.print("Collection type: " + info.typeName);
            Console.print("Initialization date: " + info.dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z")));
            Console.print("Collection size: " + info.size);
            return true;
    }

    @Override
    public String getDescription() {
        return "returns information about Collection (type, date of initialization, quantity of elements etc.)";
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public boolean validateArguments(List<String> args) throws ConsoleException {
        return args.size() == 0;
    }
}
