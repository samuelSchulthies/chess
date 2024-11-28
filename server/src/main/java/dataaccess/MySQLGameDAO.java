package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import requestresult.ListResult;
import service.GameService;
import exception.DataAccessException;

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
                "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameJSON) VALUES (?, ?, ?, ?, ?)";
        var gameJSON = new Gson().toJson(game.game());
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(gameSQLStatement)){
                ps.setInt(1, game.gameID());
                ps.setString(2, game.whiteUsername());
                ps.setString(3, game.blackUsername());
                ps.setString(4, game.gameName());
                ps.setString(5, gameJSON);

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
    public ListResult listGames() throws DataAccessException {
        var gameList = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()){
            var getGameStatement =
                    "SELECT gameID, whiteUsername, blackUsername, gameName, gameJSON FROM game";

            try (var ps = conn.prepareStatement(getGameStatement)){
                try (var rs = ps.executeQuery()){
                    while (rs.next()) {
                        gameList.add(new GameData(rs.getInt("gameID"),
                                rs.getString("whiteUsername"), rs.getString("blackUsername"),
                                rs.getString("gameName"), readGame(rs)));
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return new ListResult(gameList);
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
//        removeGame(gameID);
//        createGame(game);
        String gameSQLStatement =
                "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameJSON = ? WHERE gameID = ?";
        var gameJSON = new Gson().toJson(game.game());

        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(gameSQLStatement)){
                ps.setString(1, game.whiteUsername());
                ps.setString(2, game.blackUsername());
                ps.setString(3, gameJSON);
                ps.setInt(4, gameID);

                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }

    }

    private final String[] gameCreateStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID int NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(256),
                blackUsername VARCHAR(256),
                gameName VARCHAR(256) NOT NULL,
                gameJSON TEXT DEFAULT NULL,
                PRIMARY KEY (gameID)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        SQLUtility configureDatabase = new SQLUtility(gameCreateStatements);
        configureDatabase.configureDatabase();
    }

    private ChessGame readGame (ResultSet rs) throws SQLException{
        var gameJSON = rs.getString("gameJSON");
        return new Gson().fromJson(gameJSON, ChessGame.class);
    }

    @Override
    public void clear(GameService gameService) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var clearGamesStatement = "TRUNCATE game";
            try (var ps = conn.prepareStatement(clearGamesStatement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
//        gameService.resetGameID();
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