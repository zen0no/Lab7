package exceptions;

/**
 * Exception, raised when builder is busy
 */
public class BuilderIsBusyException  extends BuilderException{
    public BuilderIsBusyException(){
        super("Builder is busy");
    }
}
