package dataaccess;

import java.sql.SQLException;
import Exception.DataAccessException;

public class SQLUtility {

    private final String[] createStatements;

    public SQLUtility(String[] createStatements) {
        this.createStatements = createStatements;
    }

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

}
