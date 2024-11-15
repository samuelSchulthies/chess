package dataaccess;

import model.AuthData;
import exception.DataAccessException;

import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthTokenDAO implements AuthTokenDAO{

    public MySQLAuthTokenDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();

        String userSQLStatement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(userSQLStatement)){
                ps.setString(1, authToken);
                ps.setString(2, username);

                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }

        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var getAuthStatement = "SELECT username, authToken FROM authData WHERE authToken=?";

            try (var ps = conn.prepareStatement(getAuthStatement)){
                ps.setString(1, authToken);

                try (var rs = ps.executeQuery()){
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"),
                                rs.getString("username"));
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var clearAuthRowStatement = "DELETE FROM authData WHERE authToken=?";
            try (var ps = conn.prepareStatement(clearAuthRowStatement)){
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] authDataCreateStatements = {
            """
            CREATE TABLE IF NOT EXISTS authData (
                authToken varchar(255) NOT NULL,
                username varchar(255) NOT NULL,
                PRIMARY KEY (authToken)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        SQLUtility configureDatabase = new SQLUtility(authDataCreateStatements);
        configureDatabase.configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var clearAuthDataStatement = "TRUNCATE authData";
            try (var ps = conn.prepareStatement(clearAuthDataStatement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int getAuthDataCollectionSize() {
        return 0;
    }
}