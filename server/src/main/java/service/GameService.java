package service;

import chess.ChessGame;
import dataaccess.AuthTokenDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.UserData;
import requestresult.*;

import java.util.Objects;
import java.util.UUID;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthTokenDAO authTokenDAO;
    private final UserDAO userDAO;
    private int gameID = 0;

    public GameService(GameDAO gameDAO, AuthTokenDAO authTokenDAO, UserDAO userDAO){
        this.gameDAO = gameDAO;
        this.authTokenDAO = authTokenDAO;
        this.userDAO = userDAO;
    }
    public CreateResult create(CreateRequest r) throws DataAccessException {
        if (authTokenDAO.getAuth(r.authToken()) != null) {
            if ((r.gameName() != null) && (!r.gameName().equals(""))) {
                GameData game = new GameData(gameID, "", "", r.gameName(), new ChessGame());
                gameID++;
                gameDAO.createGame(game);
                return new CreateResult(game.gameID());
            }
            else {
                throw new DataAccessException("game name cannot be empty");
            }
        }
        else {
            throw new DataAccessException("invalid authtoken");
        }
    }
    public JoinResult join(JoinRequest r) throws DataAccessException {
        if (authTokenDAO.getAuth(r.authToken()) != null){
            if (gameDAO.getGame(r.gameID()) != null){
                GameData game = gameDAO.getGame(r.gameID());
                if (!Objects.equals(game.whiteUsername(), "")
                        && !Objects.equals(game.blackUsername(), "")){
                    throw new DataAccessException("game is full");
                }
                if (Objects.equals(r.playerColor(), "BLACK")
                        && (Objects.equals(game.blackUsername(), ""))){
                    GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(),
                            authTokenDAO.getAuth(r.authToken()).username(), game.gameName(), game.game());
                    gameDAO.updateGame(game.gameID(), updatedGame);
                }
                else if (Objects.equals(r.playerColor(), "BLACK")) {
                    throw new DataAccessException("black is taken");
                }

                if (Objects.equals(r.playerColor(), "WHITE")
                        && (Objects.equals(game.whiteUsername(), ""))){
                    GameData updatedGame = new GameData(game.gameID(), authTokenDAO.getAuth(r.authToken()).username(),
                            game.blackUsername(), game.gameName(), game.game());
                    gameDAO.updateGame(game.gameID(), updatedGame);
                }
                else if (Objects.equals(r.playerColor(), "WHITE")){
                    throw new DataAccessException("white is taken");
                }

                return new JoinResult();

            }
            else {
                throw new DataAccessException("invalid gameID");
            }
        }
        else {
            throw new DataAccessException("invalid authtoken");
        }

    }
    public ListResult list(ListRequest r) throws DataAccessException {
        if (authTokenDAO.getAuth(r.authToken()) != null){
            return new ListResult(gameDAO.getGameDataCollection());
        }
        else {
            throw new DataAccessException("invalid authtoken");
        }
    }

    int gameDataSize(){
        return gameDAO.getGameDataCollectionSize();
    }

    GameDAO getGameDAO(){
        return gameDAO;
    }
}