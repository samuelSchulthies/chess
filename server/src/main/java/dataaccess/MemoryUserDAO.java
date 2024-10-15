package dataaccess;

import model.UserData;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

import java.util.Collection;

public class MemoryUserDAO implements UserDAO {

    Collection<UserData> userDataCollection;

    public MemoryUserDAO(){

    }

    @Override
    public void createUser(UserData u) {
        userDataCollection.add(u);
    }

    @Override
    public UserData getUser(String username) {
        throw new RuntimeException("getUser not implemented");
    }

    public int getUserDataCollectionSize(){
        return userDataCollection.size();
    }

    @Override
    public void clear() {
        userDataCollection.clear();
    }
}