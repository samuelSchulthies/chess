package client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.DataAccessException;
import server.ServerFacade;
import ui.ChessBoardUI;

import java.util.*;

public class GameClient {
    private final ServerFacade server;
    private final ServerMessageHandler serverMessageHandler;
    public final GameInfo gameInfo;
    private WebSocketFacade ws;
    private final PostLoginClient postLoginClient;
    private boolean resignRestrictionFlag = false;
    ObserverGameShared observerGameShared;

    final static Map<String, ChessPiece.PieceType> STRING_TO_PIECE_TYPE = Map.of(
            "bishop", ChessPiece.PieceType.BISHOP,
            "knight", ChessPiece.PieceType.KNIGHT,
            "queen", ChessPiece.PieceType.QUEEN,
            "rook", ChessPiece.PieceType.ROOK
    );

    public GameClient(ServerFacade server, ServerMessageHandler serverMessageHandler,
                      WebSocketFacade ws, GameInfo gameInfo, PostLoginClient postLoginClient) {
        this.server = server;
        this.serverMessageHandler = serverMessageHandler;
        this.ws = ws;
        this.gameInfo = gameInfo;
        this.postLoginClient = postLoginClient;
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
                case "move" -> move(params);
                case "resign" -> resign(params);
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (DataAccessException e){
            return e.getMessage();
        }
    }

    public String redraw() throws DataAccessException {
        observerGameShared.redraw();
        return "";
    }

    public String leave() throws DataAccessException{
        observerGameShared.leave();
        return "leave";
    }

    public String move(String... params) throws DataAccessException, RuntimeException{
        if((params.length != 3) && (params.length != 2)){
            throw new DataAccessException("Incorrect move arguments");
        }

        ChessPiece.PieceType promotionPiece;
        if (params.length == 3){
            promotionPiece = STRING_TO_PIECE_TYPE.get(params[2]);
        }
        else {
            promotionPiece = null;
        }

        var pieceLocation = params[0];
        ChessPosition startPosition = observerGameShared.parsePosition(pieceLocation);

        var moveLocation = params[1];
        ChessPosition endPosition = observerGameShared.parsePosition(moveLocation);

        ChessMove newMove = new ChessMove(startPosition, endPosition, promotionPiece);

        ws.makeMove(gameInfo.getAuthToken(), gameInfo.getGameID(), newMove);

        return "move";
    }

    public String resign(String... params) throws DataAccessException{
        if(resignRestrictionFlag){
            return "Game over, double resign not allowed";
        }

        if(params.length != 0){
            throw new DataAccessException("Too many arguments");
        }
        String prompt = "[RESIGN?] >>> ";
        System.out.println("\nConfirm resignation (y/n)? Game will be forfeit, opposing team wins.\n");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals(("y")) && (!result.equals("n"))) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            result = line.toLowerCase();
        }

        if (result.equals("y")) {
            ws.resign(gameInfo.getAuthToken(), gameInfo.getGameID());
            return "resign";
        }
        return "Cancelled resign";
    }

    public String highlight(String... params) throws DataAccessException{
        observerGameShared.highlight(params);
        return "highlight";
    }

    public String help(){
        return """
                
                redraw                        - Redraws the chess board
                leave                         - Removes player from game
                move <START END PROMO_PIECE>  - Input desired chess move (ex: b7 b8 queen)
                resign                        - forfeits game
                highlight <PIECE_POSITION>    - shows all valid moves for the piece at that position
                help
                """;
    }

    public void setResignRestrictionFlag(boolean setBool){
        resignRestrictionFlag = setBool;
    }

}
