package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthTokenDAO implements AuthTokenDAO{

    public MySQLAuthTokenDAO() throws DataAccessException{
        configureDatabase();
    }
    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
//        AuthData authData = new AuthData(authToken, username);

        String userSQLStatement = "INSERT INTO authData (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(userSQLStatement)){
                ps.setString(1, username);
                ps.setString(2, authToken);

                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("unable to update database");
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
                        return new AuthData(rs.getString("username"),
                                rs.getString("authToken"));
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public int getAuthDataCollectionSize() {
        return 0;
    }

    private final String[] authDataCreateStatements = {
            """
            CREATE TABLE IF NOT EXISTS authData (
                username varchar(255) NOT NULL,
                authToken varchar(255) NOT NULL,
                PRIMARY KEY (authToken)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : authDataCreateStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() {

    }
}