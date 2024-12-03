package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ChessBoardUI {

    private static final String ROW_LABELS = "12345678";
    private static String columnLabels;
    private static final PrintStream OUT = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private static final String SQUARE = "   ";
    private static boolean isWhite;
    private static ArrayList<ChessPosition> endPositions = new ArrayList<>();
    private static ChessPosition startPosition;

    final static Map<ChessPiece.PieceType, Character> PIECE_TYPE_TO_CHAR = Map.of(
            ChessPiece.PieceType.BISHOP, 'B',
            ChessPiece.PieceType.KING, 'K',
            ChessPiece.PieceType.KNIGHT, 'N',
            ChessPiece.PieceType.PAWN, 'P',
            ChessPiece.PieceType.QUEEN, 'Q',
            ChessPiece.PieceType.ROOK, 'R'
    );

    static private ChessBoard currentBoard = new ChessBoard();

    public ChessBoardUI(){
    }
    public static void buildUIWhite(ChessBoard updatedBoard){
        isWhite = true;
        setBoard(updatedBoard);

        OUT.print(ERASE_SCREEN);

        OUT.print("Printing board...\n");

        setColumnLabels("    a  b  c  d  e  f  g  h    ");
        printBoard();

        OUT.print(RESET_BG_COLOR);
        OUT.println();

        OUT.print(RESET_TEXT_COLOR);
    }

    public static void buildUIBlack(ChessBoard updatedBoard){
        isWhite = false;
        setBoard(updatedBoard);

        OUT.print(ERASE_SCREEN);

        OUT.print("Printing board...\n");

        setColumnLabels("    h  g  f  e  d  c  b  a    ");
        printBoard();

        OUT.print(RESET_BG_COLOR);
        OUT.println();

        OUT.print(RESET_TEXT_COLOR);
    }

    public static void setHighlightValidMoves(ArrayList<ChessPosition> validMoves, ChessPosition position){
        startPosition = position;
        endPositions = validMoves;
    }

    private static void printBoard(){
        labelColumns();
        clearBoarders();
        OUT.println();
        labelRows();
        labelColumns();

        if ((startPosition != null) && (endPositions.size() != 0)) {
            startPosition = null;
            endPositions.clear();
        }
    }
    private static void labelColumns(){
        setBoardersGrey();

        for(char col : columnLabels.toCharArray()){
            OUT.print(col);
        }
    }
    private static void labelRows(){
        if (isWhite) {
            for (int row = ROW_LABELS.length() - 1; row >= 0; --row) {
                labelRowsHelper(row);
            }
        }
        else {
            for (int row = 0; row < ROW_LABELS.length(); ++row) {
                labelRowsHelper(row);
            }
        }
    }

    private static void labelRowsHelper(int row){
        setBoardersGrey();
        OUT.print(" ");
        OUT.print(ROW_LABELS.charAt(row));
        OUT.print(" ");
        setSquares(row);
        setBoardersGrey();
        OUT.print(SQUARE);
        clearBoarders();
        OUT.print("\n");
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
        ChessPosition highlightChecker = new ChessPosition(row, col);

        if ((startPosition != null) &&
                (highlightChecker.getRow() == startPosition.getRow()) &&
                (highlightChecker.getColumn() == startPosition.getColumn())) {
            OUT.print(SET_BG_COLOR_YELLOW);
        } else {
            if ((col % 2 == colorSwitcher)) {
                if ((endPositions != null) && (endPositions.contains(highlightChecker))){
                    OUT.print(SET_BG_COLOR_GREEN);
                }
                else {
                    OUT.print(SET_BG_COLOR_WHITE);
                }
            }
            else {
                if ((endPositions != null) && (endPositions.contains(highlightChecker))){
                    OUT.print(SET_BG_COLOR_DARK_GREEN);
                }
                else {
                    OUT.print(SET_BG_COLOR_BLACK);
                }
            }
        }

        setPiece(row, col);
    }

    private static void setPiece(int row, int col){
        ChessPosition pieceLocation = new ChessPosition(row + 1, col + 1);
        if (currentBoard.getPiece(pieceLocation) == null){
            OUT.print(SQUARE);
        }
        else {
            OUT.print(" ");
            if (currentBoard.getPiece(pieceLocation).getTeamColor() == ChessGame.TeamColor.WHITE){
                OUT.print(SET_TEXT_COLOR_RED);
            }
            else {
                OUT.print(SET_TEXT_COLOR_BLUE);
            }
            OUT.print(SET_TEXT_BOLD);
            OUT.print(PIECE_TYPE_TO_CHAR.get(currentBoard.getPiece(pieceLocation).getPieceType()));
            OUT.print(" ");
            OUT.print(RESET_TEXT_BOLD_FAINT);
        }
    }

    private static void setBoardersGrey(){
        OUT.print(SET_BG_COLOR_LIGHT_GREY);
        OUT.print(SET_TEXT_COLOR_BLACK);
    }

    private static void clearBoarders(){
        OUT.print(RESET_BG_COLOR);
    }
    
    private static void setColumnLabels(String labels){
        columnLabels = labels;
    }

    private static void setBoard(ChessBoard updatedBoard){
        currentBoard = updatedBoard;
    }
}
