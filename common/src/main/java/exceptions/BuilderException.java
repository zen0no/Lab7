package exceptions;

/**
 * Base exception for HumanBeingBuilder
 */
public class BuilderException extends RuntimeException{
    public BuilderException(String description){
        super(description);
    }
}
