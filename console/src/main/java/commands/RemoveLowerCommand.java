package commands;

/**
 * Class of command that removes all elements smaller than the specified one from the collection
 */

import dataClasses.Car;
import dataClasses.Coordinates;
import dataClasses.HumanBeing;
import exceptions.*;
import manager.Console;
import net.Method;
import net.Request;
import net.Response;
import server.ServerConnection;
import utils.HumanBeingBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveLowerCommand extends AbstractCommand {
    private final HumanBeingBuilder builder = new HumanBeingBuilder();

    /**
     * Constructor of class
     */

    @Override
    public boolean execute(List<String> args) throws ConsoleException {
        if (!validateArguments(args)) {
            throw new IncorrectArgumentConsoleException("Incorrect argument exception");
        }
        try {
            builder.createTemp();
            Console.print("Enter element values:");
            for (String field : HumanBeing.getFields()) {

                Map<String, String> fieldArgs = new HashMap<>();
                if (field.equals("car")) {
                    for (String carField : Car.getFields()) {
                        Console.print("HumanBeing.car." + carField + ":");
                        fieldArgs.put(carField, Console.input());
                    }
                } else if (field.equals("coordinates")) {
                    for (String corField : Coordinates.getFields()) {
                        Console.print("HumanBeing.coordinates." + corField + ":");
                        fieldArgs.put(corField, Console.input());
                    }

                } else {
                    Console.print("HumanBeing." + field);
                    fieldArgs.put("value", Console.input());
                }
                try {
                    builder.build(field, fieldArgs);
                } catch (ModelFieldException e) {
                    Console.printError(e.getMessage());
                    return false;
                }
            }
            HumanBeing h = builder.get();
            ArrayList<HumanBeing> collection = (ArrayList<HumanBeing>) ServerConnection.getConnection().sendRequest(new Request(user, Method.GET, null)).body;

            List<HumanBeing> toRemove = collection.stream().filter((humanBeing -> humanBeing.compareTo(h) > 0)).toList();

            Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.DELETE, toRemove));
            if (response.code == Response.StatusCode.ERROR){
                Console.printError((String) response.body);
                return false;
            }

            for (HumanBeing e: (ArrayList<HumanBeing>) response.body){
                Console.print(e.shortShow());
            }

            return true;
        }
        catch (BuilderIsBusyException e){
            builder.clear();
            return false;
        }
        catch (BuilderException e){
            builder.clear();
            Console.printError(e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "removes all elements smaller than the specified one from the collection";
    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public boolean validateArguments(List<String> args) throws ConsoleException {
        return args.size() == 0;
    }
}
