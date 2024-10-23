package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {

    ChessBoard board;
    ChessPosition myPosition;
    ArrayList<ChessMove> pieceMovesArray;

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> pieceMovesArray) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceMovesArray = pieceMovesArray;
    }

    public void bishopMovesCalculator(){
        int signRow = 1;
        int signCol = 1;
        for (int i = 0; i < 4; ++i){
            int addHelper = 0;

            // i = 0 is ++ quadrant
            // i = 1 is -+ quadrant
            // i = 2 is -- quadrant
            // i = 3 is +- quadrant

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

            PieceMovesRecurser pieceMovesRecurser = new PieceMovesRecurser(signRow, signCol, board, myPosition,
                    pieceMovesArray, addHelper);
            pieceMovesRecurser.pieceMovesRecurser();

        }
    }

}