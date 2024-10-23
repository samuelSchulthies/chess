package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ExceptionHandler {
    public Object exception(DataAccessException ex, Request req, Response res){
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", ex.getMessage()), "success", false));
        res.type("application/json");
        res.status(ex.StatusCode());
        res.body(body);
        return body;
    }
}