package passoff.chess.piece;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static passoff.chess.TestUtilities.validateMoves;

public class BishopMoveTests {

//    @Test
//    public void bishopEquals() {
//        var piece1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
//        var piece2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
//        var piece3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
//        Assertions.assertEquals(piece2, piece1);
//        Assertions.assertEquals(piece2.hashCode(), piece1.hashCode());
//        Assertions.assertNotEquals(piece2, piece3);
//        Assertions.assertNotEquals(piece2.hashCode(), piece3.hashCode());
//
//        var p1 = new ChessPosition(1, 3);
//        var p2 = new ChessPosition(1, 3);
//        var p3 = new ChessPosition(2, 3);
//        Assertions.assertEquals(p2, p1);
//        Assertions.assertEquals(p2.hashCode(), p1.hashCode());
//        Assertions.assertNotEquals(p2, p3);
//        Assertions.assertNotEquals(p2.hashCode(), p3.hashCode());
//
//        var m1 = new ChessMove(p1, p2, null);
//        var m2 = new ChessMove(p1, p2, null);
//        var m3 = new ChessMove(p1, p2, ChessPiece.PieceType.ROOK);
//        Assertions.assertEquals(m2, m1);
//        Assertions.assertEquals(m2.hashCode(), m1.hashCode());
//        Assertions.assertNotEquals(m2, m3);
//        Assertions.assertNotEquals(m2.hashCode(), m3.hashCode());
//    }

    @Test
    public void bishopMoveUntilEdge() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |B| | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new ChessPosition(5, 4),
                new int[][]{
                        {6, 5}, {7, 6}, {8, 7},
                        {4, 5}, {3, 6}, {2, 7}, {1, 8},
                        {4, 3}, {3, 2}, {2, 1},
                        {6, 3}, {7, 2}, {8, 1},
                }
        );
    }


    @Test
    public void bishopCaptureEnemy() {
        validateMoves("""
                        | | | | | | | | |
                        | | | |Q| | | | |
                        | | | | | | | | |
                        | |b| | | | | | |
                        |r| | | | | | | |
                        | | | | | | | | |
                        | | | | |P| | | |
                        | | | | | | | | |
                        """,
                new ChessPosition(5, 2),
                new int[][]{
                        {6, 3}, {7, 4},
                        {4, 3}, {3, 4}, {2, 5},
                        // none
                        {6, 1},
                }
        );
    }


    @Test
    public void bishopBlocked() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | |R| |P| |
                        | | | | | |B| | |
                        """,
                new ChessPosition(1, 6),
                new int[][]{}
        );
    }

}