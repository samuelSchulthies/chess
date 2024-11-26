package client;

import exception.DataAccessException;

public class SharedClient {
    public String highlight() throws DataAccessException {
        return "highlight";
    }

    public String redraw() throws DataAccessException{
        return "redraw";
    }
}
