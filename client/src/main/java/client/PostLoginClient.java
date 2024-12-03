package client;

import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.DataAccessException;
import model.GameData;
import requestresult.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PostLoginClient {
    private final ServerFacade server;
    private final String serverUrl;
    private String authToken = "";
    public static UserStatus status;
    private WebSocketFacade ws;
    private GameInfo gameInfo;
    private final ServerMessageHandler serverMessageHandler;
    private final Map<Integer, Integer> displayIDtoGameID = new HashMap<>();
    private final Map<Integer, GameData> gameIDtoGame = new HashMap<>();

    public PostLoginClient(ServerFacade server, String serverUrl, ServerMessageHandler serverMessageHandler) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.serverMessageHandler = serverMessageHandler;
    }

    public String eval(String input){
        try {
            var tokensFromUser = input.split(" ");
            var cmd = (tokensFromUser.length > 0) ? tokensFromUser[0].toLowerCase() : "help";
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
        if (params.length != 1) {
            throw new DataAccessException("Please enter a game name with no spaces");
        }
        var gameName = params[0];
        CreateRequest createRequest = new CreateRequest(gameName);
        server.create(createRequest, authToken);
        return String.format("Game " + gameName + " has been created\n");
    }

    public String list() throws DataAccessException {
        StringBuilder gameList = new StringBuilder();
        ListResult listResult = server.list(authToken);

        displayIDtoGameID.clear();
        gameIDtoGame.clear();

        for (int i = 0; i < listResult.games().size(); ++i){
            int listIndex = i + 1;
            if (listResult.games().get(i).game().getGameOver()){
                gameList.append("\n---Game Ended---\n");
            }
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
            if (listResult.games().get(i).game().getGameOver()){
                gameList.append("\n");
            }

            displayIDtoGameID.put(listIndex, listResult.games().get(i).gameID());
            gameIDtoGame.put(listResult.games().get(i).gameID(), listResult.games().get(i));
        }

        return gameList.toString();

    }

    public String join(String... params) throws DataAccessException {
        list();
        if(params.length != 2) {
            throw new DataAccessException("Incorrect number of join arguments. Please try again\n");
        }
        var id = params[0];
        var team = params[1].toUpperCase();
        int fetchedGameID;

        if ((!team.equals("BLACK")) && (!team.equals("WHITE"))){
            throw new RuntimeException("Team must be 'black' or 'white'");
        }

        try {
            fetchedGameID = displayIDtoGameID.get(Integer.parseInt(id));
        } catch (Throwable e){
            throw new DataAccessException("Bad input");
        }

        JoinRequest joinRequest = new JoinRequest(team, fetchedGameID);
        server.join(joinRequest, authToken);
        ws = new WebSocketFacade(serverUrl, serverMessageHandler);
        ws.openGameConnection(authToken, fetchedGameID);
        gameInfo = new GameInfo(authToken, fetchedGameID, team, gameIDtoGame.get(fetchedGameID).game());
        setStatus(UserStatus.IN_GAME);

        return String.format("You have joined game " + gameIDtoGame.get(fetchedGameID).gameName() + " as " + team + "\n");

    }

    public String observe(String... params) throws DataAccessException {
        list();
        if(params.length == 0) {
            throw new DataAccessException("Please enter an ID");
        }
        var id = params[0];
        int fetchedGameID;

        try {
            fetchedGameID = displayIDtoGameID.get(Integer.parseInt(id));
        } catch (Throwable e){
            throw new DataAccessException("Bad input");
        }

        ws = new WebSocketFacade(serverUrl, serverMessageHandler);
        ws.openGameConnection(authToken, fetchedGameID);
        gameInfo = new GameInfo(authToken, fetchedGameID, "", gameIDtoGame.get(fetchedGameID).game());
        setStatus(UserStatus.OBSERVING);

        return "";
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

    public GameData getGame(int gameID){
        return gameIDtoGame.get(gameID);
    }

    public void updateBoard(int gameID) throws DataAccessException {
        list();
        gameInfo.setBoard(gameIDtoGame.get(gameID).game().getBoard());
    }

    public void updateGame(int gameID) throws DataAccessException {
        list();
        gameInfo.setGame(gameIDtoGame.get(gameID).game());
    }
}

