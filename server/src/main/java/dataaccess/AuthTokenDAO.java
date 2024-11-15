package dataaccess;

import exception.DataAccessException;

import model.AuthData;

public interface AuthTokenDAO {
    String createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;

    int getAuthDataCollectionSize();
}