package chess;

import java.util.ArrayList;

public class PieceMovesRecurser {

    int signRow;
    int signCol;
    int addHelper;
    ChessBoard board;
    ChessPosition myPosition;
    ArrayList<ChessMove> pieceMovesArray;

    public PieceMovesRecurser(int signRow, int signCol, ChessBoard board, ChessPosition myPosition,
                              ArrayList<ChessMove> pieceMovesArray, int addHelper) {
        this.signRow = signRow;
        this.signCol = signCol;
        this.board = board;
        this.myPosition = myPosition;
        this.pieceMovesArray = pieceMovesArray;
        this.addHelper = addHelper;
    }

    public void pieceMovesRecurser(){
        addHelper++;
        int rowToAdd = myPosition.getRow() + (addHelper * signRow);
        int colToAdd = myPosition.getColumn() + (addHelper * signCol);
        ChessPosition positionChecker = new ChessPosition(rowToAdd, colToAdd);
        ChessMove newMove = new ChessMove(myPosition, positionChecker, null);

        if (((rowToAdd < 9) && (rowToAdd > 0)) && ((colToAdd < 9) && (colToAdd > 0))){
            if ((board.getPiece(positionChecker) != null) &&
                    (board.getPiece(positionChecker).pieceColor != board.getPiece(myPosition).pieceColor)){
                pieceMovesArray.add(newMove);
            }
            if (board.getPiece(positionChecker) == null){
                pieceMovesArray.add(newMove);
                pieceMovesRecurser();
            }
        }

    }
}
