package chess;

import java.util.ArrayList;

public class PawnMovesCalculator {

    ChessBoard board;
    ChessPosition myPosition;
    ArrayList<ChessMove> pieceMovesArray;

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> pieceMovesArray) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceMovesArray = pieceMovesArray;
    }

    public void pawnMovesCalculator(){
        int signAddRow = 1;
        int signAddCol = 1;
        for (int i = 0; i < 3; ++i){

            // i = 0 is ++ quadrant
            // i = 1 is +0 quadrant and is ++0 quadrant
            // i = 2 is +- quadrant

            if (i == 1){
                signAddCol = 0;
            }
            if (i == 2){
                signAddCol = -1;
            }

            if(board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK){
                signAddRow = -1;
            }

            int rowToAdd = myPosition.getRow() + signAddRow;
            int colToAdd = myPosition.getColumn() + signAddCol;
            ChessPosition positionChecker = new ChessPosition(rowToAdd, colToAdd);
            ChessMove newMove = new ChessMove(myPosition, positionChecker, null);

            if ((rowToAdd > 0) && (rowToAdd < 9)
                    && (colToAdd > 0) && (colToAdd < 9)){
                if((i == 0) || (i == 2)) {
                    if ((board.getPiece(positionChecker) != null)
                            && (board.getPiece(positionChecker).pieceColor != board.getPiece(myPosition).pieceColor)) {
                        pawnPromotionLogic(board, myPosition, positionChecker, newMove);
                    }
                }

                if(i == 1){
                    if (board.getPiece(positionChecker) == null) {
                        pawnPromotionLogic(board, myPosition, positionChecker, newMove);
                        //Initial Move Logic
                        if (((newMove.getStartPosition().getRow() == 2) &&
                                (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE))
                                ||
                                ((newMove.getStartPosition().getRow() == 7) &&
                                        (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK))){
                            if ((board.getPiece(positionChecker) == null)){
                                positionChecker = new ChessPosition(rowToAdd + signAddRow, colToAdd);
                                if(board.getPiece(positionChecker) == null){
                                    newMove = new ChessMove(myPosition, positionChecker, null);
                                    pieceMovesArray.add(newMove);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void pawnPromotionLogic(ChessBoard board, ChessPosition myPosition, ChessPosition positionChecker,
                                   ChessMove newMove){
        if (((positionChecker.getRow() == 8) &&
                (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE))
                ||
                ((positionChecker.getRow() == 1) &&
                        (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK))) {
            for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
                if ((piece != ChessPiece.PieceType.KING) && (piece != ChessPiece.PieceType.PAWN)) {
                    newMove = new ChessMove(myPosition, positionChecker, piece);
                    pieceMovesArray.add(newMove);
                }
            }
        }
        else {
            pieceMovesArray.add(newMove);
        }
    }

}