package client;

import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.DataAccessException;
import repl.GameRepl;
import server.ServerFacade;
import ui.ChessBoardUI;

import java.util.Arrays;
import java.util.Objects;

public class GameClient {
    private final ServerFacade server;
    private final ServerMessageHandler serverMessageHandler;
    private final GameInfo gameInfo;
    private WebSocketFacade ws;
    private final PostLoginClient postLoginClient;

    public GameClient(ServerFacade server, ServerMessageHandler serverMessageHandler,
                      WebSocketFacade ws, GameInfo gameInfo, PostLoginClient postLoginClient) {
        this.server = server;
        this.serverMessageHandler = serverMessageHandler;
        this.ws = ws;
        this.gameInfo = gameInfo;
        this.postLoginClient = postLoginClient;
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

    public String redraw() throws DataAccessException {
        postLoginClient.updateBoard(gameInfo.getGameID());
        if (Objects.equals(gameInfo.getTeam(), "WHITE")){
            ChessBoardUI.buildUIWhite(gameInfo.getBoard());
        }
        if (Objects.equals(gameInfo.getTeam(), "BLACK")){
//            ChessBoardUI.buildUIBlack();
        }
        else {
            throw new DataAccessException("Player is not on either team");
        }
        return "redraw";
    }

    public String leave() throws DataAccessException{
        ws.closeGameConnection(gameInfo.getAuthToken(), gameInfo.getGameID());
        ws = null;
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
