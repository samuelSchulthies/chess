package chess;

import java.util.ArrayList;

public class KnightMovesCalculator {

    ChessBoard board;
    ChessPosition myPosition;
    ArrayList<ChessMove> pieceMovesArray;

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> pieceMovesArray) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceMovesArray = pieceMovesArray;
    }

    public void knightMovesCalculator(){
        int signRow = 1;
        int signCol = 1;
        int addHelperRow = 0;
        int addHelperCol = 0;
        for (int i = 0; i < 4; ++i) {

            // i = 0 is ++ quadrant
            // i = 1 is -+ quadrant
            // i = 2 is -- quadrant
            // i = 3 is +- quadrant

            if (i == 1) {
                signRow = -1;
            }
            if (i == 2) {
                signRow = -1;
                signCol = -1;
            }
            if (i == 3) {
                signRow = 1;
                signCol = -1;
            }

            for (int j = 0; j < 2; ++j) {
                if (j == 0) {
                    addHelperRow = 2;
                    addHelperCol = 1;
                }
                if (j == 1) {
                    addHelperRow = 1;
                    addHelperCol = 2;
                }
                int rowToAdd = myPosition.getRow() + (signRow * addHelperRow);
                int colToAdd = myPosition.getColumn() + (signCol * addHelperCol);
                ChessPosition positionChecker = new ChessPosition(rowToAdd, colToAdd);
                ChessMove newMove = new ChessMove(myPosition, positionChecker, null);

                if ((rowToAdd > 0) && (rowToAdd < 9)
                        && (colToAdd > 0) && (colToAdd < 9)) {
                    if ((board.getPiece(positionChecker) != null)
                            && (board.getPiece(positionChecker).pieceColor != board.getPiece(myPosition).pieceColor)) {
                        pieceMovesArray.add(newMove);
                    }
                    if (board.getPiece(positionChecker) == null) {
                        pieceMovesArray.add(newMove);
                    }
                }
            }
        }
    }
}