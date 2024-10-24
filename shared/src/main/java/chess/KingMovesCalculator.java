package chess;

import java.util.ArrayList;

public class KingMovesCalculator {


    ChessBoard board;
    ChessPosition myPosition;
    ArrayList<ChessMove> pieceMovesArray;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> pieceMovesArray) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceMovesArray = pieceMovesArray;
    }

    public void kingMovesCalculator(){
        int signRow = 1;
        int signCol = 1;
        for (int i = 0; i < 8; ++i){

            // i = 0 is ++ quadrant
            // i = 1 is -+ quadrant
            // i = 2 is -- quadrant
            // i = 3 is +- quadrant

            // i = 4 is +0 quadrant
            // i = 5 is 0- quadrant
            // i = 6 is -0 quadrant
            // i = 7 is 0+ quadrant

            if (i == 1){
                signRow = -1;
            }
            if (i == 2){
                signRow = -1;
                signCol = -1;
            }
            if (i == 3){
                signRow = 1;
                signCol = -1;
            }
            if (i == 4){
                signRow = 1;
                signCol = 0;
            }
            if (i == 5){
                signRow = 0;
                signCol = -1;
            }
            if (i == 6){
                signRow = -1;
                signCol = 0;
            }
            if (i == 7){
                signRow = 0;
                signCol = 1;
            }

            int rowToAdd = myPosition.getRow() + signRow;
            int colToAdd = myPosition.getColumn() + signCol;
            ChessPosition positionChecker = new ChessPosition(rowToAdd, colToAdd);
            ChessMove newMove = new ChessMove(myPosition, positionChecker, null);

            PieceAddNoRecursion pieceAdd = new PieceAddNoRecursion(board, myPosition, pieceMovesArray, newMove,
                    positionChecker, rowToAdd, colToAdd);
            pieceAdd.pieceAddNoRecursion();
        }
    }
}