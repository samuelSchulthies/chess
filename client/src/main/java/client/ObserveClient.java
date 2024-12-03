package client;

import client.websocket.WebSocketFacade;
import exception.DataAccessException;

import java.util.Arrays;

public class ObserveClient {

    private final PostLoginClient postLoginClient;
    private final GameInfo gameInfo;
    private WebSocketFacade ws;
    private ObserverGameShared observerGameShared;
    public ObserveClient(PostLoginClient postLoginClient, GameInfo gameInfo, WebSocketFacade ws){
        this.postLoginClient = postLoginClient;
        this.gameInfo = gameInfo;
        this.ws = ws;
        observerGameShared = new ObserverGameShared(postLoginClient, gameInfo, ws);
    }
    public String eval(String input){
        try {
            var tokensFromUser = input.toLowerCase().split(" ");
            var cmd = (tokensFromUser.length > 0) ? tokensFromUser[0] : "help";
            var params = Arrays.copyOfRange(tokensFromUser, 1, tokensFromUser.length);

            return switch (cmd){
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (DataAccessException e){
            return e.getMessage();
        }
    }

    public String redraw() throws DataAccessException{
        observerGameShared.redraw();
        return "";
    }

    public String leave() throws DataAccessException{
        observerGameShared.leave();
        return "leave";
    }

    public String highlight(String... params) throws DataAccessException{
        observerGameShared.highlight(params);
        return "";
    }

    public String help(){
        return """
                
                redraw                         - redraws the chess board
                leave                          - stop observing
                highlight <PIECE_POSITION>     - shows all valid moves for the piece at that position
                help
                """;
    }

}
