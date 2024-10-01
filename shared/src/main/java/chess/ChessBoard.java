package chess;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    String defaultBoardLayout = """
                |r|n|b|q|k|b|n|r|
                |p|p|p|p|p|p|p|p|
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                |P|P|P|P|P|P|P|P|
                |R|N|B|Q|K|B|N|R|
                """;

    final static Map<Character, ChessPiece.PieceType> CHAR_TO_PIECE_TYPE = Map.of(
            'b', ChessPiece.PieceType.BISHOP,
            'k', ChessPiece.PieceType.KING,
            'n', ChessPiece.PieceType.KNIGHT,
            'p', ChessPiece.PieceType.PAWN,
            'q', ChessPiece.PieceType.QUEEN,
            'r', ChessPiece.PieceType.ROOK);

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    public ChessBoard getDefaultBoard(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        return board;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public String toString() {
        StringBuilder stringChessPieces = new StringBuilder();

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (this.squares[i][j] != null) {
                    stringChessPieces.append("(");
                    stringChessPieces.append(squares[i][j].getPieceType());
                    stringChessPieces.append(",");
                    stringChessPieces.append(squares[i][j].getTeamColor());
                    stringChessPieces.append(")");
                }
                else {
                    stringChessPieces.append("NULL");
                }
            }
            stringChessPieces.append("--");
        }
        return stringChessPieces.toString();
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        int row = 8;
        int column = 1;
        for (char boardChar : defaultBoardLayout.toCharArray()) {
            switch (boardChar) {
                case '\n' -> {
                    column = 1;
                    row--;
                }
                case ' ' -> column++;
                case '|' -> {
                }
                default -> {
                    ChessGame.TeamColor color = Character.isLowerCase(boardChar) ? ChessGame.TeamColor.BLACK
                            : ChessGame.TeamColor.WHITE;
                    var type = CHAR_TO_PIECE_TYPE.get(Character.toLowerCase(boardChar));
                    var position = new ChessPosition(row, column);
                    var piece = new ChessPiece(color, type);
                    addPiece(position, piece);
                    column++;
                }
            }
        }
    }

    //TODO: Understand this code from IntelliJ
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessBoard that = (ChessBoard) o;

        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}


