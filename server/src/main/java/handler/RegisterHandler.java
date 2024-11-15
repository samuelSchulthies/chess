package handler;


import com.google.gson.Gson;
import Exception.DataAccessException;
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
        RegisterRequest user = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(user);
        res.status(200);
        return new Gson().toJson(registerResult);
    }
}