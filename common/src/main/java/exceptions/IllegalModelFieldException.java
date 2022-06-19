package exceptions;

/**
 * Exception, thrown when data class field have illegal value
 */
public class IllegalModelFieldException extends ModelFieldException{
    public IllegalModelFieldException(String description){
        super(description);
    }
}
