package client;

import chess.ChessPiece;
import chess.ChessPosition;
import client.websocket.WebSocketFacade;
import exception.DataAccessException;
import ui.ChessBoardUI;

import java.util.Map;
import java.util.Objects;

public class ObserverGameShared {

    private final PostLoginClient postLoginClient;
    private final GameInfo gameInfo;
    private WebSocketFacade ws;

    final static Map<Character, Integer> ALPHA_CHAR_TO_INT = Map.of(
            'a', 1,
            'b', 2,
            'c', 3,
            'd', 4,
            'e', 5,
            'f', 6,
            'g', 7,
            'h', 8
    );

    final static Map<Character, Integer> NUM_CHAR_TO_INT = Map.of(
            '1', 1,
            '2', 2,
            '3', 3,
            '4', 4,
            '5', 5,
            '6', 6,
            '7', 7,
            '8', 8
    );

    public ObserverGameShared(PostLoginClient postLoginClient, GameInfo gameInfo, WebSocketFacade ws){
        this.postLoginClient = postLoginClient;
        this.gameInfo = gameInfo;
        this.ws = ws;
    }
    public void redraw() throws DataAccessException {
        postLoginClient.updateBoard(gameInfo.getGameID());
        if (Objects.equals(gameInfo.getTeam(), "BLACK")){
            ChessBoardUI.buildUIBlack(gameInfo.getBoard());
        }
        else {
            ChessBoardUI.buildUIWhite(gameInfo.getBoard());
        }
    }
    public void leave() throws DataAccessException{
        ws.closeGameConnection(gameInfo.getAuthToken(), gameInfo.getGameID());
        ws = null;
    }

    public void highlight(String... params) throws DataAccessException{
        if(params.length != 1){
            throw new DataAccessException("Incorrect move arguments");
        }

        var pieceLocation = params[0];
        ChessPosition positionToHighlight = parsePosition(pieceLocation);

    }

    public ChessPosition parsePosition(String position){
        int row;
        int col;

        if (position.length() != 2){
            throw new RuntimeException("Invalid move or piece location");
        }
        if (NUM_CHAR_TO_INT.get(position.charAt(1)) == null){
            throw new RuntimeException("Invalid move or piece location");
        }
        else {
            row = NUM_CHAR_TO_INT.get(position.charAt(1));
        }
        if (ALPHA_CHAR_TO_INT.get(position.charAt(0)) == null){
            throw new RuntimeException("Invalid move or piece location");
        }
        else {
            col = ALPHA_CHAR_TO_INT.get(position.charAt(0));
        }
        return new ChessPosition(row, col);
    }
}
