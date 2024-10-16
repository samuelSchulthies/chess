package dataaccess;

import model.AuthData;

public interface AuthTokenDAO {
    String createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    int getAuthDataCollectionSize();

    public void clear();
}