import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server chessServer = new Server();
//        chessServer.run(Integer.parseInt(args[0]));
        chessServer.run(8080);
    }
}