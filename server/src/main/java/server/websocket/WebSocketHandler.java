package server.websocket;

import chess.*;
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
    public void onMessage(Session session, String message) throws DataAccessException, IOException, InvalidMoveException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        String username = userService.getUsername(userGameCommand.getAuthToken());

        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(session, username, userGameCommand.getGameID(), userGameCommand.getAuthToken());
            case MAKE_MOVE -> makeMove(username, userGameCommand.getMove(), userGameCommand.getGameID(),
                    userGameCommand.getAuthToken(), userGameCommand.getLetterMoves());
            case LEAVE -> leave(username, userGameCommand.getGameID(), userGameCommand.getAuthToken());
            case RESIGN -> resign(username, userGameCommand.getAuthToken(), userGameCommand.getGameID());
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

    private void makeMove(String username, ChessMove move, int gameID, String authToken,
                          MovesFromUser letterMoves) throws DataAccessException, InvalidMoveException, IOException {
        if (userService.getAuthTokenDAO().getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken");
        }

        GameData game = gameService.getGameDAO().getGame(gameID);
//        if(game.game().isInCheckmate(getUserTeam(username, game))){
//            game.game().setGameOver(true);
//        }
        if (!game.game().getGameOver()){
            try {
                game.game().makeMove(move);
            } catch (InvalidMoveException e){
                var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                var errorMessage = String.format(e.getMessage());
                error.setServerMessageError(errorMessage);
                connections.broadcast(error);
            }
        }
        else {
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Game is over, no more moves can be made";
            error.setServerMessageError(errorMessage);
            connections.broadcast(error);
        }

        sendUpdate(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(username + " moved " + letterMoves.startPosition()
                + " to " + letterMoves.endPosition());
        notification.setServerMessageNotification(message);
        connections.broadcast(notification);

        if(game.game().isInCheckmate(getTeam(username, game, true))) {
            game.game().setGameOver(true);
            var notificationCheckMate = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            var messageCheckMate = String.format(getUser(username, game, true) + " is in checkmate");
            notificationCheckMate.setServerMessageNotification(messageCheckMate);
            connections.broadcast(notificationCheckMate);
        }

        if (game.game().isInCheck(getTeam(username, game, true))){
            var notificationCheck = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            var messageCheck = String.format(getUser(username, game, true) + " is in check");
            notificationCheck.setServerMessageNotification(messageCheck);
            connections.broadcast(notificationCheck);
        }

        var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(loadGame);
    }

    private void leave(String username, int gameID, String authToken) throws IOException, DataAccessException {
        if (userService.getAuthTokenDAO().getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken");
        }

        GameData game = gameService.getGameDAO().getGame(gameID);
        if (Objects.equals(game.whiteUsername(), username)){
            sendUpdate(game.gameID(), null, game.blackUsername(),
                    game.gameName(), game.game());
        }
        else if (Objects.equals(game.blackUsername(), username)){
            sendUpdate(game.gameID(), game.whiteUsername(), null,
                    game.gameName(), game.game());
        }
        else {
            throw new DataAccessException("User is not in this game");
        }

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(username + " has left the game");
        notification.setServerMessageNotification(message);
        connections.broadcast(notification);
        connections.remove(username);
    }

    private void resign(String username, String authToken, int gameID) throws DataAccessException, IOException {
        if (userService.getAuthTokenDAO().getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken");
        }

        GameData game = gameService.getGameDAO().getGame(gameID);
        String winningTeam;
        String losingTeam;

        if (!Objects.equals(game.whiteUsername(), username)){
            winningTeam = "white";
            losingTeam = "Black";
        }
        else if (!Objects.equals(game.blackUsername(), username)){
            winningTeam = "black";
            losingTeam = "White";
        }
        else {
            throw new DataAccessException("User is not in this game");
        }

        game.game().setGameOver(true);

        sendUpdate(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(losingTeam + ", " + username + ", " +
                "has forfeit the game and " + winningTeam + " wins");
        notification.setServerMessageNotification(message);
        connections.broadcast(notification);

    }

    public void sendUpdate(int gameID, String whiteUsername, String blackUsername,
                           String gameName, ChessGame game) throws DataAccessException {
        GameData updatedGame = new GameData(gameID, whiteUsername,
                blackUsername, gameName, game);
        gameService.getGameDAO().updateGame(gameID, updatedGame);
    }

    public ChessGame.TeamColor getTeam(String username, GameData game, boolean opposingTeam) throws DataAccessException {
        if (Objects.equals(game.whiteUsername(), username) && (!opposingTeam)){
            return ChessGame.TeamColor.WHITE;
        }
        else if (Objects.equals(game.blackUsername(), username) && (!opposingTeam)){
            return ChessGame.TeamColor.BLACK;
        }
        else {
            throw new DataAccessException("User is not in this game");
        }
    }

    public String getUser(String username, GameData game, boolean opposingUser) throws DataAccessException {
        if (Objects.equals(game.whiteUsername(), username) && (!opposingUser)){
            return game.whiteUsername();
        }
        else if (Objects.equals(game.blackUsername(), username) && (!opposingUser)){
            return game.blackUsername();
        }
        else {
            throw new DataAccessException("User is not in this game");
        }
    }
}
