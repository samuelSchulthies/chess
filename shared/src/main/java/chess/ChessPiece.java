package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> bishopArray = new ArrayList<>();
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(6,5), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(7,6), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(8,7), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(4,5), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(3,6), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(2,7), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(1,8), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(4,3), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(3,2), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(2,1), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(6,3), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(7,2), null));
        bishopArray.add(new ChessMove(myPosition, new ChessPosition(8,1), null));
        return bishopArray;
    }
}
