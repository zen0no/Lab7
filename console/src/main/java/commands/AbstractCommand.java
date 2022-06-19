package commands;

import login.User;

import java.util.Scanner;


/**
 * Command interface realization. Base command class
 */
public abstract class AbstractCommand implements Command {
    protected Scanner scanner;
    protected User user;

    /**
     * @param scanner input scanner
     */
    public void setScanner(Scanner scanner){
        this.scanner = scanner;
    }

    public void setUser(User user){this.user = user;}


}
