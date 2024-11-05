package dataaccess;

import chess.ChessGame;
import model.GameData;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import requestresult.ListRequest;
import requestresult.ListResult;
import service.GameService;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData game) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ListResult listGames() throws DataAccessException;

    void updateGame(int gameID, GameData game) throws DataAccessException;

    void clear(GameService gameService) throws DataAccessException;

    ArrayList<GameData> getGameDataCollection();

    int getGameDataCollectionSize();
}