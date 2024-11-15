package dataaccess;

import model.UserData;
import Exception.DataAccessException;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void createUser(UserData u) throws DataAccessException {
        String userSQLStatement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(userSQLStatement)){
                ps.setString(1, u.username());
                ps.setString(2, u.password());
                ps.setString(3, u.email());

                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("unable to update database");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var getUserStatement = "SELECT username, password, email FROM user WHERE username=?";

            try (var ps = conn.prepareStatement(getUserStatement)){
                ps.setString(1, username);

                try (var rs = ps.executeQuery()){
                    if (rs.next()) {
                        return new UserData(rs.getString("username"),
                                rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    private final String[] userCreateStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(256) NOT NULL,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL,
                PRIMARY KEY (username)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        SQLUtility configureDatabase = new SQLUtility(userCreateStatements);
        configureDatabase.configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var clearUsersStatement = "TRUNCATE user";
            try (var ps = conn.prepareStatement(clearUsersStatement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int getUserDataCollectionSize() {
        return 0;
    }
}