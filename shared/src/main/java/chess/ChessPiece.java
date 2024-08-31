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
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    HashSet<ChessMove> pieceMovesArray = new HashSet<>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //TODO: Algorithm:
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

                bishopRecurser(board, myPosition, signRow, signCol);

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
                                //Promotion Logic
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
                        }

                    if(i == 1){
                        if (board.getPiece(positionChecker) == null) {
                            //Promotion Logic
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


        return pieceMovesArray;
    }

    public void bishopRecurser(ChessBoard board, ChessPosition myPosition, int signRow, int signCol) {
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
            if ((rowToAdd > 1) && (rowToAdd < 8)
                    && (colToAdd > 1) && (colToAdd < 8)){
                // REMOVED as we can ONLY continue if it's null. If you capture you stop!
//                if (board.getPiece(positionChecker) != null){
//                    if (board.getPiece(positionChecker).pieceColor != board.getPiece(myPosition).pieceColor){
//                        bishopRecurser(board, myPosition, signRow, signCol);
//                    }
//                }
                if (board.getPiece(positionChecker) == null){
                    bishopRecurser(board, myPosition, signRow, signCol);
                }
            }
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


//        Hard coded test:
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(6,3), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(2,7), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(8,7), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(4,5), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(7,6), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(4,3), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(6,5), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(7,2), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(8,1), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(3,2), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(3,6), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(2,1), null));
//        bishopArray.add(new ChessMove(myPosition, new ChessPosition(1,8), null));


//    //White is default positive
//    int signRow = 1;
//    int signCol = 1;
//    int add_helper = 0;
//    int repeat = 1;
//            if(board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK){
//                    signRow = -1;
//                    }
//
//                    for (int i = 0; i < repeat; ++i) {
//        add_helper += 1;
//        int rowToAdd = myPosition.getRow() + (add_helper * signRow);
//        int colToAdd = myPosition.getColumn();
//        ChessPosition positionChecker = new ChessPosition(rowToAdd, colToAdd);
//        ChessMove newMove = new ChessMove(myPosition, positionChecker, null);
//
//        if(myPosition == newMove.getStartPosition()){
//        repeat = 2;
//        }
//
//        if ((rowToAdd > 0) && (rowToAdd < 9)
//        && (colToAdd > 0) && (colToAdd < 9)) {
//        if ((board.getPiece(positionChecker) != null)
//        && (board.getPiece(positionChecker).pieceColor != board.getPiece(myPosition).pieceColor)) {
//        pieceMovesArray.add(newMove);
//        }
//        if (board.getPiece(positionChecker) == null) {
//        pieceMovesArray.add(newMove);
//        }
//        }
//        }

//Manual Promotion Logic

//                                    newMove = new ChessMove(myPosition, positionChecker, PieceType.QUEEN);
//                                    pieceMovesArray.add(newMove);
//                                    newMove = new ChessMove(myPosition, positionChecker, PieceType.BISHOP);
//                                    pieceMovesArray.add(newMove);
//                                    newMove = new ChessMove(myPosition, positionChecker, PieceType.KNIGHT);
//                                    pieceMovesArray.add(newMove);
//                                    newMove = new ChessMove(myPosition, positionChecker, PieceType.ROOK);
//                                    pieceMovesArray.add(newMove);