package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateHandler {
    GameService gameService;

    public CreateHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object create(Request req, Response res) throws DataAccessException {
        var game = new Gson().fromJson(req.body(), CreateRequest.class);
        var authToken = req.headers("authorization");
        CreateResult createResult = gameService.create(game, authToken);
        return new Gson().toJson(createResult);
    }
}