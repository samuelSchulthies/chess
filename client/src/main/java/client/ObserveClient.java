package client;

import exception.DataAccessException;

import java.util.Arrays;

public class ObserveClient {

    public String eval(String input){
        try {
            var tokensFromUser = input.toLowerCase().split(" ");
            var cmd = (tokensFromUser.length > 0) ? tokensFromUser[0] : "help";
            var params = Arrays.copyOfRange(tokensFromUser, 1, tokensFromUser.length);

            return switch (cmd){
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (DataAccessException e){
            return e.getMessage();
        }
    }

    public String redraw() throws DataAccessException{
        return "redraw";
    }

    public String leave() throws DataAccessException{
        return "leave";
    }

    public String highlight(String... params) throws DataAccessException{
        return "highlight";
    }

    public String help(){
        return """
                
                redraw                         - Redraws the chess board
                leave                          - Stop observing
                highlight <PIECE_POSITION>     - shows all valid moves for the piece at that position
                help
                """;
    }

}
