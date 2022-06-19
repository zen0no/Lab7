package commands;

import exceptions.ConsoleException;
import login.User;

import java.util.List;
import java.util.Scanner;

public interface Command {

    /**
     * Returns "true" if command completed successfully, returns "false" if command failed
     * @param args params for command
     * @return true if executed successfully, false otherwise
     * @throws ConsoleException
     */
    boolean execute(List<String> args);

    /**
     * Method to get description of command
     * @return
     */
    String getDescription();

    /**
     * Method to get name of command
     * @return command's name
     */
    String getName();


    /**
     * @param scanner input reader
     */
    void setScanner(Scanner scanner);


    /**
     * @param args
     * @return true if args are valid false otherwise
     * @throws ConsoleException
     */
    boolean validateArguments(List<String> args) throws ConsoleException;

    void setUser(User user);

}