package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/test", this::test);
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
