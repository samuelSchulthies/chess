package service;

import chess.ChessGame;
import dataaccess.AuthTokenDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.UserData;
import requestresult.*;

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
    CreateResult create(CreateRequest r) throws DataAccessException {
        if (authTokenDAO.getAuth(r.authToken()) != null) {
            if (r.gameName() != null) {
                GameData game = new GameData(gameID, "whiteUsername", "blackUsername", r.gameName(), new ChessGame());
                gameID++;
                gameDAO.createGame(game);
                return new CreateResult(gameID);
            }
            else {
                throw new DataAccessException("game name cannot be empty");
            }
        }
        else {
            throw new DataAccessException("invalid authtoken");
        }
    }
    JoinResult join(JoinRequest r){
        throw new RuntimeException("join not implemented");
    }
    ListResult list(ListRequest r){
        throw new RuntimeException("list not implemented");
    }

    int gameDataSize(){
        return gameDAO.getGameDataCollectionSize();
    }

    GameDAO getGameDAO(){
        return gameDAO;
    }
}