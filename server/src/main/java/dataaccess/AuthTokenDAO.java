package dataaccess;

import model.AuthData;

public interface AuthTokenDAO {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
}