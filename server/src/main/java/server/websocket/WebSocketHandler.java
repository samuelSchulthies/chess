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
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    final static Map<Integer, Character> INT_TO_ALPHA_CHAR = Map.of(
            1, 'a',
            2, 'b',
            3, 'c',
            4, 'd',
            5, 'e',
            6, 'f',
            7, 'g',
            8, 'h'
    );
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

        if (userService.getAuthTokenDAO().getAuth(userGameCommand.getAuthToken()) == null) {
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: Invalid authtoken";
            error.setErrorMessage(errorMessage);
            session.getRemote().sendString(error.toString());
            return;
        }

        String username = userService.getUsername(userGameCommand.getAuthToken());

        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(session, username, userGameCommand.getGameID());
            case MAKE_MOVE -> makeMove(username, userGameCommand.getMove(), userGameCommand.getGameID());
            case LEAVE -> leave(username, userGameCommand.getGameID());
            case RESIGN -> resign(username, userGameCommand.getGameID());
        }
    }

    private void connect(Session session, String username, int gameID) throws DataAccessException, IOException {

        connections.add(username, session, gameID);
        String playerStatus;

        if (gameService.getGameDAO().getGame(gameID) == null){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: This game does not exist";
            error.setErrorMessage(errorMessage);
            connections.broadcastOne(error, username, gameID);
            connections.remove(username, gameID);
            return;
        }

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
        notification.setMessage(message);
        connections.broadcastAll(notification, username, gameID);

        var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        var loadGameMessage = "game";
        loadGame.setLoadGame(loadGameMessage);
        connections.broadcastOne(loadGame, username, gameID);
    }

    private void makeMove(String username, ChessMove move, int gameID) throws DataAccessException, IOException {

        GameData game = gameService.getGameDAO().getGame(gameID);
        ChessPiece piece = game.game().getBoard().getPiece(move.getStartPosition());

        if (getUser(username, game, false) == null){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: Observer cannot make a move";
            error.setErrorMessage(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

        if (isAlone(username, game)){
            return;
        }

        if (piece == null){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: There is no piece at the given location";
            error.setErrorMessage(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

        if (piece.getTeamColor() != getTeam(username, game, false)){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: You cannot move the opposing team's pieces";
            error.setErrorMessage(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

        if (!game.game().getGameOver()){
            try {
                game.game().makeMove(move);
            } catch (InvalidMoveException e){
                var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                var errorMessage = String.format("Error: " + e.getMessage());
                error.setErrorMessage(errorMessage);
                connections.broadcastOne(error, username, gameID);
                return;
            }
        }
        else {
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: Game is over, no more moves can be made";
            error.setErrorMessage(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(username + " moved " +
                INT_TO_ALPHA_CHAR.get(move.getStartPosition().getColumn()) + move.getStartPosition().getRow() + " to "
                + INT_TO_ALPHA_CHAR.get(move.getEndPosition().getColumn()) + move.getEndPosition().getRow());
        notification.setMessage(message);
        connections.broadcastAll(notification, username, gameID);

        if(game.game().isInStalemate(getTeam(username, game, true))) {
            game.game().setGameOver(true);
            var notificationStalemate = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            var messageStalemate = "The game is in stalemate and no more moves can be made";
            notificationStalemate.setMessage(messageStalemate);
            connections.broadcastOne(notificationStalemate, username, gameID);
            connections.broadcastAll(notificationStalemate, username, gameID);
        }

        if(game.game().isInCheckmate(getTeam(username, game, true))) {
            game.game().setGameOver(true);
            var notificationCheckMate = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            var messageCheckMate = String.format(getUser(username, game, true) + " is in checkmate");
            notificationCheckMate.setMessage(messageCheckMate);
            connections.broadcastOne(notificationCheckMate, username, gameID);
            connections.broadcastAll(notificationCheckMate, username, gameID);
        }

        sendUpdate(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        if (game.game().isInCheck(getTeam(username, game, true)) &&
                (!game.game().isInCheckmate(getTeam(username, game, true)))){
            var notificationCheck = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            var messageCheck = String.format(getUser(username, game, true) + " is in check");
            notificationCheck.setMessage(messageCheck);
            connections.broadcastOne(notificationCheck, username, gameID);
            connections.broadcastAll(notificationCheck, username, gameID);
        }

        var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        var loadGameMessage = "game";
        loadGame.setLoadGame(loadGameMessage);
        connections.broadcastOne(loadGame, username, gameID);
        connections.broadcastAll(loadGame, username, gameID);
    }

    private void leave(String username, int gameID) throws IOException, DataAccessException {

        GameData game = gameService.getGameDAO().getGame(gameID);
        if (Objects.equals(game.whiteUsername(), username)){
            sendUpdate(game.gameID(), null, game.blackUsername(),
                    game.gameName(), game.game());
        }
        else if (Objects.equals(game.blackUsername(), username)){
            sendUpdate(game.gameID(), game.whiteUsername(), null,
                    game.gameName(), game.game());
        }

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(username + " has left the game");
        notification.setMessage(message);
        connections.broadcastAll(notification, username, gameID);
        connections.remove(username, gameID);
    }

    private void resign(String username, int gameID) throws DataAccessException, IOException {

        GameData game = gameService.getGameDAO().getGame(gameID);

        if (getUser(username, game, false) == null){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: Cannot resign as an observer";
            error.setErrorMessage(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

        if (isAlone(username, game)){
            return;
        }

        if (game.game().getGameOver()) {
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: Cannot resign in an ended game";
            error.setErrorMessage(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

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
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var errorMessage = "Error: User not found in game when attempting to resign";
            error.setErrorMessage(errorMessage);
            connections.broadcastOne(error, username, gameID);
            return;
        }

        game.game().setGameOver(true);

        sendUpdate(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        var message = String.format(losingTeam + ", " + username + ", " +
                "has forfeit the game and " + winningTeam + " wins");
        notification.setMessage(message);
        connections.broadcastOne(notification, username, gameID);
        connections.broadcastAll(notification, username, gameID);

    }

    public void sendUpdate(int gameID, String whiteUsername, String blackUsername,
                           String gameName, ChessGame game) throws DataAccessException {
        GameData updatedGame = new GameData(gameID, whiteUsername,
                blackUsername, gameName, game);
        gameService.getGameDAO().updateGame(gameID, updatedGame);
    }

    public ChessGame.TeamColor getTeam(String username, GameData game, boolean opposingTeam) throws IOException {
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
        var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        var errorMessage = "Error: User team not found";
        error.setErrorMessage(errorMessage);
        connections.broadcastOne(error, username, game.gameID());
        return null;
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
//            throw new DataAccessException("(getUser) User is not in this game");
            return null;
        }
    }

    public boolean isAlone(String username, GameData game) throws DataAccessException, IOException {
        if (getUser(username, game, true) == null) {
            var errorMissingUser = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            var missingUserMessage = String.format("Error: " + getTeam(username, game, true) +
                    " is vacant. Cannot play alone.");
            errorMissingUser.setErrorMessage(missingUserMessage);
            connections.broadcastOne(errorMissingUser, username, game.gameID());
            return true;
        }
        return false;
    }
}
