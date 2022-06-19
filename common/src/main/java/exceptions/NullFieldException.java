package exceptions;

/**
 * Exception, thrown when data class fields is null
 */
public class NullFieldException extends ModelFieldException{

    private final String field;

    public NullFieldException(String description, String field){
        super(description);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
