package dataaccess;

import model.UserData;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

public interface UserDAO {
    RegisterResult createUser(RegisterRequest r) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear();
}