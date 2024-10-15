package dataaccess;

import model.UserData;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

public interface UserDAO {
    void createUser(UserData u) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    int getUserDataCollectionSize();

    void clear();
}