package client;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class ChessBoardUI {

    ChessBoard board = new ChessBoard();

    private static final String ROW_LABELS = "12345678";
    private static final String COLUMN_LABELS = "    h  g  f  e  d  c  b  a    ";
    private static final String SQUARE = "   ";

    public ChessBoardUI(){
        board.resetBoard();
    }

    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        labelColumns(out);
        clearBoarders(out);
        out.println();
        labelRows(out);
        labelColumns(out);
    }

    public static void labelColumns(PrintStream out){
        setBoardersGrey(out);

        for(char col : COLUMN_LABELS.toCharArray()){
            out.print(col);
        }
    }
    public static void labelRows(PrintStream out){

        for(int row = 0; row < ROW_LABELS.length(); ++row){
            setBoardersGrey(out);
            out.print(" ");
            out.print(ROW_LABELS.charAt(row));
            out.print(" ");
            setSquares(out, row);
            clearBoarders(out);
            out.print("\n");
        }
    }

    public static void setSquares(PrintStream out, int row){
        int colorSwitcher = 0;
        for (int col = 0; col < 9; ++col){
            if (row % 2 == 1){
                colorSwitcher = 1;
            }
            if ((col % 2 == colorSwitcher) && (col != 8)) {
                out.print(SET_BG_COLOR_WHITE);
            }
            else if (col == 8){
                setBoardersGrey(out);
            }
            else {
                out.print(SET_BG_COLOR_BLACK);
            }
            out.print(SQUARE);
        }
    }

    public static void setBoardersGrey(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    public static void clearBoarders(PrintStream out){
        out.print(RESET_BG_COLOR);
    }



}
