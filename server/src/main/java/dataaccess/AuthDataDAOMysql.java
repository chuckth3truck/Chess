package dataaccess;

import com.google.gson.Gson;
import model.authData;
import model.userData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthDataDAOMysql implements authDataAccess{
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
    public authData createNewAuth(String username) {
        var statement = "INSERT INTO auth (authToken, json) VALUES (?, ?)";
        String token = UUID.randomUUID().toString();
        authData auth = new authData(token, username);
        try {
            executeUpdate(statement, token, new Gson().toJson(auth));
        }
        catch (Exception e){
            System.out.println("could not add user");
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
        authData authData = new Gson().fromJson(json, authData.class);
        return authData.authToken();
    }

    @Override
    public void deleteAuth(String authToken){
        var statement = "DELETE FROM auth WHERE authToken=?";
        try {
            executeUpdate(statement, authToken);
        }
        catch (Exception e){
            System.out.println("could not delete auth");
        }
    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE user";
        executeUpdate(statement);

    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()), 500);
        }
    }

}
