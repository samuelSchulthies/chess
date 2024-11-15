package client;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ChessBoardUI {

    private static final String ROW_LABELS = "12345678";
    private static final String COLUMN_LABELS = "    h  g  f  e  d  c  b  a    ";
    private static final String SQUARE = "   ";
    final static Map<ChessPiece.PieceType, Character> PIECE_TYPE_TO_CHAR = Map.of(
            ChessPiece.PieceType.BISHOP, 'B',
            ChessPiece.PieceType.KING, 'K',
            ChessPiece.PieceType.KNIGHT, 'N',
            ChessPiece.PieceType.PAWN, 'P',
            ChessPiece.PieceType.QUEEN, 'Q',
            ChessPiece.PieceType.ROOK, 'R'
    );

    static private ChessBoard gameBoardDefault = new ChessBoard();

    public ChessBoardUI(){
    }

    public static void main(String[] args){
        gameBoardDefault.resetBoard();
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
        for (int col = 0; col < 8; ++col){
            if (row % 2 == 1){
                colorSwitcher = 1;
            }
            if ((col % 2 == colorSwitcher) && (col != 8)) {
                out.print(SET_BG_COLOR_WHITE);
                out.print(SET_TEXT_COLOR_BLACK);
            }
            else if (col == 8){
                setBoardersGrey(out);
            }
            else {
                out.print(SET_BG_COLOR_BLACK);
                out.print(SET_TEXT_COLOR_WHITE);
            }
            ChessPosition pieceLocation = new ChessPosition(row + 1, col + 1);
            if (gameBoardDefault.getPiece(pieceLocation) == null){
                out.print(SQUARE);
            }
            else {
                out.print(" ");
                out.print(PIECE_TYPE_TO_CHAR.get(gameBoardDefault.getPiece(pieceLocation).getPieceType()));
                out.print(" ");
            }
        }
    }

    public static void setPiece(PrintStream out){

    }

    public static void setBoardersGrey(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    public static void clearBoarders(PrintStream out){
        out.print(RESET_BG_COLOR);
    }



}
