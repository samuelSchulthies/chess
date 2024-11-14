package client;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class ChessBoardUI {

    ChessBoard board = new ChessBoard();

    private static final String rowLabels = "12345678";
    private static final String columnLabels = "   a  b  c  d  e  f  g  h   ";

    public ChessBoardUI(){
        board.resetBoard();
    }

    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        labelColumns(out);
        out.println();
        labelRows(out);
        labelColumns(out);
    }

    public static void labelColumns(PrintStream out){
        setBoarders(out);

        for(char col : columnLabels.toCharArray()){
            out.print(col);
        }
    }
    public static void labelRows(PrintStream out){
        setBoarders(out);

        for(char row : rowLabels.toCharArray()){
            out.print(" ");
            out.println(row);
        }
    }

    public static void setBoarders(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }



}
