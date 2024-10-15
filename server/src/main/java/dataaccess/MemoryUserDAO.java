package dataaccess;

import model.UserData;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

public class MemoryUserDAO implements UserDAO {

    @Override
    public RegisterResult createUser(RegisterRequest r) {
        throw new RuntimeException("createUser not implemented");
    }

    @Override
    public UserData getUser(String username) {
        throw new RuntimeException("getUser not implemented");
    }

    @Override
    public void clear() {
        throw new RuntimeException("clear not implemented");
    }
}