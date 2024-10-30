package dataaccess;

import model.UserData;

public class MySQLUserDAO implements UserDAO {
    @Override
    public void createUser(UserData u) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public int getUserDataCollectionSize() {
        return 0;
    }

    @Override
    public void clear() {

    }
}