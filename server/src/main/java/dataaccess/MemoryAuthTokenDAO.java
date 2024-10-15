package dataaccess;

import model.AuthData;

public class MemoryAuthTokenDAO implements AuthTokenDAO {

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }
}