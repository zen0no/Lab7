package exceptions;

/**
 * Base exception for data class fields
 */
public class ModelFieldException extends RuntimeException{
    protected ModelFieldException(String description){
        super(description);
    }
}
