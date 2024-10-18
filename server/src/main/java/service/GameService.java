package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.UserData;
import requestresult.*;

import java.util.UUID;

public class GameService {

    private final GameDAO gameDAO;
    private int gameID = 0;

    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
    CreateResult create(CreateRequest r) throws DataAccessException {
        if ((r.gameName() != null) && (r.authToken() != null)) {
            GameData game = new GameData(gameID, "whiteUsername", "blackUsername", r.gameName(), new ChessGame());
            gameID++;
            gameDAO.createGame(game);
            return new CreateResult(gameID);
        }
        else {
            throw new DataAccessException("one of the game creation fields is empty");
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