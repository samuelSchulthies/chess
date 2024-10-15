package dataaccess;

import model.GameData;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import requestresult.ListRequest;
import requestresult.ListResult;

public interface GameDAO {
    CreateResult createGame(CreateRequest r) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ListResult listGames(ListRequest r) throws DataAccessException;

    void updateGame(int gameID) throws DataAccessException;

}