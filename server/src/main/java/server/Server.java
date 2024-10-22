package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/hello", (req, rep) -> "Hello friend!");
        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object test(Request req, Response rep){
        return null;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
