package service;

import dataaccess.AuthTokenDAO;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import requestresult.ClearResult;

public class ClearService {
    private final UserService userService;
    private final GameService gameService;
    public ClearService(UserService userService, GameService gameService){
        this.userService = userService;
        this.gameService = gameService;
    }

    public ClearResult clear(){
        userService.getUserDAO().clear();
        userService.getAuthTokenDAO().clear();
        gameService.getGameDAO().clear();
        return new ClearResult();
    }
}