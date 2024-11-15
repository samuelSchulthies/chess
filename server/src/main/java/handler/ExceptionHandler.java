package handler;

import com.google.gson.Gson;
import Exception.DataAccessException;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ExceptionHandler {
    public void exception(DataAccessException ex, Request req, Response res){
        var body = new Gson().toJson(Map.of(
                "message", String.format("Error: %s", ex.getMessage()),
                "success", false));
        res.type("application/json");
        res.status(ex.statusCode());
        res.body(body);
    }
}
