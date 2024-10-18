package service;
import dataaccess.AuthTokenDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import requestresult.*;

import java.util.Objects;

public class UserService {

    private final UserDAO userDAO;
    private final AuthTokenDAO authTokenDAO;
    public UserService(UserDAO userDAO, AuthTokenDAO authtokenDAO){
        this.userDAO = userDAO;
        this.authTokenDAO = authtokenDAO;
    }

    RegisterResult register(RegisterRequest r) throws DataAccessException {

        if ((r.username() != null) && (r.password() != null) && (r.email() != null)) {
            if (userDAO.getUser(r.username()) == null) {
                UserData user = new UserData(r.username(), r.password(), r.email());
                userDAO.createUser(user);
                LoginRequest newLogin = new LoginRequest(r.username(), r.password());
                String authToken = login(newLogin).authToken();
                return new RegisterResult(newLogin.username(), authToken);
            }
            else {
                throw new DataAccessException("username already taken");
            }
        }
        else {
            throw new DataAccessException("one of the register fields is empty");
        }
    }

    LoginResult login(LoginRequest r) throws DataAccessException{
        if(userDAO.getUser((r.username())) == null){
            throw new DataAccessException("username does not exist");
        }
        UserData userAuthentication = userDAO.getUser(r.username());
        if(Objects.equals(userAuthentication.password(), r.password())){
            String authToken = authTokenDAO.createAuth(userAuthentication.username());
            return new LoginResult(r.username(), authToken);
        }
        else {
            throw new DataAccessException("passwords do not match");
        }
    }

    LogoutResult logout(LogoutRequest r){
        throw new RuntimeException("logout not implemented");
    }

    int userDataSize(){
        return userDAO.getUserDataCollectionSize();
    }

    int authTokenSize(){
        return authTokenDAO.getAuthDataCollectionSize();
    }

    UserDAO getUserDAO(){
        return userDAO;
    }

    AuthTokenDAO getAuthTokenDAO(){
        return authTokenDAO;
    }

}