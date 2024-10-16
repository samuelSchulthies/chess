package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthTokenDAO implements AuthTokenDAO {

    private ArrayList<AuthData> authDataCollection = new ArrayList<>();

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken  = UUID.randomUUID().toString();
        authDataCollection.add(new AuthData(authToken, username));
        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public int getAuthDataCollectionSize(){
        return authDataCollection.size();
    }

    public void clear(){
        authDataCollection.clear();
    }
}