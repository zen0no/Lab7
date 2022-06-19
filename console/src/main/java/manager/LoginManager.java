package manager;

import exceptions.ServerException;
import login.User;
import net.Method;
import net.Request;
import net.Response;
import server.ServerConnection;

import java.util.Objects;

public class LoginManager {

    public LoginManager(){

    }

    public User askUser(){
        Console.print("Are you new to city races?");
        Console.print("Type \"register\" if you want to create account");
        Console.print("Type \"login\" if you already have it");
        String s = Console.input();
        try{
        switch (s){
            case "register" -> {
                return register();
            }
            case "login" -> {
                return login();
            }
            default -> {
                Console.print("Well, I see you are not smart, ya? Let's try again");
                return askUser();
            }
        }
        }
        catch(ServerException e){
            Console.printError(e);
            return askUser();
        }
    }

    private User register(){
        Console.print("Login: ");
        String login = Console.input();
        Console.print("Password: ");
        String password = Console.input();
        if (Objects.equals(password, "")){
            Console.printError("Password can't be empty");
            return askUser();
        }
        Console.print("Repeat password:");
        String repeatedPassword = Console.input();
        if (!Objects.equals(password, repeatedPassword)) {
            Console.printError("Passwords don't match");
            return askUser();
        }

        User user = new User(login, password);

        Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.REGISTER, null));

        if (response.code == Response.StatusCode.ERROR){
            Console.printError((String) response.body);
        }

        return user;
    }

    private User login(){
        Console.print("Login: ");
        String login = Console.input();
        Console.print("Password: ");
        String password = Console.input();
        if (Objects.equals(password, "")){
            Console.printError("Password can't be empty");
            return askUser();
        }

        User user = new User(login, password);

        Response response = ServerConnection.getConnection().sendRequest(new Request(user, Method.LOGIN, null));

        if (response.code == Response.StatusCode.ERROR){
            Console.printError((String) response.body);
            return askUser();
        }

        Console.print("You're logged in as " + user.getLogin());

        return user;
    }

}
