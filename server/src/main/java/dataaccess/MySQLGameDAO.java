package dataaccess;

import model.GameData;
import requestresult.ListRequest;
import requestresult.ListResult;

import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO{

    public MySQLGameDAO() throws DataAccessException {
        configureDatabase();
    }
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

    private final String[] gameCreateStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID int NOT NULL,
                whiteUsername VARCHAR(256) NOT NULL,
                blackUsername VARCHAR(256) NOT NULL,
                gameName VARCHAR(256) NOT NULL,
                gameJSON TEXT DEFAULT NULL,
                PRIMARY KEY (gameID),
                FOREIGN KEY (whiteUsername) REFERENCES user(whiteUsername),
                FOREIGN KEY (blackUsername) REFERENCES user(blackUsername)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : gameCreateStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var clearGamesStatement = "TRUNCATE game";
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