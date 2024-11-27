package server.websocket;

import com.google.gson.Gson;
import exception.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    UserService userService;
    GameService gameService;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(UserService userService, GameService gameService){
        this.userService = userService;
        this.gameService = gameService;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        String username = userService.getUsername(userGameCommand.getAuthToken());

        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(session, username, userGameCommand.getGameID(), userGameCommand.getAuthToken());
            case MAKE_MOVE -> makeMove(session, username);
            case LEAVE -> leave(username, userGameCommand.getGameID(), userGameCommand.getAuthToken());
            case RESIGN -> resign(session, username);
        }
    }

    private void connect(Session session, String username, int gameID, String authToken) throws DataAccessException, IOException {
        if (userService.getAuthTokenDAO().getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken");
        }

        connections.add(username, session);
        String playerStatus;

        if (Objects.equals(gameService.getGameDAO().getGame(gameID).whiteUsername(), username)){
            playerStatus = "white";
        }
        else if (Objects.equals(gameService.getGameDAO().getGame(gameID).blackUsername(), username)){
            playerStatus = "black";
        }
        else {
            playerStatus = "an observer";
        }

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(username + " has joined the game as " + playerStatus);
        notification.setServerMessageNotification(message);
        connections.broadcast(notification);

        var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(loadGame);
    }

    private void makeMove(Session session, String username){

    }

    private void leave(String username, int gameID, String authToken) throws IOException, DataAccessException {
        if (userService.getAuthTokenDAO().getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken");
        }

        GameData currentGame = gameService.getGameDAO().getGame(gameID);
        GameData updatedGame;

        if (Objects.equals(currentGame.whiteUsername(), username)){
            updatedGame = new GameData(currentGame.gameID(), null, currentGame.blackUsername(),
                    currentGame.gameName(), currentGame.game());
        }
        else if (Objects.equals(currentGame.blackUsername(), username)){
            updatedGame = new GameData(currentGame.gameID(), currentGame.whiteUsername(), null,
                    currentGame.gameName(), currentGame.game());
        }
        else {
            throw new DataAccessException("User is not in this game");
        }

        gameService.getGameDAO().updateGame(gameID, updatedGame);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(username + " has left the game");
        notification.setServerMessageNotification(message);
        connections.broadcast(notification);
        connections.remove(username);
    }

    private void resign(Session session, String username){

    }
}
