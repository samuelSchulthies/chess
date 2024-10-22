package handler;


import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.UserData;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) throws DataAccessException {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResult registerResult = userService.register(registerRequest);
        res.status(200);
        return new Gson().toJson(registerResult);
    }
}