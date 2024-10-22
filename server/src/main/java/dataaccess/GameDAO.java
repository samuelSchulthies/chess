package dataaccess;

import chess.ChessGame;
import model.GameData;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import requestresult.ListRequest;
import requestresult.ListResult;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData game) throws DataAccessException;

    void removeGame(int gameID) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ListResult listGames(ListRequest r) throws DataAccessException;

    void updateGame(int gameID, GameData game) throws DataAccessException;

    int getGameDataCollectionSize();

    ArrayList<GameData> getGameDataCollection();

    void clear();

}