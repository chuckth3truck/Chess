package dataaccess;

import com.google.gson.Gson;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class AuthDataDAOMysql implements AuthDataAccess {
     final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    public AuthDataDAOMysql() throws DataAccessException{
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public AuthData createNewAuth(String username) {
        var statement = "INSERT INTO auth (authToken, json) VALUES (?, ?)";
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, username);
        try {
            DatabaseManager.executeUpdate(statement, token, new Gson().toJson(auth));
        }
        catch (Exception e){
            System.out.println("could not add user");
            return null;
        }
        return auth;
    }

    @Override
    public String getUserByAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, json FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }
    private String readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        AuthData authData = new Gson().fromJson(json, AuthData.class);
        return authData.username();
    }

    @Override
    public void deleteAuth(String authToken){
        var statement = "DELETE FROM auth WHERE authToken=?";
        try {
            DatabaseManager.executeUpdate(statement, authToken);
        }
        catch (Exception e){
            System.out.println("could not delete auth");
        }
    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE auth";
        DatabaseManager.executeUpdate(statement);

    }

}
