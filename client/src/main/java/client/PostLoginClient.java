package client;

import chess.ChessGame;
import chess.ChessPiece;
import dataaccess.DataAccessException;
import model.GameData;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import requestresult.ListResult;
import server.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PostLoginClient {
    private final ServerFacade server;
    private String authToken = "";
    private UserStatus status;

    private Map<Integer, GameData> gameNumberToGame = new HashMap<>();

    public PostLoginClient(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input){
        try {
            var tokensFromUser = input.toLowerCase().split(" ");
            var cmd = (tokensFromUser.length > 0) ? tokensFromUser[0] : "help";
            var params = Arrays.copyOfRange(tokensFromUser, 1, tokensFromUser.length);

            return switch (cmd){
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (DataAccessException e){
            return e.getMessage();
        }
    }

    public String create(String... params) throws DataAccessException {
        StringBuilder gameName = new StringBuilder();
        for (int i = 0; i < params.length; ++i){
            gameName.append(params[i]);
            if (i != params.length - 1) {
                gameName.append(" ");
            }
        }

        CreateRequest createRequest = new CreateRequest(gameName.toString());
        CreateResult createResult = server.create(createRequest, authToken);

        return String.format("Game " + gameName + " has been created with gameID " + createResult.gameID() + "\n");
    }

    public String list() throws DataAccessException {
        StringBuilder gameList = new StringBuilder();
        ListResult listResult = server.list(authToken);

        gameNumberToGame.clear();

        for (int i = 0; i < listResult.games().size(); ++i){
            int listIndex = i + 1;
            gameList.append("Game ");
            gameList.append(listIndex);
            gameList.append(" | Name: ");
            gameList.append(listResult.games().get(i).gameName());

            gameList.append(" | White Player: ");
            if (listResult.games().get(i).whiteUsername() == null){
                gameList.append("NO PLAYER");
            }
            else {
                gameList.append(listResult.games().get(i).whiteUsername());
            }

            gameList.append(" | Black Player: ");
            if (listResult.games().get(i).blackUsername() == null){
                gameList.append("NO PLAYER");
            }
            else {
                gameList.append(listResult.games().get(i).blackUsername());
            }
            gameList.append("\n");

            gameNumberToGame.put(listIndex, listResult.games().get(i));
        }

        return gameList.toString();

    }

    public String join(String... params) throws DataAccessException {
        return null;
    }

    public String observe(String... params) throws DataAccessException {
        return null;
    }

    public String logout() throws DataAccessException {
        server.logout(authToken);
        return "logout";
    }
    public String help(){
        return """
                
                create <GAME_NAME>           - make a new game
                list                         - output all games
                join <GAME_ID> <WHITE|BLACK> - join a game as a player with chosen team
                observe <GAME_ID>            - join a game as an observer
                logout
                help
                
                """;
    }

    public void setAuthToken(String newAuthToken){
        authToken = newAuthToken;
    }

    public void setStatus(UserStatus newStatus){
        status = newStatus;
    }

    public UserStatus getStatus(){
        return status;
    }
}

