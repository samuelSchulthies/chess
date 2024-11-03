package dataaccess;

import model.GameData;
import requestresult.ListRequest;
import requestresult.ListResult;

import java.sql.SQLException;
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
    public void clear() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var clearGamesStatement = "TRUNCATE games";
            try (var ps = conn.prepareStatement(clearGamesStatement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getGameDataCollection() {
        return null;
    }

    @Override
    public int getGameDataCollectionSize() {
        return 0;
    }
}