package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requestresult.ListResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListHandler {
    GameService gameService;

    public ListHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object list(Request req, Response res) throws DataAccessException {
        var authToken = req.headers("authorization");
        ListResult listResult = gameService.list(authToken);
        return new Gson().toJson(listResult);
    }
}