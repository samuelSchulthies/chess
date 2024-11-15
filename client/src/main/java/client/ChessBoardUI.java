package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ChessBoardUI {

    private static String rowLabels;
    private static String columnLabels;
    private static final String SQUARE = "   ";

    final static Map<ChessPiece.PieceType, Character> PIECE_TYPE_TO_CHAR = Map.of(
            ChessPiece.PieceType.BISHOP, 'B',
            ChessPiece.PieceType.KING, 'K',
            ChessPiece.PieceType.KNIGHT, 'N',
            ChessPiece.PieceType.PAWN, 'P',
            ChessPiece.PieceType.QUEEN, 'Q',
            ChessPiece.PieceType.ROOK, 'R'
    );

    static private final ChessBoard gameBoardDefault = new ChessBoard();

    public ChessBoardUI(){
    }

    public static void buildUI(){
        gameBoardDefault.resetBoard();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        // Set for white
        setRowLabels("12345678");
        setColumnLabels("    a  b  c  d  e  f  g  h    ");
        printBoard(out);

        out.print(RESET_BG_COLOR);
        out.println();
        out.println();

        // Set for black
        gameBoardDefault.changeDefaultBoardLayout("BLACK");
        gameBoardDefault.resetBoard();
        setRowLabels("87654321");
        setColumnLabels("    h  g  f  e  d  c  b  a    ");
        printBoard(out);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
        out.println();
    }

    public static void printBoard(PrintStream out){
        labelColumns(out);
        clearBoarders(out);
        out.println();
        labelRows(out);
        labelColumns(out);
    }
    public static void labelColumns(PrintStream out){
        setBoardersGrey(out);

        for(char col : columnLabels.toCharArray()){
            out.print(col);
        }
    }
    public static void labelRows(PrintStream out){
        for(int row = rowLabels.length() - 1; row >= 0; --row){
            setBoardersGrey(out);
            out.print(" ");
            out.print(rowLabels.charAt(row));
            out.print(" ");
            setSquares(out, row);
            setBoardersGrey(out);
            out.print(SQUARE);
            clearBoarders(out);
            out.print("\n");
        }
    }

    public static void setSquares(PrintStream out, int row){
        int colorSwitcher = 1;
        for (int col = 0; col < 8; ++col){
            if (row % 2 == 1){
                colorSwitcher = 0;
            }
            if ((col % 2 == colorSwitcher)) {
                out.print(SET_BG_COLOR_WHITE);
            }
            else {
                out.print(SET_BG_COLOR_BLACK);
            }
            setPiece(out, row, col);
        }
    }

    public static void setPiece(PrintStream out, int row, int col){
        ChessPosition pieceLocation = new ChessPosition(row + 1, col + 1);
        if (gameBoardDefault.getPiece(pieceLocation) == null){
            out.print(SQUARE);
        }
        else {
            out.print(" ");
            if (gameBoardDefault.getPiece(pieceLocation).getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_RED);
            }
            else {
                out.print(SET_TEXT_COLOR_BLUE);
            }
            out.print(PIECE_TYPE_TO_CHAR.get(gameBoardDefault.getPiece(pieceLocation).getPieceType()));
            out.print(" ");
        }
    }

    public static void setBoardersGrey(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    public static void clearBoarders(PrintStream out){
        out.print(RESET_BG_COLOR);
    }
    
    public static void setColumnLabels(String labels){
        columnLabels = labels;
    }

    public static void setRowLabels(String labels){
        rowLabels = labels;
    }
}
