package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handler.ClearHandler;
import handler.CreateHandler;
import handler.RegisterHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    static final UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
    static final GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());
    static final ClearService clearService = new ClearService(userService, gameService);

    static final ClearHandler clearHandler = new ClearHandler(clearService);
    static final RegisterHandler registerHandler = new RegisterHandler(userService);
    static final CreateHandler createHandler = new CreateHandler(gameService, userService);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::clear);
        Spark.post("/user", registerHandler::register);
        Spark.post("/game", createHandler::create);

//        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object test(Request req, Response rep){
        return null;
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res){

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
