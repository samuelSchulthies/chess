package client;

import dataaccess.DataAccessException;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient {
    private final ServerFacade server;
    private String authToken = "";

    private UserStatus status;

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
                case "list" -> list(params);
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

    public String list(String... params) throws DataAccessException {
        return null;
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

