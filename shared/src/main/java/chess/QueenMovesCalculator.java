package chess;

import java.util.ArrayList;

public class QueenMovesCalculator {

    ChessBoard board;
    ChessPosition myPosition;
    ArrayList<ChessMove> pieceMovesArray;

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> pieceMovesArray) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceMovesArray = pieceMovesArray;
    }

    public void queenMovesCalculator() {
        BishopMovesCalculator bishopMoves = new BishopMovesCalculator(board, myPosition, pieceMovesArray);
        bishopMoves.bishopMovesCalculator();
        RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(board, myPosition, pieceMovesArray);
        rookMovesCalculator.rookMovesCalculator();
    }

}