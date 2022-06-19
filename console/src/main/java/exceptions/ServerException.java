package exceptions;

public class ServerException extends RuntimeException{
    public ServerException(String msg){
        super(msg);
    }
}
