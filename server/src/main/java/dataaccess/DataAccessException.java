package dataaccess;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private int statusCode;
    public DataAccessException(String message) {
        super(message);
        if ((Objects.equals(message, "invalid gameID")) || (Objects.equals(message, "team color cannot be empty")) ||
                (Objects.equals(message, "one of the register fields is empty"))){
            setStatusCode(400);
        }
        if ((Objects.equals(message, "invalid authtoken")) || (Objects.equals(message, "username does not exist")) ||
                (Objects.equals(message, "passwords do not match"))){
            setStatusCode(401);
        }
        if (Objects.equals(message, "game is full") || (Objects.equals(message, "black is taken")) ||
                (Objects.equals(message, "white is taken")) || (Objects.equals(message, "username already taken"))){
            setStatusCode(403);
        }
        if (Objects.equals(message, "game name cannot be empty")){
            setStatusCode(404);
        }

    }

    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;
    }

    public int statusCode(){
        return statusCode;
    }
}
