package exceptions;

public class DataTimeCollisionException extends RuntimeException{
    public DataTimeCollisionException (String message){
        super(message);
    }
}
