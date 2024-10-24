package service;

import chess.ChessGame;
import dataaccess.AuthTokenDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import requestresult.*;

import java.util.Objects;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthTokenDAO authTokenDAO;
    private int gameID = 1;

    public GameService(GameDAO gameDAO, AuthTokenDAO authTokenDAO){
        this.gameDAO = gameDAO;
        this.authTokenDAO = authTokenDAO;
    }
    public CreateResult create(CreateRequest r, String authToken) throws DataAccessException {
        if (authTokenDAO.getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken", 401);
        }
        if ((r.gameName() == null) || (r.gameName().equals(""))) {
            throw new DataAccessException("game name cannot be empty", 404);
        }
        GameData game = new GameData(gameID, null, null, r.gameName(), new ChessGame());
        gameID++;
        gameDAO.createGame(game);
        return new CreateResult(game.gameID());
    }
    public JoinResult join(JoinRequest r, String authToken) throws DataAccessException {
        if (authTokenDAO.getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken", 401);
        }
        if (gameDAO.getGame(r.gameID()) == null) {
            throw new DataAccessException("invalid gameID", 400);
        }
        GameData game = gameDAO.getGame(r.gameID());
        if (r.playerColor() == null) {
            throw new DataAccessException("team color cannot be empty", 400);
        }
        if (!Objects.equals(game.whiteUsername(), null)
                && !Objects.equals(game.blackUsername(), null)){
            throw new DataAccessException("game is full", 403);
        }
        if (Objects.equals(r.playerColor(), "BLACK")
                && (Objects.equals(game.blackUsername(), null))){
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(),
                    authTokenDAO.getAuth(authToken).username(), game.gameName(), game.game());
            gameDAO.updateGame(game.gameID(), updatedGame);
        }
        else if (Objects.equals(r.playerColor(), "BLACK")) {
            throw new DataAccessException("black is taken", 403);
        }
        if (Objects.equals(r.playerColor(), "WHITE")
                && (Objects.equals(game.whiteUsername(), null))){
            GameData updatedGame = new GameData(game.gameID(), authTokenDAO.getAuth(authToken).username(),
                    game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(game.gameID(), updatedGame);
        }
        else if (Objects.equals(r.playerColor(), "WHITE")){
            throw new DataAccessException("white is taken", 403);
        }

        return new JoinResult();



    }
    public ListResult list(String authToken) throws DataAccessException {
        if (authTokenDAO.getAuth(authToken) != null){
            return new ListResult(gameDAO.getGameDataCollection());
        }
        else {
            throw new DataAccessException("invalid authtoken", 401);
        }
    }

    int gameDataSize(){
        return gameDAO.getGameDataCollectionSize();
    }

    GameDAO getGameDAO(){
        return gameDAO;
    }
}