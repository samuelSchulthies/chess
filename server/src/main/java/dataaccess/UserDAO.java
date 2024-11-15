package dataaccess;

import model.UserData;
import Exception.DataAccessException;

public interface UserDAO {
    void createUser(UserData u) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;

    int getUserDataCollectionSize();
}