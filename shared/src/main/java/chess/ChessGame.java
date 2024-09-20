package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard gameBoard;
    TeamColor teamTurn;
    ChessPosition kingLocation;

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
            //TODO: Add logic that removes moves that put the king in check
            return getBoard().getPiece(startPosition).pieceMoves(gameBoard, startPosition);
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
        throw new RuntimeException("Not implemented");
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
                Collection<ChessMove> validMovesCollection = validMoves(positionChecker);

                if (gameBoard.getPiece(positionChecker) != null) {
                    if (gameBoard.getPiece(positionChecker).getTeamColor() != teamColor) {
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
        //TODO: Checkmate logic Next
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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

        if (!Objects.equals(gameBoard, chessGame.gameBoard)) return false;
        return teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        int result = gameBoard != null ? gameBoard.hashCode() : 0;
        result = 31 * result + (teamTurn != null ? teamTurn.hashCode() : 0);
        return result;
    }
}
