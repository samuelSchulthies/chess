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

        connections.add(username, session, gameID);
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
        connections.broadcastAll(notification, gameID);

        var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcastOne(loadGame, username, gameID);
    }

    private void makeMove(String username, ChessMove move, int gameID, String authToken,
                          MovesFromUser letterMoves) throws DataAccessException, InvalidMoveException, IOException {
        if (userService.getAuthTokenDAO().getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken");
        }

        GameData game = gameService.getGameDAO().getGame(gameID);

//        if (getUser(username, game, true) == null) {
//            var errorMissingUser = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
//            var missingUserMessage = String.format(getTeam(username, game, true) +
//                    " is vacant. Cannot play alone.");
//            errorMissingUser.setServerMessageNotification(missingUserMessage);
//            connections.broadcast(errorMissingUser);
//            return;
//        }

        if (isAlone(username, game)){
            return;
        }

        if (game.game().getTeamTurn() != getTeam(username, game, false)){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = String.format(e.getMessage());
            error.setServerMessageError(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

        if (!game.game().getGameOver()){
            try {
                game.game().makeMove(move);
            } catch (InvalidMoveException e){
                var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                var errorMessage = String.format(e.getMessage());
                error.setServerMessageError(errorMessage);
                connections.broadcastOne(error, username, gameID);
                return;
            }
        }
        else {
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Game is over, no more moves can be made";
            error.setServerMessageError(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

        sendUpdate(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(username + " moved " + letterMoves.startPosition()
                + " to " + letterMoves.endPosition());
        notification.setServerMessageNotification(message);
        connections.broadcastAll(notification, gameID);

        if(game.game().isInCheckmate(getTeam(username, game, true))) {
            game.game().setGameOver(true);
            var notificationCheckMate = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            var messageCheckMate = String.format(getUser(username, game, true) + " is in checkmate");
            notificationCheckMate.setServerMessageNotification(messageCheckMate);
            connections.broadcastAll(notificationCheckMate, gameID);
        }

        if (game.game().isInCheck(getTeam(username, game, true))){
            var notificationCheck = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            var messageCheck = String.format(getUser(username, game, true) + " is in check");
            notificationCheck.setServerMessageNotification(messageCheck);
            connections.broadcastAll(notificationCheck, gameID);
        }

        var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcastAll(loadGame, gameID);
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
            throw new DataAccessException("(leave) User is not in this game");
        }

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(username + " has left the game");
        notification.setServerMessageNotification(message);
        connections.broadcastAll(notification, gameID);
        connections.remove(username, gameID);
    }

    private void resign(String username, String authToken, int gameID) throws DataAccessException, IOException {
        if (userService.getAuthTokenDAO().getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken");
        }

        if (isAlone(username, gameService.getGameDAO().getGame(gameID))){
            return;
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
            throw new DataAccessException("(resign) User is not in this game");
        }

        game.game().setGameOver(true);

        sendUpdate(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(losingTeam + ", " + username + ", " +
                "has forfeit the game and " + winningTeam + " wins");
        notification.setServerMessageNotification(message);
        connections.broadcastAll(notification, gameID);

    }

    public void sendUpdate(int gameID, String whiteUsername, String blackUsername,
                           String gameName, ChessGame game) throws DataAccessException {
        GameData updatedGame = new GameData(gameID, whiteUsername,
                blackUsername, gameName, game);
        gameService.getGameDAO().updateGame(gameID, updatedGame);
    }

    public ChessGame.TeamColor getTeam(String username, GameData game, boolean opposingTeam) throws DataAccessException {
        if (Objects.equals(game.whiteUsername(), username)){
            if (!opposingTeam) {
                return ChessGame.TeamColor.WHITE;
            }
            else {
                return ChessGame.TeamColor.BLACK;
            }
        }
        if (Objects.equals(game.blackUsername(), username)){
            if (!opposingTeam) {
                return ChessGame.TeamColor.BLACK;
            }
            else {
                return ChessGame.TeamColor.WHITE;
            }
        }
        throw new DataAccessException("(getTeam) User is not in this game");
    }

    public String getUser(String username, GameData game, boolean opposingUser) throws DataAccessException {
        if (Objects.equals(game.whiteUsername(), username)){
            if (!opposingUser) {
                return game.whiteUsername();
            }
            else {
                return game.blackUsername();
            }
        }
        if (Objects.equals(game.blackUsername(), username)){
            if (!opposingUser) {
                return game.blackUsername();
            }
            else {
                return game.whiteUsername();
            }
        }
        else {
            throw new DataAccessException("(getUser) User is not in this game");
        }
    }

    public boolean isAlone(String username, GameData game) throws DataAccessException, IOException {
        if (getUser(username, game, true) == null) {
            var errorMissingUser = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var missingUserMessage = String.format(getTeam(username, game, true) +
                    " is vacant. Cannot play alone.");
            errorMissingUser.setServerMessageNotification(missingUserMessage);
            connections.broadcastOne(errorMissingUser, username, game.gameID());
            return true;
        }
        return false;
    }
}
