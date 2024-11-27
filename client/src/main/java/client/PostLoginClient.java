package client;

import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.DataAccessException;
import model.GameData;
import requestresult.*;
import server.ServerFacade;
import ui.ChessBoardUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PostLoginClient {
    private final ServerFacade server;
    private final String serverUrl;
    private String authToken = "";
    private UserStatus status;
    private WebSocketFacade ws;
    private GameInfo gameInfo;
    private ServerMessageHandler serverMessageHandler;
    private Map<Integer, GameData> gameNumberToGame = new HashMap<>();

    public PostLoginClient(ServerFacade server, String serverUrl, ServerMessageHandler serverMessageHandler) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.serverMessageHandler = serverMessageHandler;
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
        list();
        StringBuilder gameName = new StringBuilder();
        if (params.length > 0) {
            for (int i = 0; i < params.length; ++i) {
                gameName.append(params[i]);
                if (i != params.length - 1) {
                    gameName.append(" ");
                }
            }
            CreateRequest createRequest = new CreateRequest(gameName.toString());
            CreateResult createResult = server.create(createRequest, authToken);
            //TODO: createResult returns wrong gameID from server
            int fetchedGameID = gameNumberToGame.get(createResult.gameID()).gameID();
            return String.format("Game " + gameName + " has been created with gameID " + fetchedGameID + "\n");
        }

        throw new DataAccessException("Please enter a game name");

    }

    public String list() throws DataAccessException {
        StringBuilder gameList = new StringBuilder();
        ListResult listResult = server.list(authToken);

        gameNumberToGame.clear();

        for (int i = 0; i < listResult.games().size(); ++i){
            int listIndex = i + 1;
            gameList.append("GameID ");
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
        list();
        if(params.length == 2){
            var id = params[0];
            var team = params[1].toUpperCase();
            int fetchedGameID;

            try {
                fetchedGameID = gameNumberToGame.get(Integer.parseInt(id)).gameID();
            } catch (Throwable e){
                throw new DataAccessException("Bad input");
            }

            JoinRequest joinRequest = new JoinRequest(team, fetchedGameID);
            server.join(joinRequest, authToken);
            ws = new WebSocketFacade(serverUrl, serverMessageHandler);
            ws.openGameConnection(authToken, fetchedGameID);
            gameInfo = new GameInfo(authToken, fetchedGameID);
            setStatus(UserStatus.IN_GAME);

//            ChessBoardUI.buildUI();

            return String.format("You have joined game " + id + " as " + team + "\n");
        }
        throw new DataAccessException("Incorrect number of join arguments. Please try again\n");
    }

    public String observe(String... params) throws DataAccessException {
        list();
        if(params.length > 0){
            var id = params[0];
            int fetchedTeamID;

            try {
                fetchedTeamID = gameNumberToGame.get(Integer.parseInt(id)).gameID();
            } catch (Throwable e){
                throw new DataAccessException("Bad input");
            }

            ws = new WebSocketFacade(serverUrl, serverMessageHandler);
            ws.openGameConnection(authToken, fetchedTeamID);
            setStatus(UserStatus.OBSERVING);

//            ChessBoardUI.buildUI();

            return "";
        }
        throw new DataAccessException("Please enter an ID");
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

    public String getAuthToken(){
        return authToken;
    }

    public GameInfo getGameInfo(){
        return gameInfo;
    }

    public void setStatus(UserStatus newStatus){
        status = newStatus;
    }

    public UserStatus getStatus(){
        return status;
    }

    public WebSocketFacade getWs(){
        return ws;
    }
}

