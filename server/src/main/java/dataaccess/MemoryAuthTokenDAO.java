package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthTokenDAO implements AuthTokenDAO {

    final private ArrayList<AuthData> authDataCollection = new ArrayList<>();

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        authDataCollection.add(new AuthData(authToken, username));
        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData data : authDataCollection) {
            if (Objects.equals(data.authToken(), authToken)){
                return data;
            }
        }
        return null;
    }

    public void deleteAuth(String authToken){
        authDataCollection.removeIf(data -> Objects.equals(data.authToken(), authToken));
    }

    public int getAuthDataCollectionSize(){
        return authDataCollection.size();
    }

    public void clear(){
        authDataCollection.clear();
    }
}