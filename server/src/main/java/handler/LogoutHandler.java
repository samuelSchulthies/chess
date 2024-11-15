package handler;

import dataaccess.DataAccessException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public Object logout(Request req, Response res) throws DataAccessException {
        var authToken = req.headers("authorization");
        userService.logout(authToken);
        return "";
    }
}