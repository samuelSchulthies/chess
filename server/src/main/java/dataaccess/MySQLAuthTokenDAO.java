package dataaccess;

import model.AuthData;

public class MySQLAuthTokenDAO implements AuthTokenDAO{
    @Override
    public String createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public int getAuthDataCollectionSize() {
        return 0;
    }

    @Override
    public void clear() {

    }
}