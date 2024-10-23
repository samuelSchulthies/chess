package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requestresult.JoinRequest;
import requestresult.JoinResult;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {

    UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public Object login(Request req, Response res) throws DataAccessException {
        var login = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(login);
        return new Gson().toJson(loginResult);
    }
}