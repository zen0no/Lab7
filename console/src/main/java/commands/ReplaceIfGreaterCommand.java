package commands;

/**
 * Class of command that replaces the value by key if the new value is greater than the old one
 */

import dataClasses.Car;
import dataClasses.Coordinates;
import dataClasses.HumanBeing;
import exceptions.*;
import manager.Console;
import net.Method;
import net.Request;
import net.Response;
import other.Predicates;
import server.ServerConnection;
import utils.HumanBeingBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplaceIfGreaterCommand extends AbstractCommand {
    private final HumanBeingBuilder builder = new HumanBeingBuilder();


    @Override
    public boolean execute(List<String> args) throws ConsoleException {
        if (!validateArguments(args)) {
            throw new IncorrectArgumentConsoleException("Incorrect argument exception");
        }
        try {

            String key = args.get(0);
            Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.GET, new Predicates.Equal("PrimaryKey", key)));
            if (response.code == Response.StatusCode.ERROR){
                Console.printError((String) response.body);
            }
            HumanBeing h = ((ArrayList<HumanBeing>) response.body).get(0);
            if (h == null){
                System.out.println("");
                return false;
            }
            HumanBeingBuilder builder = new HumanBeingBuilder();
            builder.createTemp(h.getPrimaryKey(), h.getId(), h.getCreationDate());

            Console.print("Enter element values:");
            for (String field : HumanBeing.getFields()) {

                Map<String, String> fieldArgs = new HashMap<>();
                if (field.equals("car")) {
                    for (String carField : Car.getFields()) {
                        System.out.println("HumanBeing.car." + carField + ":");
                        fieldArgs.put(carField, Console.input());
                    }
                } else if (field.equals("coordinates")) {
                    for (String corField : Coordinates.getFields()) {
                        System.out.println("HumanBeing.coordinates." + corField + ":");
                        fieldArgs.put(corField, Console.input());
                    }

                } else {
                    System.out.println("HumanBeing." + field);
                    fieldArgs.put("value", Console.input());
                }
                try {
                    builder.build(field, fieldArgs);
                } catch (ModelFieldException e) {
                    Console.printError(e.getMessage());
                    return false;
                }
            }
            HumanBeing updated = builder.get();
            if (updated.compareTo(h) > 0)
            {
                response = ServerConnection.getConnection().sendRequest(new Request(user, Method.UPDATE, List.of(updated)));
                Console.print("Updated: " + h.toString());
            }
            else Console.print("Nothing updated");
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
        return "replaces the value by key if the new value is greater than the old one" +
                "format: ";
    }

    @Override
    public String getName() {
        return "replace_if_greater";
    }

    @Override
    public boolean validateArguments(List<String> args){
        return args.size() == 1;
    }
}
