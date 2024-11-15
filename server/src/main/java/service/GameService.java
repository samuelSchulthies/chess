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
            throw new DataAccessException("invalid authtoken");
        }
        if ((r.gameName() == null) || (r.gameName().equals(""))) {
            throw new DataAccessException("game name cannot be empty");
        }
        GameData game = new GameData(gameID, null, null, r.gameName(), new ChessGame());
        gameID++;
        gameDAO.createGame(game);
        return new CreateResult(game.gameID());
    }
    public JoinResult join(JoinRequest r, String authToken) throws DataAccessException {
        if (authTokenDAO.getAuth(authToken) == null) {
            throw new DataAccessException("invalid authtoken");
        }
        if (gameDAO.getGame(r.gameID()) == null) {
            throw new DataAccessException("invalid gameID");
        }
        GameData game = gameDAO.getGame(r.gameID());
        if (r.playerColor() == null) {
            throw new DataAccessException("team color cannot be empty");
        }
        if (!Objects.equals(game.whiteUsername(), null)
                && !Objects.equals(game.blackUsername(), null)){
            throw new DataAccessException("game is full");
        }
        if (Objects.equals(r.playerColor(), "BLACK")
                && (Objects.equals(game.blackUsername(), null))){
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(),
                    authTokenDAO.getAuth(authToken).username(), game.gameName(), game.game());
            gameDAO.updateGame(game.gameID(), updatedGame);
        }
        else if (Objects.equals(r.playerColor(), "BLACK")) {
            throw new DataAccessException("black is taken");
        }
        if (Objects.equals(r.playerColor(), "WHITE")
                && (Objects.equals(game.whiteUsername(), null))){
            GameData updatedGame = new GameData(game.gameID(), authTokenDAO.getAuth(authToken).username(),
                    game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(game.gameID(), updatedGame);
        }
        else if (Objects.equals(r.playerColor(), "WHITE")){
            throw new DataAccessException("white is taken");
        }

        return new JoinResult();



    }
    public ListResult list(String authToken) throws DataAccessException {
        if (authTokenDAO.getAuth(authToken) != null){
            return gameDAO.listGames();
        }
        else {
            throw new DataAccessException("invalid authtoken");
        }
    }

    int gameDataSize(){
        return gameDAO.getGameDataCollectionSize();
    }

    public void resetGameID(){
        gameID = 1;
    }

    public GameDAO getGameDAO(){
        return gameDAO;
    }
}