package dataaccess;

import model.AuthData;

public interface AuthTokenDAO {
    String createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken);
    public void clear() throws DataAccessException;

    int getAuthDataCollectionSize();
}