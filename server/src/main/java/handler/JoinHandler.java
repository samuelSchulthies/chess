package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requestresult.JoinRequest;
import requestresult.JoinResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinHandler {

    GameService gameService;

    public JoinHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object join(Request req, Response res) throws DataAccessException {
        var join = new Gson().fromJson(req.body(), JoinRequest.class);
        var authToken = req.headers("authorization");
        JoinResult joinResult = gameService.join(join, authToken);
        return new Gson().toJson(joinResult);
    }
}