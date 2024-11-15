package service;

import dataaccess.*;
import requestresult.ClearResult;

public class ClearService {
    private final UserService userService;
    private final GameService gameService;
    public ClearService(UserService userService, GameService gameService){
        this.userService = userService;
        this.gameService = gameService;
    }

    public ClearResult clear() throws DataAccessException {
        userService.getUserDAO().clear();
        userService.getAuthTokenDAO().clear();
        gameService.getGameDAO().clear(gameService);
        return new ClearResult();
    }
}