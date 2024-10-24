package chess;

import java.util.ArrayList;

public class RookMovesCalculator {

    ChessBoard board;
    ChessPosition myPosition;
    ArrayList<ChessMove> pieceMovesArray;

    public RookMovesCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> pieceMovesArray) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceMovesArray = pieceMovesArray;
    }

    public void rookMovesCalculator(){
        int signRow = 1;
        int signCol = 0;

        for (int i = 0; i < 4; ++i) {
            int addHelper = 0;

            // i = 4 is +0 quadrant
            // i = 5 is 0- quadrant
            // i = 6 is -0 quadrant
            // i = 7 is 0+ quadrant

            if (i == 1) {
                signRow = 0;
                signCol = -1;
            }
            if (i == 2) {
                signRow = -1;
                signCol = 0;
            }
            if (i == 3) {
                signRow = 0;
                signCol = 1;
            }

            PieceMovesRecurser pieceMovesRecurser = new PieceMovesRecurser(signRow, signCol, board, myPosition,
                    pieceMovesArray, addHelper);
            pieceMovesRecurser.pieceMovesRecurser();
        }
    }

}