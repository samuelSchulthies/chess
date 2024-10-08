package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public int ADD_HELPER;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    ArrayList<ChessMove> pieceMovesArray = new ArrayList<>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(getPieceType() == PieceType.BISHOP){
            int signRow = 1;
            int signCol = 1;
            for (int i = 0; i < 4; ++i){
                ADD_HELPER = 0;

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

                pieceMovesRecurser(board, myPosition, signRow, signCol);

            }
        }

        if(getPieceType() == PieceType.KING){
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

        if(getPieceType() == PieceType.KNIGHT){
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
                    if (j == 0){
                        addHelperRow = 2;
                        addHelperCol = 1;
                    }
                    if (j == 1){
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

        if(getPieceType() == PieceType.PAWN){
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

        if(getPieceType() == PieceType.QUEEN){
            int signRow = 1;
            int signCol = 1;

            for (int i = 0; i < 8; ++i) {
                ADD_HELPER = 0;

                // i = 0 is ++ quadrant
                // i = 1 is -+ quadrant
                // i = 2 is -- quadrant
                // i = 3 is +- quadrant

                // i = 4 is +0 quadrant
                // i = 5 is 0- quadrant
                // i = 6 is -0 quadrant
                // i = 7 is 0+ quadrant

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

                if (i == 4) {
                    signRow = 1;
                    signCol = 0;
                }
                if (i == 5) {
                    signRow = 0;
                    signCol = -1;
                }
                if (i == 6) {
                    signRow = -1;
                    signCol = 0;
                }
                if (i == 7) {
                    signRow = 0;
                    signCol = 1;
                }

                pieceMovesRecurser(board, myPosition, signRow, signCol);
            }
        }

        if(getPieceType() == PieceType.ROOK){
            int signRow = 1;
            int signCol = 0;

            for (int i = 0; i < 4; ++i) {
                ADD_HELPER = 0;

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

                pieceMovesRecurser(board, myPosition, signRow, signCol);
            }
        }
        return pieceMovesArray;
    }

    public void pieceMovesRecurser(ChessBoard board, ChessPosition myPosition, int signRow, int signCol) {
        ADD_HELPER += 1;
        int rowToAdd = myPosition.getRow() + (ADD_HELPER * signRow);
        int colToAdd = myPosition.getColumn() + (ADD_HELPER * signCol);
        ChessPosition positionChecker = new ChessPosition(rowToAdd, colToAdd);
        ChessMove newMove = new ChessMove(myPosition, positionChecker, null);

        if ((rowToAdd > 0) && (rowToAdd < 9)
                && (colToAdd > 0) && (colToAdd < 9)){
            //Capture
            if ((board.getPiece(positionChecker) != null)
                    && (board.getPiece(positionChecker).pieceColor != board.getPiece(myPosition).pieceColor)){
                pieceMovesArray.add(newMove);
            }
            if (board.getPiece(positionChecker) == null){
                pieceMovesArray.add(newMove);
            }
            if (((positionChecker.getRow() + (signRow)) > 0)
                    && ((positionChecker.getRow() + (signRow)) < 9)
                    && ((positionChecker.getColumn() + (signCol)) > 0)
                    && ((positionChecker.getColumn() + (signCol)) < 9)){
                if (board.getPiece(positionChecker) == null){
                    pieceMovesRecurser(board, myPosition, signRow, signCol);
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
            for (PieceType piece : PieceType.values()) {
                if ((piece != PieceType.KING) && (piece != PieceType.PAWN)) {
                    newMove = new ChessMove(myPosition, positionChecker, piece);
                    pieceMovesArray.add(newMove);
                }
            }
        }
        else {
            pieceMovesArray.add(newMove);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
