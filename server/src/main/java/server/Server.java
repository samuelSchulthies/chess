package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handler.*;
import handler.ExceptionHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    static final UserService USER_SERVICE = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
    static final GameService GAME_SERVICE = new GameService(new MemoryGameDAO(), USER_SERVICE.getAuthTokenDAO(), USER_SERVICE.getUserDAO());
    static final ClearService CLEAR_SERVICE = new ClearService(USER_SERVICE, GAME_SERVICE);

    static final ExceptionHandler EXCEPTION_HANDLER = new ExceptionHandler();
    static final ClearHandler CLEAR_HANDLER = new ClearHandler(CLEAR_SERVICE);
    static final CreateHandler CREATE_HANDLER = new CreateHandler(GAME_SERVICE);
    static final JoinHandler JOIN_HANDLER = new JoinHandler(GAME_SERVICE);
    static final ListHandler LIST_HANDLER = new ListHandler(GAME_SERVICE);
    static final LoginHandler LOGIN_HANDLER = new LoginHandler(USER_SERVICE);
    static final LogoutHandler LOGOUT_HANDLER = new LogoutHandler(USER_SERVICE);
    static final RegisterHandler REGISTER_HANDLER = new RegisterHandler(USER_SERVICE);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", CLEAR_HANDLER::clear);
        Spark.post("/game", CREATE_HANDLER::create);
        Spark.put("/game", JOIN_HANDLER::join);
        Spark.get("/game", LIST_HANDLER::list);
        Spark.post("/session", LOGIN_HANDLER::login);
        Spark.delete("/session", LOGOUT_HANDLER::logout);
        Spark.post("/user", REGISTER_HANDLER::register);
        Spark.exception(DataAccessException.class, EXCEPTION_HANDLER::exception);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
