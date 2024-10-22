package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private final int errorCode;
    public DataAccessException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorMessage(){
        return String.format("{ \"message\": \"Error: %s\" }", getMessage());
    }
    public int getErrorCode(){
        return errorCode;
    }
}
