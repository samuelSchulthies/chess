package service;

import dataaccess.AuthTokenDAO;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import requestresult.ClearResult;

public class ClearService {
    private final UserService userService;
    public ClearService(UserService userService){
        this.userService = userService;
    }


    ClearResult clear(){
        userService.getUserDAO().clear();
        userService.getAuthTokenDAO().clear();
        return new ClearResult();
    }
}