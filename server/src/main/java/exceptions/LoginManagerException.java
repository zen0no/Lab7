package exceptions;

public class LoginManagerException extends RuntimeException{
    public LoginManagerException(String msg){
        super(msg);
    }
}
