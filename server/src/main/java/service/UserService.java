package service;
import dataaccess.AuthTokenDAO;
import exception.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requestresult.*;

import java.util.Objects;

public class UserService {

    private final UserDAO userDAO;
    private final AuthTokenDAO authTokenDAO;
    public UserService(UserDAO userDAO, AuthTokenDAO authtokenDAO){
        this.userDAO = userDAO;
        this.authTokenDAO = authtokenDAO;
    }

    public RegisterResult register(RegisterRequest r) throws DataAccessException {

        if (((r.username() != null) && ((!Objects.equals(r.username(), "")))) &&
                ((r.password() != null) && ((!Objects.equals(r.password(), "")))) &&
                        (((r.email() != null) && ((!Objects.equals(r.email(), "")))))) {
            if (userDAO.getUser(r.username()) == null) {
                String hashedPassword = BCrypt.hashpw(r.password(), BCrypt.gensalt());
                UserData user = new UserData(r.username(), hashedPassword, r.email());
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

    public LoginResult login(LoginRequest r) throws DataAccessException{
        if(userDAO.getUser((r.username())) == null){
            throw new DataAccessException("username does not exist");
        }
        UserData userAuthentication = userDAO.getUser(r.username());
        if(BCrypt.checkpw(r.password(), userAuthentication.password())){
            String authToken = authTokenDAO.createAuth(userAuthentication.username());
            return new LoginResult(r.username(), authToken);
        }
        else {
            throw new DataAccessException("passwords do not match");
        }
    }

    public LogoutResult logout(String authToken) throws DataAccessException{
        if (authTokenDAO.getAuth(authToken) != null) {
            authTokenDAO.deleteAuth(authToken);
            return new LogoutResult();
        }
        else {
            throw new DataAccessException("invalid authtoken");
        }
    }

    int userDataSize(){
        return userDAO.getUserDataCollectionSize();
    }

    int authTokenSize(){
        return authTokenDAO.getAuthDataCollectionSize();
    }

    public UserDAO getUserDAO(){
        return userDAO;
    }

    public AuthTokenDAO getAuthTokenDAO(){
        return authTokenDAO;
    }

    public String getUsername(String authToken) throws DataAccessException {
        return authTokenDAO.getAuth(authToken).username();
    }

}