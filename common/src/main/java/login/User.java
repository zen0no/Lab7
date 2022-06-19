package login;

import utils.HashGenerator;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class User {
    private String login;
    private String password;

    public User(String login, String password){
        this.login = login;
        this.password = HashGenerator.hash(password);
    }

    public User(String login, String password, boolean hashed){
        this.login = login;
        if (hashed){
            this.password = HashGenerator.hash(password);
        }
        else this.password = password;
    }


    public String getPassword(){
        return this.password;
    }

    public String getLogin(){
        return this.login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = HashGenerator.hash(password);
    }
    public void setPassword(String password, boolean hashed){
        if (hashed){
            this.password = HashGenerator.hash(password);
        }
        else{
            this.password = password;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.login) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
