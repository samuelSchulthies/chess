import chess.*;
import exception.DataAccessException;
import repl.PreLoginRepl;

public class Main {
    public static void main(String[] args) throws DataAccessException {

        var serverUrl = "http://localhost:8080";
        if (args.length == 1){
            serverUrl = args[0];
        }
        new PreLoginRepl(serverUrl).run();
    }
}