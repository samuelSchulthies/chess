package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    static private ChessBoard gameBoard;
    static TeamColor teamTurn;
    static ChessPosition kingLocation;

    private ChessPiece pieceStorage;
    Boolean exception = false;

    public ChessGame() {

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
            Collection<ChessMove> possibleMoves = getBoard().getPiece(startPosition).pieceMoves(gameBoard, startPosition);
            Collection<ChessMove> validMoves = new ArrayList<>();

            for (ChessMove possibleMove : possibleMoves){
                exception = false;
                pieceStorage = gameBoard.getPiece(possibleMove.getStartPosition());
                try {
                    makeMove(possibleMove);
                    if (!exception) {
                        validMoves.add(possibleMove);
                    }
                }
                catch (InvalidMoveException e) {
                    System.out.print("Not including invalid move");
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
        setTeamTurn(gameBoard.getPiece(move.getStartPosition()).getTeamColor());

        if (gameBoard.getPiece(move.getEndPosition()) != null) {
            pieceStorage = gameBoard.getPiece(move.getEndPosition());
        }
        gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
        gameBoard.removePiece(move.getStartPosition());

        if(isInCheck(getTeamTurn())){
            exception = true;
            if(isInCheckmate(getTeamTurn())){
                throw new InvalidMoveException("The move " + move + " puts the king in checkmate");
            }
            else {
                throw new InvalidMoveException("The move " + move + " puts the king in check");
            }
        }
    }

    public void undoMove(ChessMove move){
        gameBoard.addPiece(move.getStartPosition(), pieceStorage);
        gameBoard.removePiece(move.getEndPosition());
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

        for (row = 1; row < 9; ++row) {
            for (col = 1; col < 9; ++col) {
                ChessPosition positionChecker = new ChessPosition(row, col);

                if (gameBoard.getPiece(positionChecker) != null) {
                    if ((gameBoard.getPiece(positionChecker).getPieceType() == ChessPiece.PieceType.KING) &&
                            (gameBoard.getPiece(positionChecker).getTeamColor() == teamColor)) {
                        kingLocation = new ChessPosition(row, col);
                    }
                }
            }
        }

        for (row = 1; row < 9; ++row) {
            for (col = 1; col < 9; ++col) {
                ChessPosition positionChecker = new ChessPosition(row, col);
                if (gameBoard.getPiece(positionChecker) != null) {
                    if (gameBoard.getPiece(positionChecker).getTeamColor() != teamColor) {
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

        Collection<ChessMove> validKingMovesCollection = new ArrayList<>();
        HashSet<ChessPosition> validKingSet = new HashSet<>();


        for (row = 1; row < 9; ++row) {
            for (col = 1; col < 9; ++col) {
                ChessPosition positionChecker = new ChessPosition(row, col);

                if (gameBoard.getPiece(positionChecker) != null) {
                    if ((gameBoard.getPiece(positionChecker).getPieceType() == ChessPiece.PieceType.KING) &&
                            (gameBoard.getPiece(positionChecker).getTeamColor() == teamColor)) {
                        validKingMovesCollection = getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker);
                    }
                }
            }
        }

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

        for (row = 1; row < 9; ++row) {
            for (col = 1; col < 9; ++col) {
                ChessPosition positionChecker = new ChessPosition(row, col);

                //TODO: Need logic that retrieves all squares controlled by a piece, not just valid moves
                //TODO: Maybe retrieve valid moves again based on squares a piece controls
                //TODO: if king moves and a new valid move comes up (pawn), then add that move to attacks against the king
                //TODO: Make every move from valid moves and check if that move has put the king into check or checkmate
                //TODO: All we need to do is call valid moves on every valid move from a piece and then run
                // check/checkmate to see if our king has been put in danger. If it returns true we discard those moves
                //TODO: Move the king to each of its valid moves and then check at each of those moves if it has a new
                // attack (likely from pawn). Add that to list of possible attacks against the king.

                if (gameBoard.getPiece(positionChecker) != null) {
                    if (gameBoard.getPiece(positionChecker).getTeamColor() != teamColor) {
                        Collection<ChessMove> validMovesCollection = getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker);
                        for (ChessMove move : validMovesCollection) {
                            for (ChessMove kingMove : validKingMovesCollection) {
                                if ((move.getEndPosition().getRow() == kingMove.getEndPosition().getRow()) &&
                                        (move.getEndPosition().getColumn() == kingMove.getEndPosition().getColumn())){
                                    validKingSet.add(move.getEndPosition());
                                }
                            }
                        }
                    }
                }
            }
        }
        return validKingSet.size() == kingMovesSize;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
//
//        //TODO: Stalemate is when the current team has no valid moves
//
//        int row;
//        int col;
//        for (row = 1; row < 9; ++row) {
//            for (col = 1; col < 9; ++col) {
//                ChessPosition positionChecker = new ChessPosition(row, col);
//
//                if (gameBoard.getPiece(positionChecker) != null) {
//                    if ((gameBoard.getPiece(positionChecker).getTeamColor() == teamColor)) {
//                        if (getBoard().getPiece(positionChecker).pieceMoves(gameBoard, positionChecker).size() != 0){
//                            return false;
//                        }
//                    }
//                }
//            }
//        }
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
