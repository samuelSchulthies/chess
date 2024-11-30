package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ChessBoardUI {

    private static String rowLabels = "12345678";
    private static String columnLabels;
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private static final String SQUARE = "   ";
    private static boolean isWhite;

    final static Map<ChessPiece.PieceType, Character> PIECE_TYPE_TO_CHAR = Map.of(
            ChessPiece.PieceType.BISHOP, 'B',
            ChessPiece.PieceType.KING, 'K',
            ChessPiece.PieceType.KNIGHT, 'N',
            ChessPiece.PieceType.PAWN, 'P',
            ChessPiece.PieceType.QUEEN, 'Q',
            ChessPiece.PieceType.ROOK, 'R'
    );

    static private final ChessBoard GAME_BOARD_DEFAULT = new ChessBoard();

    static private ChessBoard currentBoard = new ChessBoard();

    public ChessBoardUI(){
    }

//    public static void main(String[] args){
//        buildUIBlack(currentBoard);
//    }
    public static void buildUIWhite(ChessBoard updatedBoard){
//        GAME_BOARD_DEFAULT.changeDefaultBoardLayout("WHITE");
//        GAME_BOARD_DEFAULT.resetBoard();
        isWhite = true;
        setBoard(updatedBoard);

        out.print(ERASE_SCREEN);

        out.print("Printing board...\n");

//        setRowLabels("12345678");
        setColumnLabels("    a  b  c  d  e  f  g  h    ");
        printBoard();

        out.print(RESET_BG_COLOR);
        out.println();

        out.print(RESET_TEXT_COLOR);
    }

    public static void buildUIBlack(ChessBoard updatedBoard){
//        currentBoard.changeDefaultBoardLayout("BLACK");
//        currentBoard.resetBoard();
//        GAME_BOARD_DEFAULT.resetBoard();
//        setBoard(GAME_BOARD_DEFAULT);
        isWhite = false;
        setBoard(updatedBoard);

        out.print(ERASE_SCREEN);

        out.print("Printing board...\n");

//        setRowLabels("87654321");
        setColumnLabels("    h  g  f  e  d  c  b  a    ");
        printBoard();

        out.print(RESET_BG_COLOR);
        out.println();

        out.print(RESET_TEXT_COLOR);
    }

    private static void printBoard(){
        labelColumns();
        clearBoarders();
        out.println();
        labelRows();
        labelColumns();
    }
    private static void labelColumns(){
        setBoardersGrey();

        for(char col : columnLabels.toCharArray()){
            out.print(col);
        }
    }
    private static void labelRows(){
        if (isWhite) {
            for (int row = rowLabels.length() - 1; row >= 0; --row) {
                labelRowsHelper(row);
            }
        }
        else {
            for (int row = 0; row < rowLabels.length(); ++row) {
                labelRowsHelper(row);
            }
        }
    }

    private static void labelRowsHelper(int row){
        setBoardersGrey();
        out.print(" ");
        out.print(rowLabels.charAt(row));
        out.print(" ");
        setSquares(row);
        setBoardersGrey();
        out.print(SQUARE);
        clearBoarders();
        out.print("\n");
    }

    private static void setSquares(int row){
        if (isWhite) {
            for (int col = 0; col < 8; ++col) {
                setSquaresHelper(row, col);
            }
        }
        else {
            for (int col = 7; col >= 0; --col) {
                setSquaresHelper(row, col);
            }
        }
    }

    private static void setSquaresHelper(int row, int col){
        int colorSwitcher = 1;
        if (row % 2 == 1){
            colorSwitcher = 0;
        }
        if ((col % 2 == colorSwitcher)) {
            out.print(SET_BG_COLOR_WHITE);
        }
        else {
            out.print(SET_BG_COLOR_BLACK);
        }
        setPiece(row, col);
    }

    private static void setPiece(int row, int col){
        ChessPosition pieceLocation = new ChessPosition(row + 1, col + 1);
        if (currentBoard.getPiece(pieceLocation) == null){
            out.print(SQUARE);
        }
        else {
            out.print(" ");
            if (currentBoard.getPiece(pieceLocation).getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_RED);
            }
            else {
                out.print(SET_TEXT_COLOR_BLUE);
            }
            out.print(SET_TEXT_BOLD);
            out.print(PIECE_TYPE_TO_CHAR.get(currentBoard.getPiece(pieceLocation).getPieceType()));
            out.print(" ");
            out.print(RESET_TEXT_BOLD_FAINT);
        }
    }

    private static void setBoardersGrey(){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void clearBoarders(){
        out.print(RESET_BG_COLOR);
    }
    
    private static void setColumnLabels(String labels){
        columnLabels = labels;
    }

    private static void setRowLabels(String labels){
        rowLabels = labels;
    }

    private static void setBoard(ChessBoard updatedBoard){
        currentBoard = updatedBoard;
    }
}
