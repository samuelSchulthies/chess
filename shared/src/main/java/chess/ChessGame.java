package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    static private ChessBoard gameBoard = new ChessBoard();
    static int moveIndicator;
    static ChessBoard defaultBoard = new ChessBoard();
    static TeamColor teamTurn;
//    static ChessPosition kingLocation;
    private ChessPiece pieceStorage;

    Boolean validMovesFlag = false;
    Boolean exception = false;

    public ChessGame() {
        gameBoard.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        moveIndicator = 0;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (getBoard().getPiece(startPosition) != null) {
            Collection<ChessMove> possibleMoves = new ArrayList<>(getBoard().getPiece(startPosition).pieceMoves(gameBoard, startPosition));
            Collection<ChessMove> validMoves = new ArrayList<>();
            setTeamTurn(gameBoard.getPiece(startPosition).getTeamColor());

            for (ChessMove possibleMove : possibleMoves){
                exception = false;
                pieceStorage = gameBoard.getPiece(possibleMove.getStartPosition());
                try {
                    validMovesFlag = true;
                    makeMove(possibleMove);
                    if (!exception) {
                        validMoves.add(possibleMove);
                    }
                }
                catch (InvalidMoveException e) {
                    System.out.print("The move " + possibleMove + " was illegal and not included\n");
                }
                finally {
                    undoMove(possibleMove);
                }
            }
            return validMoves;
        }
        else {
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
//        if (!validMovesFlag) {
//            if (moveIndicator % 2 == 0) {
//                setTeamTurn(TeamColor.WHITE);
//            } else {
//                setTeamTurn(TeamColor.BLACK);
//            }
//            moveIndicator++;
//        }

        if (getBoard().getPiece(move.getStartPosition()) != null){

            if(gameBoard.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
                throw new InvalidMoveException("It is not " + gameBoard.getPiece(move.getStartPosition())
                        .getTeamColor() + "'s turn");
            }

            if (gameBoard.getPiece(move.getEndPosition()) != null) {
                pieceStorage = gameBoard.getPiece(move.getEndPosition());
            }
            else {
                pieceStorage = null;
            }

            if(!isInMoveSet(move)){
                throw new InvalidMoveException("The move " + move + " is not in this piece's valid moves");
            }

            if((move.getPromotionPiece() != null) &&
                    (getBoard().getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN)){
                ChessPiece promotionUpdate = new ChessPiece(getBoard().getPiece(move.getStartPosition())
                        .getTeamColor(), move.getPromotionPiece());
                gameBoard.addPiece(move.getEndPosition(), promotionUpdate);
//                if(getTeamTurn() == TeamColor.BLACK){
//                    setTeamTurn(TeamColor.WHITE);
//                }
//                if(getTeamTurn() == TeamColor.WHITE){
//                    setTeamTurn(TeamColor.BLACK);
//                }
            }
            else {
                gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
            }
            gameBoard.removePiece(move.getStartPosition());

            if (isInCheck(getTeamTurn())) {
                exception = true;
//                if (isInCheckmate(getTeamTurn())) {
//    //                undoMove(move);
//                    throw new InvalidMoveException("The move " + move + " puts the king in checkmate");
//                }
//                else {
    //                undoMove(move);
                    throw new InvalidMoveException("The move " + move + " puts the king in check");
//                }
            }
        }
        else {
            throw new InvalidMoveException("The move " + move + "does not have a chess piece");
        }
        if (!validMovesFlag) {
            if (getTeamTurn() == TeamColor.BLACK) {
                setTeamTurn(TeamColor.WHITE);
            }
            else {
                setTeamTurn(TeamColor.BLACK);
            }
        }
    }

    public void undoMove(ChessMove move){
        gameBoard.addPiece(move.getStartPosition(), gameBoard.getPiece(move.getEndPosition()));
        gameBoard.addPiece(move.getEndPosition(), pieceStorage);
    }

    public boolean isInMoveSet(ChessMove moveToCheck){
        Collection<ChessMove> moveSet = new ArrayList<>(getBoard().getPiece(moveToCheck.getStartPosition()).pieceMoves(gameBoard, moveToCheck.getStartPosition()));
        for (ChessMove validMove : moveSet) {
            if ((validMove.getEndPosition().getRow() == moveToCheck.getEndPosition().getRow()) &&
                    (validMove.getEndPosition().getColumn() == moveToCheck.getEndPosition().getColumn())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        int row;
        int col;
//        kingLocation = null;

        ChessPosition kingLocation = getKingLocation(teamColor);

//        for (row = 1; row < 9; ++row) {
//            for (col = 1; col < 9; ++col) {
//                ChessPosition positionChecker = new ChessPosition(row, col);
//
//                if (gameBoard.getPiece(positionChecker) != null) {
//                    if ((gameBoard.getPiece(positionChecker).getPieceType() == ChessPiece.PieceType.KING) &&
//                            (gameBoard.getPiece(positionChecker).getTeamColor() == teamColor)) {
//                        kingLocation = new ChessPosition(row, col);
//                    }
//                }
//            }
//        }

        if(kingLocation == null){
            return false;
        }

        for (row = 1; row < 9; ++row) {
            for (col = 1; col < 9; ++col) {
                ChessPosition positionChecker = new ChessPosition(row, col);
                if (gameBoard.getPiece(positionChecker) != null) {
                    if (gameBoard.getPiece(positionChecker).getTeamColor() != teamColor) {
                        getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker).clear();
                        Collection<ChessMove> validMovesCollection = getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker);
                        for (ChessMove move : validMovesCollection) {
                            if ((move.getEndPosition().getRow() == kingLocation.getRow()) &&
                                    (move.getEndPosition().getColumn() == kingLocation.getColumn())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        int row;
        int col;
//        kingLocation = null;

        Collection<ChessMove> validKingMovesCollection = validKingMoves(teamColor);
        HashSet<ChessPosition> validKingSet = new HashSet<>();

        //Checks for pawn controlled squares
        for (ChessMove kingMove : validKingMovesCollection) {
            int signRow = 1;
            int signCol = 1;
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

                int rowToAdd = kingMove.getEndPosition().getRow() + (signRow);
                int colToAdd = kingMove.getEndPosition().getColumn() + (signCol);
                ChessPosition pawnPositionChecker = new ChessPosition(rowToAdd, colToAdd);

                if ((rowToAdd > 0) && (rowToAdd < 9)
                        && (colToAdd > 0) && (colToAdd < 9)) {
                    if ((gameBoard.getPiece(pawnPositionChecker) != null)
                            && (gameBoard.getPiece(pawnPositionChecker).getPieceType() == ChessPiece.PieceType.PAWN)) {
                        ChessMove pawnControlled = new ChessMove(pawnPositionChecker, kingMove.getEndPosition(), null);
                        validKingSet.add(pawnControlled.getEndPosition());
                    }
                }
            }
        }

        int kingMovesSize = validKingMovesCollection.size();

        if(kingMovesSize == 0) {
            if(!isInCheck(teamColor)){
                return false;
            }
            for (row = 1; row < 9; ++row) {
                for (col = 1; col < 9; ++col) {
                    ChessPosition positionChecker = new ChessPosition(row, col);

                    if ((gameBoard.getPiece(positionChecker) != null) &&
                            (gameBoard.getPiece(positionChecker).getTeamColor() == teamColor)) {
                        getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker).clear();
                        Collection<ChessMove> validMovesCollection = new ArrayList<>(getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker));
                        for (ChessMove move : validMovesCollection) {
//                            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
//                            gameBoard.removePiece(move.getStartPosition());

                            try {
                                makeMove(move);
                                moveIndicator--;
                                System.out.print(pieceStorage);
                            }
                            catch (InvalidMoveException e) {
                                System.out.print("The move " + move + " is illegal\n");
                            }

                            getBoard().getPiece(getKingLocation(teamColor)).pieceMoves(gameBoard, getKingLocation(teamColor)).clear();
                            if(!isInCheck(teamColor)){
                                undoMove(move);
//                                moveIndicator--;
                                return false;
                            }
                            undoMove(move);
//                            moveIndicator--;
                        }
                    }


//                    if (gameBoard.getPiece(positionChecker) != null) {
//                        if (gameBoard.getPiece(positionChecker).getTeamColor() != teamColor) {
//                            getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker).clear();
//                            Collection<ChessMove> validMovesCollection = getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker);
//                            for (ChessMove move : validMovesCollection) {
//                                for (ChessMove kingMove : validKingMovesCollection) {
//                                    if ((move.getEndPosition().getRow() == kingMove.getEndPosition().getRow()) &&
//                                            (move.getEndPosition().getColumn() == kingMove.getEndPosition().getColumn())) {
//                                        validKingSet.add(move.getEndPosition());
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            }
        }
        return kingMovesSize == 0;

        //Move the king to each of its moves and check all the moves on the board
//        for (ChessMove kingMove : validKingMovesCollection) {
//            gameBoard.addPiece(kingMove.getEndPosition(), gameBoard.getPiece(kingMove.getStartPosition()));
//            gameBoard.removePiece(kingMove.getStartPosition());
//            for (row = 1; row < 9; ++row) {
//                for (col = 1; col < 9; ++col) {
//                    ChessPosition positionChecker = new ChessPosition(row, col);
//                    if (gameBoard.getPiece(positionChecker) != null) {
//                        if (gameBoard.getPiece(positionChecker).getTeamColor() != teamColor) {
//                            getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker).clear();
//                            Collection<ChessMove> validMovesCollection = getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker);
////                            Collection<ChessMove> validMovesCollection = validMoves(positionChecker);
//                            for (ChessMove move : validMovesCollection) {
//                                if ((move.getEndPosition().getRow() == kingMove.getEndPosition().getRow()) &&
//                                        (move.getEndPosition().getColumn() == kingMove.getEndPosition().getColumn())) {
//                                    validKingSet.add(move.getEndPosition());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            undoMove(kingMove);
//        }
//
//        //Move each piece on the board and check the king's moves for new openings
//        for (row = 1; row < 9; ++row) {
//            for (col = 1; col < 9; ++col) {
//                ChessPosition positionChecker = new ChessPosition(row, col);
//                if (gameBoard.getPiece(positionChecker) != null) {
//                    if ((gameBoard.getPiece(positionChecker).getTeamColor() == teamColor) &&
//                            (gameBoard.getPiece(positionChecker).getPieceType() != ChessPiece.PieceType.KING)) {
//                        getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker).clear();
//                        Collection<ChessMove> validMovesCollection = new ArrayList<>(getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker));
//                        for (ChessMove move : validMovesCollection) {
//                            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
//                            gameBoard.removePiece(move.getStartPosition());
//                            validKingMoves(teamColor);
////                            validKingSet.add(move.getEndPosition());
//                            for (ChessMove kingMove : validKingMovesCollection) {
//                                if ((move.getEndPosition().getRow() == kingMove.getEndPosition().getRow()) &&
//                                        (move.getEndPosition().getColumn() == kingMove.getEndPosition().getColumn())){
//                                    validKingSet.add(move.getEndPosition());
//                                }
//                            }
//                            undoMove(move);
//                        }
//                    }
//                }
//            }
//        }

//        if ((validKingSet.size() != 0) && (kingMovesSize != 0)) {
//            return validKingSet.size() == kingMovesSize;
//        }
//        else {
//            return false;
//        }
    }

    public Collection<ChessMove> validKingMoves(TeamColor teamColor){
        Collection<ChessMove> validKingMovesCollection = new ArrayList<>();
        for (int row = 1; row < 9; ++row) {
            for (int col = 1; col < 9; ++col) {
                ChessPosition positionChecker = new ChessPosition(row, col);
                if (gameBoard.getPiece(positionChecker) != null) {
                    if ((gameBoard.getPiece(positionChecker).getPieceType() == ChessPiece.PieceType.KING) &&
                            (gameBoard.getPiece(positionChecker).getTeamColor() == teamColor)) {
                        getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker).clear();
                        validKingMovesCollection = validMoves(positionChecker);
                    }
                }
            }
        }
        return validKingMovesCollection;
    }

    public ChessPosition getKingLocation(TeamColor teamColor){
        int row;
        int col;

        for (row = 1; row < 9; ++row) {
            for (col = 1; col < 9; ++col) {
                ChessPosition positionChecker = new ChessPosition(row, col);
                if (gameBoard.getPiece(positionChecker) != null) {
                    if ((gameBoard.getPiece(positionChecker).getPieceType() == ChessPiece.PieceType.KING) &&
                            (gameBoard.getPiece(positionChecker).getTeamColor() == teamColor)) {
                        return positionChecker;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        //TODO: stalemate returns a false positive because, while there are valid moves left, it disregards all of
        // those moves because the king is already in check
        if (isInCheckmate(teamColor)){
            return false;
        }
        int row;
        int col;
        for (row = 1; row < 9; ++row) {
            for (col = 1; col < 9; ++col) {
                ChessPosition positionChecker = new ChessPosition(row, col);

                if (gameBoard.getPiece(positionChecker) != null) {
                    if ((gameBoard.getPiece(positionChecker).getTeamColor() == teamColor)) {
                        if (validMoves(positionChecker).size() != 0){
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }
}
