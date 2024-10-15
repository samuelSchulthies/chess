package dataaccess;

import model.GameData;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import requestresult.ListRequest;
import requestresult.ListResult;

public class MemoryGameDAO implements GameDAO {

    @Override
    public CreateResult createGame(CreateRequest r) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public ListResult listGames(ListRequest r) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID) throws DataAccessException {

    }
}