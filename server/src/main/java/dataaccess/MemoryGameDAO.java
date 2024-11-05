package dataaccess;

import chess.ChessGame;
import model.GameData;
import requestresult.ListRequest;
import requestresult.ListResult;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    final private ArrayList<GameData> gameDataCollection = new ArrayList<>();
    @Override
    public void createGame(GameData game) throws DataAccessException {
        gameDataCollection.add(game);
    }

    private void removeGame(int gameID) throws DataAccessException{
        gameDataCollection.remove(getGame(gameID));
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
    public ListResult listGames() throws DataAccessException {
        return new ListResult(gameDataCollection);
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        removeGame(gameID);
        createGame(game);
    }

    public int getGameDataCollectionSize(){
        return gameDataCollection.size();
    }

    public ArrayList<GameData> getGameDataCollection(){
        return gameDataCollection;
    }

    public void clear(){
        gameDataCollection.clear();
    }
}