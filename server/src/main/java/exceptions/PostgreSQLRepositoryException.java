package exceptions;


public class PostgreSQLRepositoryException extends RuntimeException{
    public PostgreSQLRepositoryException(String msg){
        super(msg);
    }
}
