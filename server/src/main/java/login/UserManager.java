package login;


import exceptions.LoginManagerException;
import jdbc.DBEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class UserManager {

    private static Logger log = LogManager.getLogger(UserManager.class);

    private static UserManager manager;

    private static Set<User> loggedUsers = Collections.synchronizedSet(new HashSet<>());

    private static  boolean isLoaded = false;

    private Map<String, User> values = new HashMap<>();

    private UserManager(){
        Connection con = DBEngine.getConnection();

        String existence = """
           SELECT * FROM public.user_
            """;
        try{
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(existence);
            while (resultSet.next()){
                User user = new User(resultSet.getString("username"),resultSet.getString("password_"), false );
                values.put(user.getLogin(), user);
            }
            this.values = Collections.synchronizedMap(values);
        } catch (SQLException throwables) {
            throw new LoginManagerException(throwables.getLocalizedMessage());
        }
    }

    public static UserManager getManager(){
        if (!isLoaded){
            throw new LoginManagerException("Manager is not loaded");
        }
        return manager;
    }

    public static void load(){
        log.info("Loading login manager...");
        if (isLoaded){
            throw new LoginManagerException("Repository is already loaded");
        }
        isLoaded = true;
        manager = new UserManager();
        log.info("LoginManager loaded");
    }


    public void register(User user){
        Connection con = DBEngine.getConnection();
        try{
            if (values.get(user.getLogin()) != null){
                throw new LoginManagerException("User already exists");
            }
            else if(user.getLogin().isEmpty() || user.getLogin() == null || user.getPassword().isEmpty() || user.getPassword() == null){
                throw new LoginManagerException("Invalid value for user");
            }
            String sql = "INSERT INTO public.user_ VALUES (?, ?)";

            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();

            values.put(user.getLogin(), user);
            log.info("Created user " + user.toString());

        } catch (SQLException throwables) {
            throw new LoginManagerException(throwables.getMessage());
        }
    }

    public boolean login(User user){
        boolean res= !loggedUsers.contains(user) && Objects.equals(user, values.get(user.getLogin()));
        if (res){
            loggedUsers.add(user);
        }
        return res;
    }

    public boolean logout(User user){
        if (!loggedUsers.contains(user)){
            return false;
        }
        loggedUsers.remove(user);
        return true;
    }

    public void changePassword(User user, String password){
        Connection con = DBEngine.getConnection();
        try{
            if (values.get(user.getLogin()) != null){
                throw new LoginManagerException("User already exists");
            }
            else if(user.getLogin().isEmpty() || user.getLogin() == null || user.getPassword().isEmpty() || user.getPassword() == null){
                throw new LoginManagerException("Invalid value for user");
            }
            String sql = "UPDATE public.user_ SET password_ = ? WHERE username = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, password);
            statement.setString(2, user.getLogin());
            statement.executeUpdate();
            con.commit();
            values.get(user.getLogin()).setPassword(password, false);
            log.info("Password changed for " + user);

        } catch (SQLException throwables) {
            throw new LoginManagerException(throwables.getMessage());
        }
    }
}
