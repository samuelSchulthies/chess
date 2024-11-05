package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import requestresult.ListRequest;
import requestresult.ListResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO{

    public MySQLGameDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void createGame(GameData game) throws DataAccessException {
        String gameSQLStatement =
                "INSERT INTO game (whiteUsername, blackUsername, gameName, gameJSON) VALUES (?, ?, ?, ?)";
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        if (game.whiteUsername() == null){
            whiteUsername = "";
        }
        if (game.blackUsername() == null){
            blackUsername = "";
        }
        var gameJSON = new Gson().toJson(game.game());
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(gameSQLStatement)){
                ps.setString(1, whiteUsername);
                ps.setString(2, blackUsername);
                ps.setString(3, game.gameName());
                ps.setString(4, gameJSON);

                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    private void removeGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var removeGameStatement = "DELETE FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(removeGameStatement)){
                ps.setInt(1, gameID);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var getGameStatement =
                    "SELECT gameID, whiteUsername, blackUsername, gameName, gameJSON FROM game WHERE gameID=?";

            try (var ps = conn.prepareStatement(getGameStatement)){
                ps.setInt(1, gameID);

                try (var rs = ps.executeQuery()){
                    if (rs.next()) {
                        return new GameData(rs.getInt("gameID"),
                                rs.getString("whiteUsername"), rs.getString("blackUsername"),
                                rs.getString("gameName"), readGame(rs));
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public ListResult listGames(ListRequest r) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        removeGame(gameID);
        createGame(game);
    }

    private final String[] gameCreateStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID int NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(256) NOT NULL,
                blackUsername VARCHAR(256) NOT NULL,
                gameName VARCHAR(256) NOT NULL,
                gameJSON TEXT DEFAULT NULL,
                PRIMARY KEY (gameID)
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

    private ChessGame readGame (ResultSet rs) throws SQLException{
        var gameJSON = rs.getString("gameJSON");
        return new Gson().fromJson(gameJSON, ChessGame.class);
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