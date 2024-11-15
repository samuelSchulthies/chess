package handler;

import Exception.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public Object clear(Request req, Response res) throws DataAccessException {
        clearService.clear();
        res.status(200);
        return "";
    }
}