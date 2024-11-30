package client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.MovesFromUser;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.DataAccessException;
import server.ServerFacade;
import ui.ChessBoardUI;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class GameClient {
    private final ServerFacade server;
    private final ServerMessageHandler serverMessageHandler;
    public final GameInfo gameInfo;
    private WebSocketFacade ws;
    private final PostLoginClient postLoginClient;
    private boolean resignRestrictionFlag = false;

    final static Map<Character, Integer> LETTER_TO_NUM = Map.of(
            'a', 1,
            'b', 2,
            'c', 3,
            'd', 4,
            'e', 5,
            'f', 6,
            'g', 7,
            'h', 8
    );

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
        postLoginClient.updateBoard(gameInfo.getGameID());
        if (Objects.equals(gameInfo.getTeam(), "WHITE")){
            ChessBoardUI.buildUIWhite(gameInfo.getBoard());
        }
        else if (Objects.equals(gameInfo.getTeam(), "BLACK")){
            ChessBoardUI.buildUIBlack(gameInfo.getBoard());
        }
        else {
            throw new DataAccessException("Player is not on either team");
        }
        return "";
    }

    public String leave() throws DataAccessException{
        ws.closeGameConnection(gameInfo.getAuthToken(), gameInfo.getGameID());
        ws = null;
        return "leave";
    }

    public String move(String... params) throws DataAccessException{
        if((params.length != 3) && (params.length != 2)){
            throw new DataAccessException("Incorrect move arguments");
        }

//        if(resignRestrictionFlag){
//            return "Game over, moves not allowed";
//        }

        ChessPiece.PieceType promotionPiece;
        if (params.length == 3){
            promotionPiece = STRING_TO_PIECE_TYPE.get(params[2]);
        }
        else {
            promotionPiece = null;
        }

        var pieceLocation = params[0];
        int pieceRow = Character.getNumericValue(pieceLocation.charAt(1));
        int pieceCol = LETTER_TO_NUM.get(pieceLocation.charAt(0));

        var moveLocation = params[1];
        int moveRow = Character.getNumericValue(moveLocation.charAt(1));
        int moveCol = LETTER_TO_NUM.get(moveLocation.charAt(0));

        ChessPosition startPosition = new ChessPosition(pieceRow, pieceCol);
        ChessPosition endPosition = new ChessPosition(moveRow, moveCol);

        ChessMove newMove = new ChessMove(startPosition, endPosition, promotionPiece);
        MovesFromUser originalMoves = new MovesFromUser(pieceLocation, moveLocation);

        ws.makeMove(gameInfo.getAuthToken(), gameInfo.getGameID(), newMove, originalMoves);

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

    public void updateVacantTeam(){
        if((postLoginClient.getGame(gameInfo.getGameID()).whiteUsername() != null)
                && (postLoginClient.getGame(gameInfo.getGameID()).blackUsername() != null)) {
            gameInfo.setWaitingForPlayer(false);
        }
    }

    public void setResignRestrictionFlag(boolean setBool){
        resignRestrictionFlag = setBool;
    }

}
