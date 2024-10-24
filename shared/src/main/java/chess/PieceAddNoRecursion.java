package chess;

import java.util.ArrayList;

public class PieceAddNoRecursion {

    int rowToAdd;
    int colToAdd;
    ChessMove newMove;
    ChessBoard board;
    ChessPosition myPosition;
    ChessPosition positionChecker;
    ArrayList<ChessMove> pieceMovesArray;

    public PieceAddNoRecursion(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> pieceMovesArray,
                               ChessMove newMove, ChessPosition positionChecker, int rowToAdd, int colToAdd) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceMovesArray = pieceMovesArray;
        this.newMove = newMove;
        this.positionChecker = positionChecker;
        this.rowToAdd = rowToAdd;
        this.colToAdd = colToAdd;
    }

    public void pieceAddNoRecursion(){
        if ((rowToAdd > 0) && (rowToAdd < 9)
                && (colToAdd > 0) && (colToAdd < 9)){
            if ((board.getPiece(positionChecker) != null)
                    && (board.getPiece(positionChecker).pieceColor != board.getPiece(myPosition).pieceColor)){
                pieceMovesArray.add(newMove);
            }
            if (board.getPiece(positionChecker) == null){
                pieceMovesArray.add(newMove);
            }
        }
    }

}
