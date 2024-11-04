package dataaccess;
import com.google.gson.Gson;
import model.UserData;
import java.sql.*;


public class UserDAOMysql implements UserDataAccess {
    final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    public UserDAOMysql() throws DataAccessException{
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
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
    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    @Override
    public void addUser(UserData userData) {
        var statement = "INSERT INTO user (username, json) VALUES (?, ?)";
        var json = new Gson().toJson(userData);
        try {
            DatabaseManager.executeUpdate(statement, userData.username(), json);
        }
        catch (Exception e){
            System.out.println("could not add user");
        }
    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE user";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public Boolean checkUserExists(String username) {
        try {
            UserData user = getUserData(username);
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }

}
