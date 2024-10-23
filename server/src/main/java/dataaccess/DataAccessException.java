package dataaccess;

import java.lang.reflect.Type;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    final private int statusCode;
    public DataAccessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int StatusCode(){
        return statusCode;
    }
}
