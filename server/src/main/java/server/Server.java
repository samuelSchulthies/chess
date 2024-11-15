package server;

import dataaccess.*;
import spark.*;
import exception.DataAccessException;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", HandlerUtility.CLEAR_HANDLER::clear);
        Spark.post("/game", HandlerUtility.CREATE_HANDLER::create);
        Spark.put("/game", HandlerUtility.JOIN_HANDLER::join);
        Spark.get("/game", HandlerUtility.LIST_HANDLER::list);
        Spark.post("/session", HandlerUtility.LOGIN_HANDLER::login);
        Spark.delete("/session", HandlerUtility.LOGOUT_HANDLER::logout);
        Spark.post("/user", HandlerUtility.REGISTER_HANDLER::register);
        Spark.exception(DataAccessException.class, HandlerUtility.EXCEPTION_HANDLER::exception);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
