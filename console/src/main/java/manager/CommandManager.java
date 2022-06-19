package manager;

import commands.AbstractCommand;
import commands.Command;
import exceptions.ConsoleException;
import exceptions.IncorrectArgumentConsoleException;
import exceptions.ServerException;
import login.User;

import java.util.*;

/**
 * Class for interacting with user
 */
public class CommandManager {
    private final ArrayList<Command> executedCommands = new ArrayList<>();
    private final Map<String, Command> availableCommands = new HashMap<>();
    private final User user;

    /**
     * Class constructor
     */
    public CommandManager(User user) {
        Command help = new Help();
        Command history = new History();
        this.availableCommands.put(help.getName(), help);
        this.availableCommands.put(history.getName(), history);
        this.user = user;
    }

    /**
     * @param commands Commands which manager can execute
     */
    public void addCommands(List<Command> commands){
        for (Command c: commands){
            addCommand(c);
        };
    }

    private void addCommand(Command command){
        command.setUser(user);
        this.availableCommands.put(command.getName(), command);
    }
    /**
     * Method to read InputStream
     */

    public void read(){
        while (Console.canRead()){
            try {
                String s = Console.input();
                List<String> args = Arrays.asList(s.split(" "));
                Command command = availableCommands.get(args.get(0));
                command.execute(args.subList(1, args.size()));
                executedCommands.add(command);
            }
            catch (ConsoleException|ServerException e) {
                Console.printError(e);
            }

            catch (NullPointerException e){
                Console.print("Unknown command");
            }
        }
    }

    /**
     * Abstract command child class. Can show information about all of available commands
     */
    private class Help extends AbstractCommand{
        @Override
        public boolean execute(List<String> args) throws ConsoleException {
            if (!validateArguments(args)){
                throw new IncorrectArgumentConsoleException("Command help has no arguments");
            }
            Console.print("HumanBeing fields:");
            Console.print("""
                    name: string:
                    coordinates.x: integer (max: 673)
                    coordinates.y: integer
                    speed: integer
                    car.cool: boolean
                    realHero: boolean
                    hasToothpick: boolean
                    weaponType:   AXE,
                            PISTOL,
                            KNIFE,
                            BAT;
                    mood:   SADNESS,
                            SORROW,
                            GLOOM,
                            APATHY,
                            FRENZY;
                    """);
            Console.print("------------------------------------");
            for (Map.Entry<String, Command> c: availableCommands.entrySet()){
                Console.print("-" + c.getKey());
                Console.print(c.getValue().getDescription());
            }
            return true;
        }

        @Override
        public boolean validateArguments(List<String> args){
            return (args.size() == 0);
        }

        @Override
        public String getDescription(){
            return "shows information about all of available commands";
        }

        @Override
        public String getName(){
            return "help";
        }
    }

    /**
     * Abstract command child class. Can show latest 11 executed commands
     */
    private class History extends AbstractCommand {

        @Override
        public boolean execute(List<String> args) throws ConsoleException {
            if (!validateArguments(args)) {
                throw new IncorrectArgumentConsoleException("Command history has no arguments");
            }
            if (executedCommands.size() <= 11) {
                for (Command c : Set.copyOf(executedCommands)) {
                    Console.print(c.getName());
                }
                return true;
            }

            int size = executedCommands.size();
            ArrayList<Command> commandsSublist = (ArrayList<Command>) executedCommands.subList(size - 12, size - 1);
            for (Command c : Set.copyOf(commandsSublist)) {
                Console.print(c.getName());
                Console.print(c.getDescription());
            }
            return true;
        }

        @Override
        public String getDescription() {
            return "shows the last 11 commands";
        }

        @Override
        public String getName() {
            return "history";
        }

        @Override
        public boolean validateArguments(List<String> args) throws ConsoleException {
            return (args.size() == 0);
        }
    }

}

