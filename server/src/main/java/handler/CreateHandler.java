package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class CreateHandler {
    GameService gameService;
    UserService userService;

    public CreateHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    public Object create(Request req, Response res) throws DataAccessException {
//        CreateRequest game = new Gson().fromJson(req.body(), CreateRequest.class);
//        CreateResult createResult = gameService.create(game);
//        return new Gson().toJson(createResult);
        return null;
    }
}