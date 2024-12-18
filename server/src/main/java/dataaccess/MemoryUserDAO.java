package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    final private ArrayList<UserData> userDataCollection = new ArrayList<>();

    public MemoryUserDAO(){

    }

    @Override
    public void createUser(UserData u) {
        userDataCollection.add(u);
    }

    @Override
    public UserData getUser(String username) {
        for (UserData data : userDataCollection) {
            if (Objects.equals(data.username(), username)){
                return data;
            }
        }
        return null;
    }

    public int getUserDataCollectionSize(){
        return userDataCollection.size();
    }

    @Override
    public void clear() {
        userDataCollection.clear();
    }
}