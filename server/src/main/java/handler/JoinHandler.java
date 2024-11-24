package handler;

import com.google.gson.Gson;
import exception.DataAccessException;
import requestresult.JoinRequest;
import requestresult.JoinResult;
import server.websocket.WebSocketHandler;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinHandler {

    GameService gameService;
    WebSocketHandler webSocketHandler;

    public JoinHandler(GameService gameService, WebSocketHandler webSocketHandler) {
        this.gameService = gameService;
        this.webSocketHandler = webSocketHandler;
    }

    public Object join(Request req, Response res) throws DataAccessException {
        var join = new Gson().fromJson(req.body(), JoinRequest.class);
        var authToken = req.headers("authorization");
        JoinResult joinResult = gameService.join(join, authToken);
//        webSocketHandler.
        return new Gson().toJson(joinResult);
    }
}