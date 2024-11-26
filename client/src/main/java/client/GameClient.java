package client;

import client.websocket.ServerMessageHandler;
import exception.DataAccessException;
import server.ServerFacade;

import java.util.Arrays;

public class GameClient {
    private final ServerFacade server;
    private final ServerMessageHandler serverMessageHandler;

    public GameClient(ServerFacade server, ServerMessageHandler serverMessageHandler) {
        this.server = server;
        this.serverMessageHandler = serverMessageHandler;
    }

    public String eval(String input){
        try {
            var tokensFromUser = input.toLowerCase().split(" ");
            var cmd = (tokensFromUser.length > 0) ? tokensFromUser[0] : "help";
            var params = Arrays.copyOfRange(tokensFromUser, 1, tokensFromUser.length);

            return switch (cmd){
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (DataAccessException e){
            return e.getMessage();
        }
    }

    public String redraw() throws DataAccessException{
        return "redraw";
    }

    public String leave() throws DataAccessException{
        return "leave";
    }

    public String move(String... params) throws DataAccessException{
        return "move";
    }

    public String resign() throws DataAccessException{
        return "resign";
    }

    public String highlight(String... params) throws DataAccessException{
        return "highlight";
    }

    public String help(){
        return """
                
                redraw                         - Redraws the chess board
                leave                          - Removes player from game
                move <PIECE_POSITION & MOVE>   - Input desired chess move (ex: e2e4)
                resign                         - forfeits game
                highlight <PIECE_POSITION>     - shows all valid moves for the piece at that position
                help
                """;
    }

}
