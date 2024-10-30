package dataaccess;

import model.GameData;
import requestresult.ListRequest;
import requestresult.ListResult;

import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO{
    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public void removeGame(int gameID) throws DataAccessException {

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
    public void updateGame(int gameID, GameData game) throws DataAccessException {

    }

    @Override
    public int getGameDataCollectionSize() {
        return 0;
    }

    @Override
    public ArrayList<GameData> getGameDataCollection() {
        return null;
    }

    @Override
    public void clear() {

    }
}