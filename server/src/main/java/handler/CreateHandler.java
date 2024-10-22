package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.UserData;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class CreateHandler {
    GameService gameService;

    public CreateHandler(GameService gameService) {
        this.gameService = gameService;
    }


}