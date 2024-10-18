package dataaccess;

import model.GameData;
import model.UserData;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import requestresult.ListRequest;
import requestresult.ListResult;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    ArrayList<GameData> gameDataCollection = new ArrayList<>();
    @Override
    public void createGame(GameData game) throws DataAccessException {
        gameDataCollection.add(game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData data : gameDataCollection) {
            if (Objects.equals(data.gameID(), gameID)){
                return data;
            }
        }
        return null;
    }

    @Override
    public ListResult listGames(ListRequest r) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID) throws DataAccessException {

    }

    public int getGameDataCollectionSize(){
        return gameDataCollection.size();
    }

    public void clear(){
        gameDataCollection.clear();
    }
}